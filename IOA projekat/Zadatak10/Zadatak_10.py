import math
import random

# Aleksandar Suvacarov 2020/0355

A = [1, 4, 0]
B = [3, 2, 0]
C = [2, 7, 1]
D = [6, 3, 3]
E = [7, 6, 5]
F = [5, 7, 4]

Npop = 50
w = 0.729
c1 = 1.494
c2 = 1.494

a_granica = -10
b_granica = 10

def rastojanje(x, y):
    return math.sqrt((x[0] - y[0])**2 + (x[1] - y[1])**2 + (x[2] - y[2])**2)

def opt_fja(s):

    d_a = rastojanje([s[0], s[1], s[2]], A)
    d_b = rastojanje([s[0], s[1], s[2]], B)
    d_c = rastojanje([s[0], s[1], s[2]], C)
    d_d = rastojanje([s[3], s[4], s[5]], D)
    d_e = rastojanje([s[3], s[4], s[5]], E)
    d_f = rastojanje([s[3], s[4], s[5]], F)

    d_s = rastojanje([s[0], s[1], s[2]], [s[3], s[4], s[5]])

    f = d_a + d_b + d_c + d_d + d_e + d_f + d_s

    return f

x = [[random.uniform(a_granica, b_granica) for i in range(6)] for _ in range(Npop)]
p_f_best = [100000 for _ in range(Npop)]
p_x_best = [[0, 0, 0, 0, 0, 0] for _ in range(Npop)]

g_f_best = 100000
g_x_best = [0, 0, 0, 0, 0, 0]

v = [[0, 0, 0, 0, 0, 0] for _ in range(Npop)]

br_iteracija = 1000
iter = 0

f_error = 10**(-3)
error = 1000
prev_g_f_best = 1000

while True:

    for i in range(len(x)):
        f = opt_fja(x[i])
        if p_f_best[i] > f:
            p_f_best[i] = f
            p_x_best[i] = [_ for _ in x[i]]
        if g_f_best > f:
            g_f_best = f
            g_x_best = [_ for _ in x[i]]
        sab1 = [w * _ for _ in v[i]]
        randa1 = random.random()
        sab2 = [c1 * randa1 * (p - x) for p, x in zip(p_x_best[i], x[i])]
        randa2 = random.random()
        sab3 = [c2 * randa2 * (g - x) for g, x in zip(g_x_best, x[i])]

        v[i] = [l1 + l2 + l3 for l1, l2, l3 in zip(sab1, sab2, sab3)]
        x[i] = [v + x for v, x in zip(v[i], x[i])]
    # print(iter, end=": ")
    # print(g_f_best)
    error = prev_g_f_best - g_f_best
    prev_g_f_best = g_f_best

    if iter > br_iteracija:
        break
    iter += 1

print("minimalna_duzina_puta: ", end="")
print(g_f_best)
print("Tacke S: ", end="")
print(g_x_best)