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

    def get_first_node(self):
        i = self.glava.header
        b = i.data
        self.remove_node(i.data)
        return b

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

    def is_empty(self):
        return True if self.glava.br_elem == 0 else False

    def is_full(self, n):
        return True if self.glava.br_elem == n else False


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


if __name__ == "__main__":

    import random

    def random_broj(broj):
        return random.randint(0, broj - 1)

    def ispis(i):
        a = int(i[4][0])
        print("Student {} {} upisao je {}. godinu studija". format(i[0], i[1], a + 1))

    def simulacija(x, lista, red):
        k = 0
        while lista.glava.br_elem > 0:
            broj = random_broj(lista.glava.br_elem)
            el = lista.get_node(broj)
            red.add_node_ate(el)
            lista.remove_node(el)

        while red.glava.br_elem > 0:
            i = red.glava.header
            r = random.random()
            if r > x:
                ispis(i.data)
                red.remove_node(i.data)
            else:
                red.add_node_ate(i.data)
            red.remove_node(i.data)
            k = k + 1
        print("Broj koraka simulacije je {}\n". format(k))


    def unos_studenta(lista):
        x = []
        x.append(input("Unesite ime: "))
        x.append(input("Unesite prezime: "))
        x.append(input("Unesite indeks: "))
        x.append(input("Unesite studijski program: "))
        x.append(input("Unesite godinu studija(kao ceo broj): "))
        if "" in x or not(x[4].isdigit()): print("Pokusajte ponovo. Pogresni podaci"); return
        lista.add_node_ate(x)
        print("Broj studenata je {}\n".format(lista.glava.br_elem))

    def meni():
        print("Unesite studenta.......1\n"
              "Pokrenite simulaciju...2\n"
              "Zavrsite program.......3\n")


    a = 1
    lista = single_linked_list()
    red = single_linked_list()

    while True:

        if a == 1:
            print("Unesite studenta")
            unos_studenta(lista)
            meni()

        if a == 2:
            if lista.glava.br_elem != 0:
                print("Unesite prag")
                try:
                    x = float(input())
                except: print("Pokusajte ponovo. Pogresan prag")
                else:
                    if x > 0.5 or x < 0: print("Pokusajte ponovo. Pogresan prag")
                    else:
                        simulacija(x, lista, red)
                        lista = single_linked_list()
                        red = single_linked_list()
                        meni()
            else: print("Nepostojeci spisak studenata"); meni()

        if a == 3:
            print("Hvala na paznji!!!")
            break

        a = 0
        try:
            a = int(input())
            if a > 3 or a < 1: print("Pokusajte ponovo\n"); meni()
        except: print("Pokusajte ponovo\n"); meni()
