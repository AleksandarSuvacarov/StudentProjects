from scipy.optimize import linprog


f = 5
p = 4

Cene = [
    [10, 30, 50, 70, 20],
    [20, 30, 35, 45, 65],
    [40, 10, 20, 15, 30],
    [60, 15, 30, 20, 60]
]

c = []
for i in range(f):
    for j in range(p):
        c.append(0.1 * Cene[j][i])


Aub = []
st = 0
fn = p
for i in range(f):
    cnt = 0
    red = []
    for j in range(f * p):
       if cnt < fn and cnt >= st:
           red.append(1)
       else:
           red.append(0)
       cnt += 1
    st += p
    fn += p
    Aub.append(red)

Ub = [500, 300, 700, 250, 750]

Aeb = []
st = 0
piv = 0
for i in range(p):
    cnt = 0
    red = []
    piv = st
    for j in range(f * p):
       if cnt == piv:
           red.append(1)
           piv += f - 1
       else:
           red.append(0)
       cnt += 1
    st += 1
    Aeb.append(red)

Eb = [1000, 500, 200, 300]

result = linprog(c, A_ub=Aub, b_ub=Ub, A_eq=Aeb, b_eq=Eb, method='highs')


resenje = []
for e in result.x:
    resenje.append(int(e))
print("Vrednosti:", resenje)
print("Min cena:", int(result.fun))

