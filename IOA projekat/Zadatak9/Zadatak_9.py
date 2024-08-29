import math
import random

# Aleksandar Suvacarov 2020/0355

S=[-0.09940112332480822, -0.09570265063923192, -0.07782620994584906, -0.044595775065571636, -0.008470411838648773, -0.0013292572938769093, -0.01402876134848341, 0.0011785680597112547, -0.0016096599564817682, -0.03141072397571561, -0.05773121434057853, -0.07098734083487862, -0.07421256224434619, -0.09674779542915338, -0.13216942328836218, -0.18406033359301877, -0.24214426775005213, -0.25978279767024376, -0.2186443973931424, -0.3289283483195699, -0.4205252223787085, -0.32130499477499636, -0.205134242990832, -0.13760381018149595]
l = 6
Npop = 50
n = 24
F = 0.8
CR = 0.9

X = [[] for _ in range(Npop)]
Z = [[0 for i in range(l)] for _ in range(Npop)]
Y = [[0 for i in range(l)] for _ in range(Npop)]

koordinate = []

x_start = -15
y_start = -15

f_min = 10**6
resenje = []

for j in range(7):
    koordinate.append([x_start, y_start])
    if j != 6: x_start += 5
y_start += 5
for j in range(6):
    koordinate.append([x_start, y_start])
    if j != 5: y_start += 5
x_start -= 5
for j in range(6):
    koordinate.append([x_start, y_start])
    if j != 5: x_start -= 5
y_start -= 5
for j in range(5):
    koordinate.append([x_start, y_start])
    if j != 4: y_start -= 5


def inicijalizacija():
    for i in range(Npop):
        x = [random.uniform(-15, 15) for _ in range(l)]
        X[i] = x

def opt_fja(x):
    x1 = x[0]
    y1 = x[1]
    x2 = x[2]
    y2 = x[3]
    A1 = x[4]
    A2 = x[5]

    for el in x:
        if abs(el) > 15:
            return 10**6

    f = 0
    for i in range(n):
        f += (A1/math.sqrt((x1 - koordinate[i][0])**2 + (y1 - koordinate[i][1])**2) + A2/math.sqrt((x2 - koordinate[i][0])**2 + (y2 - koordinate[i][1])**2) - S[i])**2
    return f

def selekcija():
    vektor = [[], [], []]
    for i in range(Npop):
        br = 0
        while(br < 3):
            randa = random.randint(0, Npop - 1)
            if randa == i or X[randa] in vektor: continue
            vektor[br] = X[randa]
            br += 1
        r = [F * (a - b) for a, b in zip(vektor[1], vektor[2])]
        z = [a + b for a, b in zip(vektor[0], r)]
        Z[i] = z

def varijacija():
    for i in range(Npop):
        R = random.randint(0, l - 1)
        for j in range(l):
            ri = random.random()
            if ri < CR or i == R:
                Y[i][j] = Z[i][j]
            else:
                Y[i][j] = X[i][j]

def prihvatanje():
    global f_min, resenje
    for i in range(Npop):
        r1 = opt_fja(Y[i])
        r2 = opt_fja(X[i])
        if r1 < r2:
            X[i] = [ _ for _ in Y[i]]
            if r1 < f_min:
                f_min = r1
                resenje = X[i]
        else:
            if r2 < f_min:
                f_min = r2
                resenje = X[i]

f_error = 10**(-15)

inicijalizacija()
iterator = 0
while f_min > f_error:
    selekcija()
    varijacija()
    prihvatanje()
    # if iterator % 1000 == 0:
    #     print(f_min)
    iterator += 1

print(resenje)
print(f_min)
# print(iterator)

# print(opt_fja([-8.500000180618501, 5.000000202020459, 6.50000149709974, -6.500000537019586, -2.9999998472483065, 0.999999879857795]))




