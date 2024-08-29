import numpy as np
import matplotlib.pyplot as plt
import math


n = 6
beta = 20 * np.pi
teta = np.pi / 3
d = 1. / 20
delta = 0

#fi = delta + beta * d * np.cos(teta)

niz_Fs = []
niz_delta = []

for i in range(100):
    delta = delta + 2 * np.pi/100
    Fs = 0
    for k in range(n):
        fi = delta + beta * d * np.cos(teta)
        Fs = Fs + complex(np.cos(k * fi), -np.sin(k * fi))
        #print(Fs)
    niz_Fs.append(abs(Fs))
    niz_delta.append(delta)

plt.plot(niz_delta, niz_Fs)
plt.xlabel("delta")
plt.ylabel("|Fs|")
plt.savefig("grafik.png")
plt.show()


x1 = 4.2
x2 = 5.1
x3 = 4.4
x_min = 0
#Brentov metod
e = 1
cnt = 0
prosla_vrednost = 10000
while e > 10 ** -7:
    cnt = cnt + 1
    X = [x1, x2, x3]
    F = []
    for delta in X:
        Fs = 0
        for k in range(n):
            fi = delta + beta * d * np.cos(teta)
            Fs = Fs + complex(np.cos(k * fi), -np.sin(k * fi))
        F.append(-abs(Fs))
    X = np.array([[x1 ** 2, x1, 1], [x2 ** 2, x2, 1], [x3 ** 2, x3, 1]])
    #print(X)

    F = np.array(F)
    X_inv = np.linalg.inv(X)

    C = np.dot(X_inv, F)
    x_min = -C[1]/(2*C[0])

    Fs = 0
    delta = x_min
    for k in range(n):
        fi = delta + beta * d * np.cos(teta)
        Fs = Fs + complex(np.cos(k * fi), -np.sin(k * fi))
    F = F.tolist()
    F.append(-abs(Fs))

    e = prosla_vrednost - F[3]
    e = abs(e)
    prosla_vrednost = F[3]

    max_elem = max(F)
    max_index = F.index(max_elem)

    X = [x1, x2, x3]
    X.append(x_min)
    X.pop(max_index)

    x1 = X[0]
    x2 = X[1]
    x3 = X[2]

print("delta = ", x_min)
print("|Fs| = ", abs(Fs))
#print("cnt = ",cnt)