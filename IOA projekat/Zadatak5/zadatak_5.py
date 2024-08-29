from numpy import e
from scipy.ndimage import label
from scipy.optimize import minimize
import random
import matplotlib.pyplot as plt

with open('data_5.txt', 'r') as f:
    lines = f.readlines()
X = []
Y = []
for line in lines:
    values = line.split()
    X.append(float(values[0]))
    Y.append(float(values[1]))

n = 51
l = 4
broj_pokusaja = 10
W = []
min_f = 1000
min_w = []
trazeno_f = 10**(-2)


def izlaz(x_in, W):
    y_out = 0
    for k in range(l):
        x_k = x_in * W[k] + W[k + 5]
        y_k = 1 / (1 + e**(-x_k))
        y_out = y_out + y_k * W[k + 10]
    y_out = y_out + W[15]
    return y_out

def funkcija(W):
    fun = 0
    for i in range(n):
        fun = fun + (izlaz(X[i], W) - Y[i]) ** 2
    fun = fun / n
    return fun


br_iteracija = 0
while min_f > trazeno_f:
    W = [random.uniform(-30, 30) for _ in range(16)]
    bounds = [(-30, 30) for _ in range(16)]
    res = minimize(funkcija, W, method='Nelder-Mead', bounds=bounds)
    if res.fun < min_f:
        min_f = res.fun
        min_w = res.x
    br_iteracija = br_iteracija + 1
    if br_iteracija > 40:
        break

for x in res.x:
    print("{:.15f}".format(x))
print("\nMinimalna vrednost funkcije:", res.fun)
if(br_iteracija > 0): print("Broj iteracija je:", br_iteracija)

for i in range(15):
    W[i] = min_w[i]
Yout = []
for i in range(n):
    Yout.append(izlaz(X[i], min_w))


plt.scatter(X, Y, label="Zadato Y")
plt.scatter(X, Yout, label="Izracunato Y")
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Grafik funkcije Y = f(X)')
plt.legend()
plt.grid(True)
plt.savefig("grafik_5.png")

plt.show()