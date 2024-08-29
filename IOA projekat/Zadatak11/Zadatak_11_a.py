import numpy as np
import math
import random as rand
import matplotlib.pyplot as plt

# [10**(-2), 5*10**(-2)]
# a = 0
# [0.1, 0.4]
# b = 0
# [0.5*10**(-6), 3*10**(-6)]
# S = 0

I = 1
sigma = 58


def calculate_H_R(a, b, S):
    d = math.sqrt(4 * S / np.pi)
    N = b / d
    z_0 = b / 2 + 10 ** (-2)

    H = (N * I) / (2 * b) * ((z_0 + b / 2) / math.sqrt(a ** 2 + (z_0 + b / 2) ** 2) -
                             (z_0 - b / 2) / math.sqrt(a ** 2 + (z_0 - b / 2) ** 2))
    R = N * 2 * np.pi * a / (sigma * S)

    return H, R

def iscrtavanje(lista):
    x_plave = [element[3] for element in lista if element[5] == 0]
    y_plave = [element[4] for element in lista if element[5] == 0]
    x_crvene = [element[3] for element in lista if element[5] == 1]
    y_crvene = [element[4] for element in lista if element[5] == 1]

    # Iscrtavanje plavih tačaka
    plt.scatter(x_plave, y_plave, c='blue', s=10, label='Ostala resenja')

    # Iscrtavanje crvenih tačaka preko plavih
    plt.scatter(x_crvene, y_crvene, c='red', s=10, label='Pareto front')

    plt.title('Pareto front optimizacije elektromagneta')
    plt.xlabel('H_max')
    plt.ylabel('R_min')
    plt.legend()

    plt.savefig("grafik_11_pareto_front")
    plt.show()


def dominantno(f1, f2):

    if f2[3] >= f1[3] and f2[4] <= f1[4]:
        if f2[3] > f1[3] or f2[4] < f1[4]:
            return True
    return False


max_iter = 10 ** 6
br_iter = 0

lista_resenja = []

while (True):

    a = rand.uniform(10 ** (-2), 5 * 10 ** (-2))
    b = rand.uniform(0.1, 0.4)
    S = rand.uniform(0.5*10**(-6), 3*10**(-6))

    h, r = calculate_H_R(a, b, S)

    lista_resenja.append([a, b, S, h, r, 0])


    if br_iter == max_iter:
        break
    br_iter += 1


for i in range(br_iter):

    flag_dom = False

    for j in range(br_iter):
        if i == j:
            continue
        if lista_resenja[j][3] >= lista_resenja[i][3] and lista_resenja[j][4] <= lista_resenja[i][4]:
            if lista_resenja[j][3] > lista_resenja[i][3] or lista_resenja[j][4] < lista_resenja[i][4]:
                flag_dom = True
                break

    if not flag_dom:
        lista_resenja[i][5] = 1

    if i % 1000 == 0:
        print(i)


iscrtavanje(lista_resenja)







