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

    def __str__(self, level = 0):
        ret = self.string_matrica(level) + "\n"
        for child in self.children:
            ret += child.__str__(level + 1)
        return ret

    def string_matrica(self, level):
        matrica = ''
        for i in range(len(self.data)):
            matrica += '\t\t' * level
            for j in range(len(self.data[i])):
                matrica += (str(self.data[i][j]) if len(str(self.data[i][j])) == 2 else ' '+ str(self.data[i][j])) + ' '
            matrica += '\n'
        return matrica


if __name__ == '__main__':
    import copy

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
                print(provera_kvadrata(i.data))
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


    def dodaj2(cvor, e, cekanje):
        lista = copy.deepcopy(cvor.data)

        for i in range(len(lista)):
            for j in range(len(lista)):
                if lista[i][j] == "a" and not e in cvor.spisak:
                    lista[i][j] = e
                    if provera_pozicije(i, j, lista):
                        cvorak = node(lista)
                        cvorak.spisak = cvor.spisak + [e]
                        cvor.add_child(cvorak)
                        cekanje.append(cvorak)
                    return
        if popunjenost(lista) != 0 and provera_kvadrata(lista) != 0: cvor.valid = 1
        return

    def formiraj2(cvor, spisak):
        cekanje = [cvor]
        while len(cekanje) != 0:
            for i in spisak:
                dodaj2(cekanje[0], i, cekanje)
            cekanje.pop(0)
        return

    def level_order(root):
        dek = [root]
        old_n = 1
        while len(dek) !=  0:
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
                    #print(*dek[j].data[i], end = "   ")
                    for x in range(len(dek[j].data[i])):
                        print("{:>3}".format(dek[j].data[i][x]), end=" ")
                    print("", end = "   ")
                else:
                    print("\t\t", end = "")
                    #print(*dek[j].data[i], end = "   ")
                    for x in range(len(dek[j].data[i])):
                        print("{:>3}".format(dek[j].data[i][x]), end=" ")
                    print("", end="   ")
                    otac = dek[j].parent
            print("")


    n = 3
    kvadrat = [[2, 7, 6], [9, 5, "a"], [4, "a", 8]]
    spisak = [3, 1]
    kvadrat1 = [[1, 14, 4, 15], ["a", 11, 5, 10], ["a", 2, "a", 3], [12, 7, 9, 6]]
    spisak1 = [16, 13, 8]

    root = node(kvadrat1)
    formiraj2(root, spisak)

    level_order(root)
    print(root.__str__())
