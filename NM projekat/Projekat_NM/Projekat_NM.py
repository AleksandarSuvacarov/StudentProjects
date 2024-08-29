main_path = './dataset/'

#img_size = [64, 64]
img_size = [128, 128]
batch_size = 64

from keras.utils import image_dataset_from_directory

ulaz_trening = image_dataset_from_directory(main_path, subset='training', validation_split=0.2, seed=52,
                                            batch_size=batch_size, image_size=img_size)
ulaz_test = image_dataset_from_directory(main_path, subset='validation', validation_split=0.2, seed=52,
                                         batch_size=batch_size, image_size=img_size)

classes = ulaz_trening.class_names

#BLOKIRANJE OBAVESTENJA
import tensorflow as tf
tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)

#ISPIS BROJA PRIMERAKA PO KLASAMA NA GRAFU

#PREBROJAVANJE
import os
count = 0
boots = main_path + "/Boot"
for path in os.listdir(boots):
    if os.path.isfile(os.path.join(boots, path)):
        count += 1
boot_count = count

count = 0
sandal = main_path + "/Sandal"
for path in os.listdir(sandal):
    if os.path.isfile(os.path.join(sandal, path)):
        count += 1
sandal_count = count

count = 0
shoe = main_path + "/Shoe"
for path in os.listdir(shoe):
    if os.path.isfile(os.path.join(shoe, path)):
        count += 1
shoe_count = count

import matplotlib.pyplot as plt


#ISCRTAVANJE NA GRAFU
x = ["Boot", "Sandal", "Shoe"]
y = [boot_count, sandal_count, shoe_count]
plt.bar(x, y)

for index, value in enumerate(y):
    plt.text(value, index,
             str(value))
plt.show()

#PRIKAZIVANJE PO JEDNOG PRIMERKA SVAKE KLASE
import tensorflow as tf
plt.figure()
for i in range(len(classes)):
    filtered_ds = ulaz_trening.filter(lambda x, l: tf.math.equal(l[0], i))
    for image, label in filtered_ds.take(1):
        ax = plt.subplot(3, 3, i+1)
        plt.imshow(image[0].numpy().astype('uint8'))
        plt.title(classes[label.numpy()[0]])
        plt.axis('off')
plt.show()

# for img, lab in ulaz_trening.take(1):
#     plt.figure()
#     print(lab)
#     for k in range(3):
#         plt.subplot(2, 5, k + 1)
#         plt.imshow(img[k].numpy().astype('uint8'))
#         plt.title(classes[lab[k]])
#     plt.show()

#------------OBRADA----------------------------

from keras import Sequential
from keras import layers
from keras import models
import numpy as np


epochs = 10
my_model = 'my_model_128_no_aug_norm_v1_1'

if(not os.path.isdir(my_model)):

    # DATA AUGMENTATION
    # data_aug = Sequential([
    #     layers.RandomFlip('horizontal', input_shape=(128, 128, 3)),
    #     layers.RandomRotation(0.25),
    #     layers.RandomZoom(0.3)
    # ])
    # for img, lab in ulaz_trening.take(1):
    #     plt.figure()
    #     for k in range(10):
    #         img_aug = data_aug(img)
    #         plt.subplot(2, 5, k + 1)
    #         plt.imshow(img_aug[0].numpy().astype('uint8'))
    #         plt.title(classes[lab[0]])
    #     plt.show()
    # import random as rnd
    #
    # for img, lab in ulaz_trening:
    #     if rnd.random() < 0.1:
    #         for k in range(5):
    #             data_aug(img)


    #KREIRANJE MODELA
    model = Sequential([
        #Ovim slojem se osigurava opseg vrednosti piksela na [0, 1]
        #Reskaliranje piksela na vrednosti između 0 i 1 se obično koristi za normalizaciju ulaznih podataka u neuronsku
        #mrežu.To se radi zato što mreže za masinsko učenje obično bolje funkcionišu kada su ulazni podaci normalizovani.
        #
        #Ako su pikseli u slikama u opsegu od 0 do 255, velike vrednosti će imati veći uticaj na rezultat učenja, što
        #može dovesti do teškoća prilikom optimizacije modela.Normalizacijom na vrednosti između 0 i 1, svaki piksel će
        #imati jednaku ulogu u učenju, što može pomoći modelu da uči brže i sa manje grešaka.
        #
        #Ukratko, reskaliranje piksela na vrednosti između 0 i 1 može pomoći modelu da se bolje optimizuje i da se
        #bolje generalizuje na nove podatke.

        #layers.Rescaling(1. / 255, input_shape=(128, 128, 3)),

        layers.Conv2D(16, 3, activation='relu', padding='same', input_shape=(128, 128, 3)),
        layers.BatchNormalization(),
        layers.MaxPooling2D(2, strides=2),

        layers.Conv2D(32, 3, activation='relu', padding='same'),
        layers.MaxPooling2D(2, strides=2),

        layers.Conv2D(64, 3, activation='relu', padding='same'),
        layers.MaxPooling2D(2, strides=2),

        layers.Dropout(0.4),

        #Koristi se da bi se visedimenzionalni podaci koji dolaze na Dense sloj pretvorili u dvodimenzionalne, kakve on i ocekuje
        layers.Flatten(),

        layers.Dense(128, activation='relu'),
        layers.Dense(len(classes), 'softmax')
    ])

    # model=Sequential()
    # model.add(layers.Conv2D(16,(3,3),input_shape=[128,128,3],activation='relu'))
    # model.add(layers.MaxPooling2D(2,2))
    # model.add(layers.Conv2D(32,(3,3),activation='relu'))
    # model.add(layers.MaxPooling2D(2,2))
    # model.add(layers.Conv2D(64,(3,3),activation='relu'))
    # model.add(layers.Conv2D(64,(3,3),activation='relu'))
    # model.add(layers.MaxPooling2D(2,2))
    # model.add(layers.Conv2D(256,(3,3),activation='relu'))
    # model.add(layers.MaxPooling2D(2,2))
    # model.add(layers.Conv2D(256,(3,3),activation='relu'))
    # model.add(layers.MaxPooling2D(2,2))
    # model.add(layers.Flatten())
    # model.add(layers.Dropout(0.5))
    # model.add(layers.Dense(64, activation='relu'))
    # model.add(layers.Dense(3, activation='sigmoid'))


    #OBUCAVANJE MODELA

    from keras.losses import SparseCategoricalCrossentropy
    #Kao optimizator je koriscen ADAM jer je najpopularniji i ima manje mana od gradijente metode koja se ranije koristila.
    #Za kriterijumsku f-ju smo koristili SparseCategoricalCrossentropy jer se ona najcesce koristi za probleme klasifikacije u vise od dve klase
    #Ova koristi labele sa celobrojnim vrednostima, za razliku od CategoricalCrossentropy, cime se poboljsavaju preformanse u memoriju i izracunavanju
    #Za aktivacionu f-ju je koriscen relu jer je najkorisceniji - brzo konvergira, nema zasicenja

    model.compile('adam', loss=SparseCategoricalCrossentropy(), metrics='accuracy')#pored accuracy u metrics moze stajati loss - odnosi se na metrike koje ce se pratiti

    history = model.fit(ulaz_trening, epochs=epochs, validation_data=ulaz_test)
    model.save(my_model)
    np.save(my_model+'/my_history.npy', history)

else:
    model = models.load_model(my_model)
    history = np.load(my_model+'/my_history.npy', allow_pickle=True).item()

#Ispis parametara obucavanja
model.summary()



#NAPREDAK OBUCAVANJA PRIKAZAN KROZ EPOHE
acc = history.history['accuracy']
val_acc = history.history['val_accuracy']

loss = history.history['loss']
val_loss = history.history['val_loss']

epochs_range = range(epochs)

plt.figure(figsize=(8, 8))
plt.subplot(1, 2, 1)
plt.plot(epochs_range, acc, label='Training Accuracy')
plt.plot(epochs_range, val_acc, label='Validation Accuracy')
plt.legend(loc='lower right')
plt.title('Training and Validation Accuracy')

plt.subplot(1, 2, 2)
plt.plot(epochs_range, loss, label='Training Loss')
plt.plot(epochs_range, val_loss, label='Validation Loss')
plt.legend(loc='upper right')
plt.title('Training and Validation Loss')
plt.show()




#PRIKAZ MATRICE KONFUZIJA
import numpy as np
from sklearn.metrics import confusion_matrix, accuracy_score, ConfusionMatrixDisplay

#PRIKAZ MATRICE KONFUZIJE NA TRENINGU
pred = np.array([])
labels = np.array([])
for img, lab in ulaz_trening:
    labels = np.append(labels, lab)
    pred = np.append(pred, np.argmax(model.predict(img, verbose=0), axis=1))

cm = confusion_matrix(labels, pred)
cm_display = ConfusionMatrixDisplay(confusion_matrix = cm,
display_labels=['Boot', 'Sandal', 'Shoe'])
cm_display.plot()
plt.show()





#PRIKAZ MATRICE KONFUZIJE NA TESTU
pred = np.array([])
labels = np.array([])
for img, lab in ulaz_test:
    labels = np.append(labels, lab)
    pred = np.append(pred, np.argmax(model.predict(img, verbose=0), axis=1))

cm = confusion_matrix(labels, pred)
cm_display = ConfusionMatrixDisplay(confusion_matrix = cm,
display_labels=['Boot', 'Sandal', 'Shoe'])
cm_display.plot()
plt.show()

acc = accuracy_score(labels, pred)
print(cm)
print(acc)







#PRIMER DOBRE I LOSE KLASIFIKACIJE
CATEGORIES = ["Boot", "Sandal", "Shoe"]
end1 = 0
end2 = 0
plt.figure()
for img, lab in ulaz_test:
    predikcija = model.predict(img)
    rez = np.argmax(predikcija, axis=1)
    for k in range(batch_size):
        #print(rez[k])
        #print(lab.numpy()[k])
        if rez[k] == lab.numpy()[k] and end1 == 0:
            plt.subplot(1, 2, 1)
            plt.imshow(img[k].numpy().astype('uint8'))
            plt.title(CATEGORIES[rez[k]])
            end1 = 1
        if rez[k] != lab.numpy()[k] and end2 == 0:
            plt.subplot(1, 2, 2)
            plt.imshow(img[k].numpy().astype('uint8'))
            plt.title(CATEGORIES[rez[k]])
            end2 = 1
        if end1 == 1 and end2 == 1:
            break
plt.show()



#PROVERA ZA PROIZVOLJNE SLIKE
# def prepare(filepath):
#     IMG_SIZE = 128
#     img_array = cv2.imread(filepath)
#     new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))
#     return new_array.reshape(-1, IMG_SIZE, IMG_SIZE, 3)
#
# import cv2
#
# prediction = model.predict([prepare("slika2.jpg")])
# print(prediction)
# ispis = np.argmax(prediction, axis = 1)
# print(ispis)
