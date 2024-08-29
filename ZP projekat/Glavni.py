from tkinter import *
from Korisnik import Korisnik
import os

class GlavniProzor:
    def __init__(self):
        # popunjavanje glavnog prozora
        self.root = Tk()
        self.root.title("PrettyGoodPrivacy")
        self.root.geometry("400x300")
        self.root.resizable(False, False)

        current_dir = os.path.dirname(os.path.abspath(__file__))
        self.key_directory = os.path.join(current_dir, "Kljucevi")
        if not os.path.exists(self.key_directory):
            os.makedirs(self.key_directory)
        self.data_directory = os.path.join(current_dir, "Podaci")
        if not os.path.exists(self.data_directory):
            os.makedirs(self.data_directory)
        self.messages_directory = os.path.join(current_dir, "Poruke")
        if not os.path.exists(self.messages_directory):
            os.makedirs(self.messages_directory)

        # elementi glavnog prozora
        self.imeKorisnikaInput = None
        self.imeKorisnika = ""

        # korisnicki podaci
        self.listaKorisnika = {"Marko": None, "Stanko": None}
        self.kreirajKorisnike()

        self.populate()
        self.root.mainloop()

    def populate(self):
        Label(self.root, text="Unesite ime korisnika").grid(row=0, column=1, sticky="EW", padx=10, pady=10)
        self.imeKorisnikaInput = Entry(self.root, width=50)
        self.imeKorisnikaInput.grid(row=1, column=1, sticky="EW", pady=10)
        dugmePrijaviSe = Button(self.root, text="Prijavi se", padx=50, command=lambda: self.prijavi_se())
        dugmePrijaviSe.grid(row=2, column=1, sticky="EW", padx=10, pady=10)

        for i in range(3):
            self.root.grid_columnconfigure(i, weight=1)

    def prijavi_se(self):
        self.imeKorisnika = self.imeKorisnikaInput.get()
        self.imeKorisnikaInput.delete(0, END)
        #print(f"Uneto ime korisnika: {self.imeKorisnika}")

        # moguce je ulogovati se samo kao postojeci korisnik
        if self.imeKorisnika in self.listaKorisnika:
            value = self.listaKorisnika[self.imeKorisnika]
            value.prikaziProzor()
    def kreirajKorisnike(self):
        for key in self.listaKorisnika.keys():
            self.listaKorisnika[key] = Korisnik(key, self)


app = GlavniProzor()
