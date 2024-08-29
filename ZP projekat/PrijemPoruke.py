import threading
from tkinter import *
from tkinter import Toplevel, Button
from tkinter import ttk
from tkinter import filedialog

from CryptoUtils import *
from GenerateRSA import *
from PravljenjePoruke import *
from Crypto.PublicKey import RSA
import base64
import re

import os


class PrijemPoruke:

    def __init__(self, Korisnik):
        self.prozor = None
        self.Korisnik = Korisnik
        self.korisnikProzor = Korisnik.prozor
        self.semafor = threading.Semaphore(0)
        self.frame = None

        self.naslov = None
        self.tekst_poruke = None
        self.timestamp_poruke = None
        self.privatniKljuc = None

        self.messageBasePath = self.Korisnik.glavnaApp.messages_directory

        self.porukaUspeha = ""
        self.porukaNeuspeha = ""


    def prikaziProzor(self):
        self.prozor = Toplevel()

        self.prozor.title(self.Korisnik.imeKorisnika)
        self.prozor.geometry("800x600")
        self.prozor.resizable(False, False)
        self.prozor.protocol("WM_DELETE_WINDOW", self.zatvoriProzor)
        self.korisnikProzor.withdraw()

        self.mejlPosiljaoca = None
        self.choosen_file = None
        self.porukaUspeha = ""
        self.porukaNeuspeha = ""
        self.unosPodatakaFlag = 1

        self.populate()


    def zatvoriProzor(self):
        self.prozor.destroy()
        self.korisnikProzor.deiconify()


    def populate(self):

        self.frame = Frame(self.prozor, width=800, height=600)
        self.frame.grid_columnconfigure(0, weight=1)
        self.frame.grid_columnconfigure(1, weight=1)
        self.frame.grid_columnconfigure(2, weight=1)
        self.frame.grid_columnconfigure(3, weight=1)
        self.frame.grid(row=0, column=0)

        if self.unosPodatakaFlag == 1:
            Label(self.frame, text="Unesite fajl u kome se nalazi poruka").grid(row=0, column=0, columnspan=2, sticky="W", padx=(30, 10), pady=30)
            self.file_name_input = Entry(self.frame)
            self.file_name_input.grid(row=0, column=2, columnspan=2, sticky="W", pady=10)
            Label(self.frame, text="  ili  ").grid(row=0, column=4, sticky="W", padx=10, pady=30)
            self.button_choose_folder = Button(self.frame, text="Izaberi file", command=self.choose_file).grid(
                row=0, column=5, sticky="W", padx=10, pady=10)
            Button(self.frame, text="Primi poruku", command=self.primiPoruku).grid(row=1, column=0, columnspan=4, sticky="W", padx=30, pady=10)


        else:
            # Dodavanje naslova
            self.title_label = Label(self.frame, text=self.naslov, font=("Arial", 16, "bold"))
            self.title_label.grid(row=0, column=0, columnspan=4, padx=20, pady=10, sticky="W")

            # Dodavanje informacija o posiljaocu
            if self.mejlPosiljaoca != None:
                self.posiljalac_labela = Label(self.frame, text="Potipisao: " + self.mejlPosiljaoca, font=("Arial", 12))
                self.posiljalac_labela.grid(row=1, column=0, columnspan=4, padx=20, pady=10, sticky="W")

            # Dodavanje teksta poruke
            self.message_label = Label(self.frame, text=self.tekst_poruke, font=("Arial", 12), wraplength=600, justify='left')
            self.message_label.grid(row=2, column=0, columnspan=4, padx=20, pady=10, sticky="W")

            # Dodavanje poruka o statusu
            self.uspeh_label = Label(self.frame, text=self.porukaUspeha, font=("Arial", 12), fg="green")
            self.uspeh_label.grid(row=3, column=0, columnspan=4, padx=20, pady=10, sticky="W")

            self.neuspeh_label = Label(self.frame, text=self.porukaNeuspeha, font=("Arial", 12), fg="red")
            self.neuspeh_label.grid(row=4, column=0, columnspan=4, padx=20, pady=10, sticky="W")

            Button(self.frame, text="Sacuvaj poruku", command=self.sacuvajPoruku).grid(row=5, column=0, columnspan=4, padx=20, pady=10, sticky="W")

    def primiPoruku(self):
        if self.choosen_file == None:
            file_name = self.file_name_input.get()
            file_path = os.path.join(self.messageBasePath, file_name)
        else:
            file_name = "postoji"
            file_path = self.choosen_file

        if file_name == "":
            self.ispisi_poruku_o_gresci("Morate uneti naziv fajla ili izabrati fajl sa porukom")
            return

        try:
            with open(file_path, "rb") as file:
                poruka = file.read()
        except FileNotFoundError:
            self.ispisi_poruku_o_gresci("Nepostojeci fajl")
            return

        try:
            # Pokušaj dekodiranja podataka iz Base64 formata
            poruka = base64.b64decode(poruka, validate=True)
        except ValueError:
            #print("Nije koriscen radix64")
            pass

        bit1, bit2, bit3, serialized_data = skiniBiteZaglavlja(poruka)
        # print(bit1)
        # print(bit2)
        # print(bit3)

        if bit1 == 1:
            # enkriptovan kljuc je duzine 256, sa paddingom, za 1024 javni 128, za 2048 javni 256
            algoritam, velikiJavni, keyid, encKey, serialized_data = deserijalizacijaEnkriptovanePoruke(
                serialized_data)
            #print(algoritam)
            #print(velikiJavni)
            if velikiJavni == 0:
                encKey = encKey[:128]
            # prepoloviti ako je neodov
            privatniKljucEnc = None
            mejlEncKljuca = None
            for r in self.Korisnik.privateRing:
                if r["KeyId"] == keyid:
                    privatniKljucEnc = r["EncPrivateKey"]
                    mejlEncKljuca = r["UserId"]
            if privatniKljucEnc == None:
                self.ispisi_poruku_o_gresci("Nemate odgovarajuci kljuc da biste dekriptovali ovu poruku")
                return
            self.dohvatiDekriptovanKljuc(privatniKljucEnc, mejlEncKljuca)

            if self.privatniKljuc == -1:
                self.ispisi_poruku_o_gresci("Pogresna lozinka")
                return
            elif self.privatniKljuc is None:
                return

            #print("Privatni kljuc:", self.privatniKljuc)
            enkriptovani_kljuc_sesije = dekriptuj_kljuc_sesije_sa_rsa(encKey, self.privatniKljuc)
            serialized_data = dekriptuj_poruku_sa_kljucem_sesije(serialized_data, enkriptovani_kljuc_sesije,
                                                                 algoritam)
            self.porukaUspeha = "Potvrdjena Tajnost "
        if bit3 == 1:
            serialized_data = dezipuj_poruku(serialized_data)

        if bit2 == 1:
            timestamp, keyid, okteti_bytes, potpis_bytes, serialized_data = deserijalizujSaPotpisom(serialized_data)
            javniKljuc = None
            for r in self.Korisnik.publicRing:
                if r["KeyId"] == keyid:
                    javniKljuc = r["PublicKey"]
                    self.mejlPosiljaoca = r["UserId"]
            if javniKljuc == None:
                self.porukaNeuspeha = "Autentikacija nije potvrdjena - nemate odgovarajuci kljuc"
            else:
                potvrda = verifikujPotpis(serialized_data, timestamp, potpis_bytes, javniKljuc)
                if potvrda == -1:
                    self.porukaNeuspeha = "Autentikacija nije potvrdjena - nemate odgovarajuci kljuc"
                else:
                    self.porukaUspeha += "Potvrdjena Autentikacija"

            # print("Timestamp:", timestamp)
            # print("KeyID:", keyid)
            # print("Okteti:", okteti_bytes)
            # print("Potpis:", potpis_bytes)

        deserialized_subject, deserialized_timestamp, deserialized_message = deserijalizacijaSamoPoruke(
            serialized_data)
        # print("Subject: ", deserialized_subject)
        # print("Timestamp: ", deserialized_timestamp)
        # print("Poruka:", deserialized_message)
        # print(self.porukaUspeha)
        # print(self.porukaNeuspeha)

        self.naslov = deserialized_subject
        self.tekst_poruke = deserialized_message
        self.timestamp_poruke = deserialized_timestamp
        self.unosPodatakaFlag = 0

        for widget in self.prozor.winfo_children():
            widget.destroy()
        self.populate()

    def dohvatiDekriptovanKljuc(self, enc_key, mejl):
        new_window = Toplevel(self.prozor)
        new_window.geometry("400x200")
        new_window.title("Lozinka")

        Label(new_window, text="Unesite lozinku privatnog kljuca za mejl: " + mejl).pack(pady=5)
        self.lozinka_input = Entry(new_window)
        self.lozinka_input.pack(pady=5)

        Button(new_window, text="Potvrdi",
               command=lambda arg1=enc_key, arg2=new_window: self.desifrujPrivatniKljuc(arg1, arg2)).pack(pady=5)

        new_window.grab_set()
        self.prozor.wait_window(new_window)

    def desifrujPrivatniKljuc(self, enc_key, prozor):
        lozinka = self.lozinka_input.get()
        self.privatniKljuc = desifruj_kljuc(enc_key, lozinka)
        prozor.destroy()

    def ispisi_poruku_o_gresci(self, poruka):
        for widget in self.prozor.winfo_children():
            widget.destroy()
        self.populate()
        Label(self.frame, text=poruka, fg="red").grid(row=2, column=1, columnspan=4, sticky="W", padx=10, pady=10)
        return

    def sacuvajPoruku(self):
        file_name = self.naslov
        file_name = re.sub(r'[\\/*?:"<>|]', "", file_name)
        file_name = file_name.replace(" ", "_")
        file_path = filedialog.asksaveasfilename(defaultextension=".txt", initialfile=file_name,
                                                 filetypes=[("Text files", "*.txt"), ("All files", "*.*")])
        if file_path:
            try:
                with open(file_path, "w", encoding="utf-8") as file:
                    file.write(self.naslov + "\n\n")
                    file.write(self.tekst_poruke)

            except Exception as e:
                print("Greška prilikom čuvanja poruke")


    def choose_file(self):
        chosen_file = filedialog.askopenfilename()
        for widget in self.prozor.winfo_children():
            widget.destroy()
        self.populate()
        if chosen_file:
            Label(self.frame, text="Usepsno ste odabrali fajl", fg="green").\
                grid(row=2, column=1, columnspan=4, sticky="W", padx=10, pady=10)
            self.choosen_file = chosen_file

