class node():
    def __init__(self, data):
        self.data = data
        self.children = []
        self.parent = None
        self.spisak = []
        self.valid = 0

    def add_child(self, child):
        child.parent = self
        self.children.append(child)

if __name__ == '__main__':
    import copy

    def savrsenost(matrica):
        i = 0
        j = 0
        n = len(matrica)
        sum1 = [0] * n
        k = 0

        while j < n:
            for i in range(n):
                sum1[k] += matrica[i % n][j % n]
                j += 1
            k += 1
            j = k

        n = len(matrica)
        i = n - 1
        j = 0
        sum2 = [0] * n
        k = 0

        while i != -1:
            for j in range(n):
                sum2[k] += matrica[i][j % n]
                i -= 1
                if i < 0: i = i + n
            k += 1
            i = n - k - 1

        for i in range(n-1):
            if sum1[i] != sum1[i+1]: return 0
        for i in range(n-1):
            if sum2[i] != sum2[i+1]: return 0
        if sum1[0] == sum2[0]: return 1


    def postorder(root):
        stack = [root]
        niz = []
        while len(stack) != 0:
            a = stack[len(stack) - 1]
            stack.pop(len(stack) - 1)
            niz.insert(0, a)
            for i in a.children:
                stack.append(i)

        for i in niz:
            if i.valid == 1:
                ispis_resenja(i.data)
                print("Karakteristicna suma je {}".format(provera_kvadrata(i.data)))
                if savrsenost(i.data):
                    print("Ovo jeste savrseni kvadrat\n")
                else:
                    print("Ovo nije savrseni kvadrat\n")
        return


    def ispis_resenja(matrica):
        for i in range(len(matrica)):
            print(*matrica[i])
        return


    def popunjenost(matrica):
        for i in range(len(matrica)):
            for j in range(len(matrica)):
                if matrica[i][j] == "a":
                    return 0
        return 1


    def provera_pozicije(a, b, matrica):
        sum1 = 0
        sum2 = 0

        for i in range(len(matrica)):
            if matrica[i][b] == "a": return 1
            sum1 += matrica[i][b]
        for j in range(len(matrica)):
            if matrica[a][j] == "a": return 1
            sum2 += matrica[a][j]
        if sum1 != sum2: return 0
        return 1


    def provera_kvadrata(matrica):
        sum1 = []
        sum2 = []
        sum3 = 0
        sum4 = 0
        for i in range(len(matrica)):
            sum = 0
            for j in range(len(matrica)):
                sum += matrica[i][j]
            sum1.append(sum)

        for j in range(len(matrica)):
            sum = 0
            for i in range(len(matrica)):
                sum += matrica[i][j]
            sum2.append(sum)

        i = j = 0
        while i < len(matrica):
            sum3 += matrica[i][j]
            i += 1
            j += 1
        i = len(matrica) - 1
        j = 0
        while j < len(matrica):
            sum4 += matrica[i][j]
            i -= 1
            j += 1
        for i in range(len(sum1) - 1):
            if sum1[i] != sum1[i + 1]: return 0
        for i in range(len(sum2) - 1):
            if sum2[i] != sum2[i + 1]: return 0
        if sum1[0] == sum2[0] == sum3 == sum4: return sum3
        return 0


    def dodaj(cvor, e, cekanje, spisak):
        lista = copy.deepcopy(cvor.data)

        for i in range(len(lista)):
            for j in range(len(lista)):
                if lista[i][j] == "a" and cvor.spisak.count(e) < spisak.count(e):
                    lista[i][j] = e
                    if provera_pozicije(i, j, lista):
                        cvorak = node(lista)
                        cvorak.spisak = cvor.spisak + [e]
                        cvor.add_child(cvorak)
                        cekanje.append(cvorak)
                    return
        if popunjenost(lista) != 0 and provera_kvadrata(lista) != 0: cvor.valid = 1
        return


    def formiraj(cvor, spisak):
        cekanje = [cvor]
        while len(cekanje) != 0:
            for i in spisak:
                dodaj(cekanje[0], i, cekanje, spisak)
            cekanje.pop(0)
        return


    def level_order(root):
        dek = [root]
        old_n = 1
        while len(dek) != 0:
            n = 0
            for i in range(len(dek)):
                for j in range(len(dek[i].children)):
                    dek.append(dek[i].children[j])
                    n += 1

            ispisi(dek, old_n)

            i = 0
            while i < old_n:
                dek.pop(0)
                i += 1
            old_n = n
            print("")
        return


    def ispisi(dek, old_n):

        for i in range(len(dek[0].data)):
            otac = dek[0].parent
            for j in range(old_n):
                if dek[j].parent == otac:
                    for x in range(len(dek[j].data[i])):
                        print("{:>3}".format(dek[j].data[i][x]), end=" ")
                    print("", end="   ")
                else:
                    print("\t\t", end="")
                    for x in range(len(dek[j].data[i])):
                        print("{:>3}".format(dek[j].data[i][x]), end=" ")
                    print("", end="   ")
                    otac = dek[j].parent
            print("")


    def meni():
        print("Unesite kvadrat..........1\n"
              "Ispis izgleda stabla.....2\n"
              "Ispis resenja kvadrata...3\n"
              "Zavrsi program...........4\n")
        return


    kvadrat = []
    prazna_polja = 0
    elementi = []
    root = None

    while True:

        meni()
        try:
            a = int(input())
            if a < 1 or a > 4: print("Pokusajte pomovo"); continue
        except: print("Pokusajte pomovo"); continue

        try:
            if a == 1:
                kvadrat = []
                prazna_polja = 0
                elementi = []
                n = int(input("Unesite dimenzije kvadrata: "))
                print("Unesite inicijalno stanje kvadrata (u prazna polja unesite slovo \"a\")")
                for i in range(n):
                    unos = input().split(" ")
                    for x in range(len(unos)):
                        if unos[x].isdigit() or unos[x][0] == "-": unos[x] = int(unos[x])
                        else: prazna_polja += 1
                    kvadrat.append(unos)
                if prazna_polja != 0:
                    print("Unesite elemente za popunjavanje kvadrata")
                    elementi = input().split(" ")
                    elementi = [int(x) for x in elementi]
                    if len(elementi) != prazna_polja: print("Neodgovarajuc broj elemenata"); continue
                root = node(kvadrat)
                if prazna_polja == 0 and provera_kvadrata(root.data) != 0: root.valid = 1
                formiraj(root, elementi)

            if a == 2:
                if len(kvadrat) != 0 and root != None:
                    level_order(root)

            if a == 3:
                if len(kvadrat) != 0 and root != None:
                    postorder(root)

            if a == 4:
                break
        except: print("Pokusajte ponovo!"); continue