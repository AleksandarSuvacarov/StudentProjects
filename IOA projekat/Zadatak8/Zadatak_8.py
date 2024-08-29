import matplotlib.pyplot as plt
import numpy as np

iteracije = []
f_opt = []
colors = ['b', 'g', 'r', 'c', 'm', 'y', 'k']
broj_ponavljanja = 20
broj_iteracija = 1000000

putanja = "D:/Materijali/7. semestar/IOA/Zadaci/ProjekatIOA/ProjekatIOA/"

#Kumulativni minimum za sva pokretanja
plt.figure(figsize=(11, 6))
for i in range(broj_ponavljanja):
    file_path = putanja + "rezultat_" + str(i) + ".txt"
    data = np.loadtxt(file_path, delimiter=',', skiprows=0)
    iteracije = data[:, 0]
    f_opt = data[:, 1]
    color = np.random.choice(colors)
    label = f'Run#{i + 1}'
    plt.loglog(iteracije, f_opt, label=label, color=color)

# Iscrtavanje grafika
plt.xlabel('Iteracije')
plt.ylabel('f_opt_min')
#plt.yscale('log')
plt.title('Grafik f_opt_min za iteracije')
plt.grid(True)
plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
plt.savefig("grafik_8_kumulativni_minimum")
plt.show()


#Srednje najbolje resenje
mean_values = np.zeros(broj_iteracija)
for i in range(broj_ponavljanja):
    filename = f'{putanja}rezultat_{i}.txt'
    data = np.loadtxt(filename, delimiter=',', skiprows=0)
    mean_values += data[:, 1]
    iteracije = data[:, 0]

#Iscrtavanje grafika
mean_values /= broj_ponavljanja
plt.loglog(iteracije, mean_values, label="Srednje najbolje resenje", color='g')
plt.xlabel('Iteracije')
plt.ylabel('f_opt_min')
plt.title('Grafik srednjeg najboljeg resenja')
plt.legend()
plt.savefig("grafik_8_srednje_najbolje")
plt.show()