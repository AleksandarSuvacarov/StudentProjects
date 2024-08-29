from tkinter import *
from tkinter import Toplevel
from tkinter import ttk
from tkinter import filedialog

from CryptoUtils import *
from GenerateRSA import *
from PravljenjePoruke import *
from Crypto.PublicKey import RSA
import base64
import re

import os

class SlanjePoruke:

    def __init__(self, Korisnik):
        self.prozor = None
        self.Korisnik = Korisnik
        self.korisnikProzor = Korisnik.prozor

        self.frame = None

        self.enkripcija_var = IntVar()
        self.potpisivanje_var = IntVar()
        self.kompresija_var = IntVar()
        self.radix64_var = IntVar()

        self.subject = None
        self.text = None

        self.biraj_javni_kljuc = None
        self.biraj_privatni_kljuc = None
        self.encryption_options = None

        self.odabraniJavniKljuc = None
        self.odabraniPrivatniKljuc = None
        self.odabraniAlgoritam = None





    def prikaziProzor(self):
        self.prozor = Toplevel()
        self.frame = Frame(self.prozor, width=800, height=600)
        self.frame.grid_columnconfigure(0, weight=1)
        self.frame.grid_columnconfigure(1, weight=1)
        self.frame.grid_columnconfigure(2, weight=1)
        self.frame.grid_columnconfigure(3, weight=1)
        self.prozor.title(self.Korisnik.imeKorisnika)
        self.prozor.geometry("800x600")
        self.prozor.resizable(False, False)
        self.prozor.protocol("WM_DELETE_WINDOW", self.zatvoriProzor)
        self.korisnikProzor.withdraw()

        self.messageBasePath = self.Korisnik.glavnaApp.messages_directory
        self.populate()

    def zatvoriProzor(self):
        self.enkripcija_var.set(0)
        self.potpisivanje_var.set(0)
        self.kompresija_var.set(0)
        self.radix64_var.set(0)
        self.prozor.destroy()
        self.korisnikProzor.deiconify()

    def populate(self):
        self.frame.grid(row=0, column=0)


        Label(self.frame, text="Unesite naslov poruke:").grid(row=0, column=0, sticky="W", padx=10, pady=10)
        self.subject = Entry(self.frame, width=50)
        self.subject.grid(row=0, column=1, columnspan = 3 , sticky="EW", padx=10, pady=10)

        Label(self.frame, text="Unesite tekst poruke:").grid(row=1, column=0, sticky="W", padx=10, pady=10)
        # Text widget za unos teksta s scrollbarom
        self.text = Text(self.frame, wrap=WORD, width=50, height=5)
        self.text.grid(row=2, column=0, columnspan=2, padx=10, pady=10, sticky="W")
        # Scrollbar za Text widget
        scrollbar = Scrollbar(self.frame, command=self.text.yview)
        scrollbar.grid(row=2, column=2, sticky="WNS")
        self.text.config(yscrollcommand=scrollbar.set)

        if len(self.Korisnik.publicRing) > 0:
            self.cb1 = Checkbutton(self.frame, text="Enkripcija poruke", variable=self.enkripcija_var, command=self.update_state)
            self.cb1.grid(row=3, column=0, sticky="W", padx=60, pady=10)
            Label(self.frame, text="Biraj javni kljuc i algoritam:").grid(row=3, column=1, sticky="W", padx=10, pady=10)
            self.odabraniJavniKljuc = StringVar()
            self.odabraniJavniKljuc.set(self.Korisnik.publicRing[0]["UserId"])
            self.biraj_javni_kljuc = ttk.Combobox(self.frame, state=DISABLED)
            self.biraj_javni_kljuc.config(textvariable=self.odabraniJavniKljuc)
            javni_values = [ceo_kljuc["UserId"] for ceo_kljuc in self.Korisnik.publicRing]
            self.biraj_javni_kljuc.config(values=javni_values)
            self.biraj_javni_kljuc.grid(row=3, column=2, sticky="EW", padx=10, pady=10)
            self.odabraniAlgoritam = StringVar()
            self.odabraniAlgoritam.set("TripleDES")
            self.encryption_options = ttk.Combobox(self.frame, values=["TripleDES", "AES128"], state=DISABLED)
            self.encryption_options.config(textvariable=self.odabraniAlgoritam)
            self.encryption_options.grid(row=3, column=3, sticky="EW", padx=10, pady=10)

        if len(self.Korisnik.privateRing) > 0:
            self.cb2 = Checkbutton(self.frame, text="Potpisivanje poruke", variable=self.potpisivanje_var, command=self.update_state)
            self.cb2.grid(row=4, column=0, sticky="W", padx=60, pady=10)
            Label(self.frame, text="Biraj privatni kljuc i loziniku:").grid(row=4, column=1, sticky="W", padx=10, pady=10)
            self.odabraniPrivatniKljuc = StringVar()
            self.odabraniPrivatniKljuc.set(self.Korisnik.privateRing[0]["UserId"])
            self.biraj_privatni_kljuc = ttk.Combobox(self.frame, state=DISABLED)
            self.biraj_privatni_kljuc.config(textvariable=self.odabraniPrivatniKljuc)
            privatni_values = [ceo_kljuc["UserId"] for ceo_kljuc in self.Korisnik.privateRing]
            self.biraj_privatni_kljuc.config(values=privatni_values)
            self.biraj_privatni_kljuc.grid(row=4, column=2, sticky="EW", padx=10, pady=10)
            self.lozinkaPrivatnogKljuca = Entry(self.frame, state=DISABLED)
            self.lozinkaPrivatnogKljuca.grid(row=4, column=3, sticky="EW", padx=10, pady=10)


        Checkbutton(self.frame, text="Kompresija", variable=self.kompresija_var).grid(row=5, column=0, sticky="W", padx=60, pady=10)
        Label(self.frame, text="Izaberi folder za cuvanje poruke:").grid(row=5, column=1, sticky="W", padx=10, pady=10)
        self.button_choose_folder = Button(self.frame, text="Izaberi folder", command=self.choose_folder).grid(row=5, column=2, sticky="W", padx=10, pady=10)
        Checkbutton(self.frame, text="Radix64", variable=self.radix64_var).grid(row=6, column=0, sticky="W", padx=60, pady=10)

        Button(self.frame, text="Pošalji poruku", command=self.posaljiPoruku).grid(row=7, column=0, columnspan=3, sticky="EW", padx=10, pady=10)

    def choose_folder(self):
        chosen_folder = filedialog.askdirectory()
        if chosen_folder:
            self.messageBasePath = chosen_folder

    def update_state(self):

        if self.potpisivanje_var.get() == 1:
            if self.biraj_privatni_kljuc is not None:
                self.biraj_privatni_kljuc.config(state=NORMAL)
                self.lozinkaPrivatnogKljuca.config(state=NORMAL)
        else:
            if self.biraj_privatni_kljuc is not None:
                self.biraj_privatni_kljuc.config(state=DISABLED)
                self.lozinkaPrivatnogKljuca.config(state=DISABLED)


        if self.enkripcija_var.get() == 1:
            if self.biraj_javni_kljuc is not None:
                self.biraj_javni_kljuc.config(state=NORMAL)
            if self.encryption_options is not None:
                self.encryption_options.config(state=NORMAL)
        else:
            if self.biraj_javni_kljuc is not None:
                self.biraj_javni_kljuc.config(state=DISABLED)
            if self.encryption_options is not None:
                self.encryption_options.config(state=DISABLED)


    def posaljiPoruku(self):

        poruka = self.text.get("1.0", END)
        subject = self.subject.get()
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        if subject == "":
            Label(self.frame, text="Mora postojati naslov", fg="red").grid(row=8, column=0, columnspan=3,
                                                                                       sticky="EW", padx=10, pady=10)
            return

        bitEnkripcija = 0
        bitPotpisivanje = 0
        bitKompresija = 0

        if self.enkripcija_var.get() == 1:
            bitEnkripcija = 1
        if self.potpisivanje_var.get() == 1:
            bitPotpisivanje = 1
        if self.kompresija_var.get() == 1:
            bitKompresija = 1


        #uvek serijalizujemo poruku
        poruka = serijalizacijaSamoPoruke(subject, timestamp, poruka)

        #dodamo deo za potpis ukoliko je stiklirano
        if self.potpisivanje_var.get() == 1:
            lozinka = self.lozinkaPrivatnogKljuca.get()
            if lozinka == "":
                Label(self.frame, text="Mora postojati lozinka", fg="red").grid(row=8, column=0, columnspan=3,
                                                                                sticky="EW", padx=10, pady=10)
                return
            #duzine imamo da su timestamp 19 bajtova, keyid 16 bajtova,2 okteta 2 bajta, potpis 128 bajtova, 1024 bita
            timestampPotpis = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            keyIdPotpis = None
            for r in self.Korisnik.privateRing:
                if r["UserId"] == self.odabraniPrivatniKljuc.get():
                    keyIdPotpis = r["KeyId"]
            privatniKljucEnc = None
            for r in self.Korisnik.privateRing:
                if r["UserId"] == self.odabraniPrivatniKljuc.get():
                    privatniKljucEnc = r["EncPrivateKey"]

            velikiPrivatni = 0
            if len(privatniKljucEnc) > 1200:
                velikiPrivatni = 1

            #print("Duzina privatnog enkriptovanog", len(privatniKljucEnc))
            # print(privatniKljucEnc)
            privatniKljuc = desifruj_kljuc(privatniKljucEnc, lozinka)
            if privatniKljuc == -1:
                Label(self.frame, text="Pogresna lozinka - neuspelo potpisivanje", fg="red").grid(row=8, column=0,
                                                                          columnspan=3, sticky="EW", padx=10, pady=10)
                return

            # print(privatniKljuc)
            potpis = napraviPotpis(poruka, timestampPotpis, privatniKljuc)
            vodeciOkteti = izvadi_vodeca_dva_okteta(potpis)

            #print("Timestamp: ", timestampPotpis)
            #print("Potpis: ", potpis)
            #print("Okteta:", vodeciOkteti)
            #print("keyid:", keyIdPotpis)

            #opet serijalizujemo poruku
            poruka = serijalizujSaPotpisom(velikiPrivatni, timestampPotpis, keyIdPotpis, vodeciOkteti, potpis, poruka)

            # print("Timestamp: ", timestampPotpis)
            # print("Potpis: ", potpis)
            # print("Okteta:",vodeciOkteti)
            # print("keyid:",keyIdPotpis)
        # print(bitEnkripcija)
        # print(bitPotpisivanje)
        # print(bitKompresija)


        #deo za zip ukoliko je stiklirano
        if bitKompresija == 1:
            poruka = zipuj_poruku(poruka)

        kluc_sesije = None
        algoritam = None
        velikiJavni = 0
        #deo za sifrovanje ukoliko je stiklirano
        if bitEnkripcija == 1:
            algoritam = self.odabraniAlgoritam.get()
            if algoritam == "TripleDES":
                kljuc_sesije = generisi_tripledes_kljuc_sesije()
            else:
                kljuc_sesije = generisi_aes_kljuc_sesije()
            poruka = enkriptuj_poruku_sa_kljucem_sesije(poruka, kljuc_sesije, algoritam)

            #print("Algoritam", algoritam)

            #dodamo deo zaglavlja
            javniKljuc = None
            for r in self.Korisnik.publicRing:
                if r["UserId"] == self.odabraniJavniKljuc.get():
                        javniKljuc = r["PublicKey"]
            #print("Duzina javnog: ", len(javniKljuc))
            if len(javniKljuc) > 300:
                velikiJavni = 1

            #print("Javni kljuc: ", javniKljuc)
            encSessionKey = enkriptuj_kljuc_sesije_sa_rsa(kljuc_sesije, javniKljuc)
            #print("Kljuc sesije: ", kljuc_sesije)


            keyIdSifrovanje = None
            for r in self.Korisnik.publicRing:
                if r["UserId"] == self.odabraniJavniKljuc.get():
                    keyIdSifrovanje = r["KeyId"]

            #dodajemo podatke keyid duzine 16 bajtova i encSesKey duzine 2048 ili 1024, 256 ili 128 bajtova
            poruka = serijalizacijaEnkriptovanePoruke(algoritam, velikiJavni, keyIdSifrovanje, encSessionKey, poruka)


        poruka = dodajBiteZaglavlja(poruka, bitEnkripcija, bitPotpisivanje, bitKompresija)

        if self.radix64_var.get() == 1:
            #print("Radix64 radimo")
            poruka = base64.b64encode(poruka)
            #print("Poruka posle radix64: ", poruka)
        else:
            #print("Poruka bez radix64: ", poruka)
            pass


        # sada je poruka spremna za slanje
        file_name = subject + "_poruka.bin"
        file_name = re.sub(r'[\\/*?:"<>|]', "", file_name)
        file_name = file_name.replace(" ", "_")
        file_path = os.path.join(self.messageBasePath, file_name)
        with open(file_path, "wb") as file:
            file.write(poruka)

        #print("slanje\n\n")
        #print("Enkriptovana poruka: ", poruka)
        #print("\n\n")

        self.enkripcija_var.set(0)
        self.potpisivanje_var.set(0)
        self.kompresija_var.set(0)
        self.radix64_var.set(0)
        self.populate()
        self.messageBasePath = self.Korisnik.glavnaApp.messages_directory
        Label(self.frame, text="Uspesno poslata poruka", fg="green").grid(row=8, column=0, columnspan=3,
                                                                        sticky="EW", padx=10, pady=10)

        # algoritam = self.odabraniAlgoritam.get()
        # javniKljucID = self.odabraniJavniKljuc.get()
        # print(algoritam)
        # print(javniKljucID)
        # #samo tajnost
        # if algoritam == "TripleDES":
        #     kljuc_sesije = generisi_tripledes_kljuc_sesije()
        # else:
        #     kljuc_sesije = generisi_aes_kljuc_sesije()
        #
        # enkriptovani_podaci = enkriptuj_poruku_sa_kljucem_sesije(poruka, kljuc_sesije,  algoritam)
        #
        # print(enkriptovani_podaci)
        #
        # javniKljuc = None
        #
        # for r in self.Korisnik.publicRing:
        #     if r["UserId"] == javniKljucID:
        #         javniKljuc = r["PublicKey"]
        # print(javniKljuc)
        #
        # privatniKljucEnc = None
        #
        # for r in self.Korisnik.privateRing:
        #     if r["UserId"] == javniKljucID:
        #         privatniKljucEnc = r["EncPrivateKey"]
        # privatniKljuc = desifruj_kljuc(privatniKljucEnc, "123")
        #
        #
        #
        #
        # kriptovani_kljuc_sesije = enkriptuj_kljuc_sesije_sa_rsa(kljuc_sesije, javniKljuc)
        # enkriptovani_kljuc_sesije = dekriptuj_kljuc_sesije_sa_rsa(kriptovani_kljuc_sesije, privatniKljuc)
        # # poruka = {"keyid" keyid, }
        #
        # dekriptovana_poruka = dekriptuj_poruku_sa_kljucem_sesije(enkriptovani_podaci, enkriptovani_kljuc_sesije, algoritam)
        # print(dekriptovana_poruka)
