class node:
    def __init__(self, data = None, next = None):
        self.data = data
        self.next = next

    def print(self):
        print(self.data)
        return

class list_header:
    def __init__(self, header = None, tail = None):
        self.data = None
        self.header = header
        self.tail = tail
        self.br_elem = 0

class single_linked_list:
    def __init__(self):
        self.head = None
        self.glava = list_header()

    def add_node_atb(self, data):

        cvor = node(data)

        if self.head == None:
            self.glava.header = cvor
            self.glava.tail = cvor
            self.head = self.glava
            cvor.next = self.glava
            self.glava.br_elem += 1
        else:
            cvor.next = self.glava.header
            self.glava.header = cvor
            self.glava.br_elem += 1


    def add_node_ate(self, data):

        cvor = node(data)

        if self.head == None:
            self.glava.header = cvor
            self.glava.tail = cvor
            self.head = self.glava
            cvor.next = self.glava
            self.glava.br_elem += 1
        else:
            cvor.next = self.glava
            self.glava.tail.next = cvor
            self.glava.tail = cvor
            self.glava.br_elem += 1

    def add_node_at(self, pos, data):
        if pos == 0: self.add_node_atb(data); return
        if pos == self.glava.br_elem - 1: self.add_node_ate(data); return

        data = node(data)

        i = self.glava.header
        pre = i
        k = 0
        while i.data != None:
            if k == pos:
                pre.next = data
                data.next = i
                self.glava.br_elem += 1
                break
            pre = i
            i = i.next
            k += 1

    def remove_node(self, data):
        if data == None or self.glava.br_elem == 0: return 0

        i = self.glava.header
        pre = None

        while i.data != None:
            if i.data == data and pre != None and i != self.glava.tail:
                pre.next = i.next
                self.glava.br_elem -= 1
                break
            elif i.data == data and pre == None:
                self.glava.header = i.next
                self.glava.br_elem -= 1
                break
            elif i.data == data and i == self.glava.tail:
                self.glava.tail = pre
                pre.next = self.glava
                self.glava.br_elem -= 1
                break
            else:
                pre = i
                i = i.next

    def find_node(self, data):
        i = self.glava.header
        k = 0
        while i.data != None:
            if i.data == data:
                return k
            k += 1
            i = i.next
        return "Greska"

    def get_node(self, pos):
        i = self.glava.header
        k = 0
        while i.data != None:
            if pos == k: return i.data
            k += 1
            i = i.next
        return "Greska"


    def delete_list(self):
        self.clear_list()
        del self

    def clear_list(self):
        i = self.glava.header
        while i.data != None:
            self.glava.header = i.next
            del i
            i = self.glava.header
        del self.glava

    def print(self):
        if self.head is None:
            print("Prazna lista")
            return

        i = self.glava.header
        lista_ispis = []

        while i.data != None:
            lista_ispis.append(i.data)
            i = i.next
        return lista_ispis


if __name__ == "__main__": #Pocetak main dela koda

    def ucitavanje_polinoma(lista_polinoma):
        try:
            temp_lista = []
            a = input().split(" ")
            if len(a) <= 0 : print("Nepostojeci polinom"); return
            b = single_linked_list()
            for i in a:
                i = i.split("x")
                e = int(i[1])
                k = float(i[0]) #Ovde u int za celobrojne koeficijente
                if k == 0 or e < 0: continue
                for j in range(0, len(temp_lista)):
                    if e == temp_lista[j][0]: temp_lista.remove(temp_lista[j]); break
                temp_lista.append([e, k])

            temp_lista.sort(key = lambda x: x[0], reverse=True)
            for i in temp_lista:
                b.add_node_ate(i)

            if b.glava.br_elem != 0:
                lista_polinoma.append(b)
            else:
                print("Uneli ste nepostojeci polinom! Pokusajte ponovo!")

        except: print("Pokusajte ponovo")
        return

    def ispisi_polinom():
        print("Izaberite polinom: ")
        try:
            a = int(input())
            if a > len(lista_polinoma) or a < 1: print("Nepostojeci polinom!"); return
            a = lista_polinoma[a-1].print()
            for i in range(0, len(a)):
                a[i] = str(a[i][1])+"x"+str(a[i][0])
            a = "+".join(a)
            for i in range(0, len(a)):
                if a[i] == "+" and a[i+1] == "-": a = a[0:i] + " " + a[i+1:]
            a = a.split(" ")
            print("".join(a))
        except: print("Pokusajte ponovo"); return

    def dodavanje_clana():
        print("Koji clan dodajete? Napomena: Ako postoji element sa istim eksponentom bice zamenjen!!!")
        try:
            a = input().split("x")
        except: print("Pokusajte ponovo"); return
        if len(a) == 0: print("Nepostojeci element!"); return

        try:
            e = int(a[1])
            if e < 0: print("Nepostojeci element!"); return
            k = float(a[0]) #Ovde u int za celobrojne koeficijente
            if k == 0: print("Nepostojeci element!"); return
            a = [e, k]
            print("Izaberite polinom kome dodajete clan. Napomena: Ako postoji element sa istim eksponentom bice zamenjen!!!")
            b = int(input())
        except: print("Pokusajte ponovo!"); return

        try:
            b = lista_polinoma[b-1]
        except: print("Nepostojeci polinom!!!"); return

        i = b.glava.header
        k = 0
        while i.data != None:
            if i.data[0] == a[0]:
                b.remove_node(i.data)
                if k == b.glava.br_elem: b.add_node_at(k - 1, a)
                else: b.add_node_at(k, a)
                break
            k += 1
            i = i.next
        else:
            i = b.glava.header
            k = 0
            while i.data != None:
                if i.data[0] < a[0]:
                    b.add_node_at(k, a)
                    break
                k += 1
                i = i.next
            else: b.add_node_ate(a)
        return

    def brisanje_clana():
        print("Izaberite element koji uklanjate:")
        try:
            a = input().split("x")
            if len(a) == 0: print("Nepostojeci element!"); return
            e = int(a[1])
            if e < 0: print("Nepostojeci element!"); return
            k = int(a[0])
            if k == 0: print("Nepostojeci element!"); return
            a = [e, k]
            print("Iz kog polinoma izbacujete element? Napomena: Ako je to poslednji clan obrisacete polinom!!!")
            b = int(input())
        except: print("Pokusajte ponovo!"); return

        try:
            b = lista_polinoma[b - 1]
        except:
            print("Nepostojeci polinom!!!"); return
        k = b.find_node(a)
        if k == "Greska": print("Nema takvog clana"); return
        b.remove_node(a)
        if b.glava.br_elem == 0: izbrisi_polinom(b); print("Obrisali ste polinom")

    def izbrisi_polinom(b):
        lista_polinoma.remove(b)
        b.delete_list()
        return

    def izracunaj_polinom():
        print("Izaberite polinom koji zelite da izracnate:")
        x = int(input())
        try:
            b = lista_polinoma[x - 1]
        except:
            print("Nepostojeci polinom!!!"); return

        print("Izaberite vrednost promenljive")
        try:
            a = int(input())
        except: print("Pokusajte ponovo"); return
        sum = 0
        i = b.glava.header
        while i.data != None:
            sum = sum + i.data[1] * a**i.data[0]
            i = i.next
        print("Vrednost polinoma je {}".format(sum))

    def mnoz(a, b):
        c = [a[0] + b[0], a[1]*b[1]]
        return c

    def sabir(a, b):
        c = [a[0], a[1] + b[1]]
        return c

    def eksponent(a, b):
        e = a[0]
        pos = 0
        i = b.glava.header
        if i == None: return "Greska"
        while i.data != None:
            if i.data[0] == e: return pos
            pos += 1
            i = i.next
        return "Greska"

    def saberi_polinome():
        if len(lista_polinoma) == 5: print("Nema mesta, obrisite neki polinom"); return

        try:
            print("Izaberite prvi sabirak")
            x = int(input())
            try:
                s1 = lista_polinoma[x - 1]
            except:
                print("Nepostojeci polinom!!!")
                return
            print("Izaberite drugi sabirak")
            x = int(input())
            try:
                s2 = lista_polinoma[x - 1]
            except:
                print("Nepostojeci polinom!!!")
                return
        except: print("Pokusajte ponovo!"); return

        c = single_linked_list()
        i1 = s1.glava.header
        i2 = s2.glava.header

        while i1.data != None and i2.data != None:
            if i1.data[0] == i2.data[0]:
                if sabir(i1.data, i2.data)[1] != 0: c.add_node_ate(sabir(i1.data, i2.data))
                i1 = i1.next
                i2 = i2.next
            elif i1.data[0] > i2.data[0]:
                c.add_node_ate(i1.data)
                i1 = i1.next
            else:
                c.add_node_ate(i2.data)
                i2 = i2.next

        if i1.data != None:
            while i1.data != None:
                c.add_node_ate(i1.data)
                i1 = i1.next
        if i2.data != None:
            while i2.data != None:
                c.add_node_ate(i2.data)
                i2 = i2.next
        if c.glava.br_elem == 0: print("Dobijeni polinom nema elemenata pa se nece ni smestiti u memoriju!")
        else: lista_polinoma.append(c)
        return

    def mnozenje_polinoma():
        if len(lista_polinoma) == 5: print("Nema mesta, obrisite neki polinom"); return

        try:
            print("Izaberite prvi cinilac")
            x = int(input())
            try:
                p1 = lista_polinoma[x - 1]
            except:
                print("Nepostojeci polinom!!!")
                return
            print("Izaberite drugi cinilac")
            x = int(input())
            try:
                p2 = lista_polinoma[x - 1]
            except:
                print("Nepostojeci polinom!!!")
                return
        except: print("Pokusajte ponovo!"); return

        l = single_linked_list()
        k = 0
        i1 = p1.glava.header
        while i1.data != None:
            i2 = p2.glava.header
            sabirak = single_linked_list()
            while i2.data != None:
                sabirak.add_node_ate(mnoz(i1.data, i2.data))
                i2 = i2.next

            l = sabiranje_pri_mnozenju(l, sabirak, k)
            i1 = i1.next
            k += 1

        lista_polinoma.append(l)
        return

    def sabiranje_pri_mnozenju(s1, s2, k):

        c = single_linked_list()
        if k == 0: i1 = s1.glava
        else: i1 = s1.glava.header
        i2 = s2.glava.header

        while i1.data != None and i2.data != None:
            if i1.data[0] == i2.data[0]:
                if sabir(i1.data, i2.data)[1] != 0: c.add_node_ate(sabir(i1.data, i2.data))
                i1 = i1.next
                i2 = i2.next
            elif i1.data[0] > i2.data[0]:
                c.add_node_ate(i1.data)
                i1 = i1.next
            else:
                c.add_node_ate(i2.data)
                i2 = i2.next

        if i1.data != None:
            while i1.data != None:
                c.add_node_ate(i1.data)
                i1 = i1.next
        if i2.data != None:
            while i2.data != None:
                c.add_node_ate(i2.data)
                i2 = i2.next

        return c



    def meni():
        print("Ucitajte novi polinom...1\n"
          "Dodajte novi clan.......2\n"
          "Izbrisite neki clan.....3\n"
          "Ispisite neki polinom...4\n"
          "Izvrsite operacije......5\n"
          "Obrisite neki polinom...6\n"
          "Zavrite program.........7\n")
        return

#Pocetak glavnog koda

    meni()
    lista_polinoma = []

    while True:
        a = 0
        try:
            a = int(input())
            if a < 1 or a > 7: print("Pokusajte ponovo"); meni()
        except: print("Pokusajte ponovo"); meni()

        if a == 1:
            if len(lista_polinoma) >= 5: print("Nema vise mesta za polinome!"); meni();
            else:
                print("Unesite polinom: ")
                ucitavanje_polinoma(lista_polinoma)
                meni()

        if a == 2:
            dodavanje_clana()
            meni()

        if a == 3:
            brisanje_clana()
            meni()

        if a == 4:
            ispisi_polinom()
            meni()

        if a == 5:
            print("Izracunaj vrednost polinoma...1\n"
                  "Saberi dva polinoma...........2\n"
                  "Pomnozi dva polinoma..........3\n")
            try:
                x = int(input())
            except:
                print("Pokusajte ponovo"); meni()
            else:
                if x < 1 or x > 3: print("Pokusajte ponovo"); meni()
                else:

                    if x == 1:
                        izracunaj_polinom()
                        meni()

                    if x == 2:
                        saberi_polinome()
                        meni()

                    if x == 3:
                        mnozenje_polinoma()
                        meni()


        if a == 6:
            print("Izaberite polinom koji brisete. Napomena: Moguca promena indeksa postojecim polinomima!!!")
            x = int(input())
            try:
                b = lista_polinoma[x - 1]
            except: print("Nepostojeci polinom!!!")
            else:
                izbrisi_polinom(b)
            meni()

        if a == 7:
            print("Hvala na paznji!")
            break


