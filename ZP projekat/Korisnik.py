from tkinter import *
from tkinter import Toplevel
from tkinter import ttk
from base64 import b64encode, b64decode
from GenerateRSA import *
from PerzistencijaPodataka import PerzistencijaPodataka
from SlanjePoruke import SlanjePoruke
from PrijemPoruke import PrijemPoruke
import os
from tkinter import font


class Korisnik:
    def __init__(self, imeKorisnika, glavnaApp):

        #korisnicki podaci
        self.imeKorisnika = imeKorisnika
        self.privateRing = []
        self.publicRing = []
        self.glavnaApp = glavnaApp
        self.glavniProzor = glavnaApp.root

        #{"Timestamp": None, "KeyId": None, "PublicKey": None, "EncPrivateKey": None, "UserId": None} - Format svakog reda u privateRing
        # {"Timestamp": None, "KeyId": None, "PublicKey": None, "UserId": None} - Format svakog reda u publicRing

        #elementi korisnickog prozora
        self.prozor = None

        self.framePrivateRing = None

        self.frameSlanjePrijem = None

        self.prozor_slanje = None

        #predefinisane vrednosti i tipovi
        self.porukaGreskeRow = 1
        self.privateRingRow = 2
        self.publicRingRow = 1
        self.bold_font = font.Font(family="Helvetica", size=8, weight="bold")

    def prikaziProzor(self):
        self.prozor = Toplevel()
        self.prozor.title(self.imeKorisnika)
        self.prozor.geometry("1400x800")
        self.prozor.resizable(False, False)
        self.prozor.protocol("WM_DELETE_WINDOW", self.zatvoriProzor)
        self.prozor_slanje = SlanjePoruke(self)
        self.prozor_prijem = PrijemPoruke(self)
        self.perzistencijaPodataka = PerzistencijaPodataka(self.glavnaApp, self.imeKorisnika)
        self.privateRing = self.perzistencijaPodataka.load_privateRing_records_from_file()
        self.publicRing = self.perzistencijaPodataka.load_publicRing_records_from_file()
        self.glavniProzor.withdraw()
        self.populate()

    def populate(self):
        # Kreiranje Canvas-a za skrolovanje
        self.canvas = Canvas(self.prozor)
        self.canvas.pack(side=LEFT, fill=BOTH, expand=True)
        # Dodavanje Scrollbara za Canvas
        scrollbar = Scrollbar(self.prozor, orient=VERTICAL, command=self.canvas.yview)
        scrollbar.pack(side=RIGHT, fill=Y)
        self.canvas.configure(yscrollcommand=scrollbar.set)
        # Glavni frame za sadržaj
        main_frame = Frame(self.canvas)
        self.canvas.create_window((0, 0), window=main_frame, anchor=NW)

        # PRIVATE RING
        self.framePrivateRing = Frame(main_frame, pady=20)
        self.framePrivateRing.grid(row=0, column=0)
        Button(self.framePrivateRing, text="Generisi novi par RSA kljuceva", width=40, padx=50, pady=10,
               command=lambda: self.generisiProzorRSA()).grid(row=0, column=0, columnspan=3, pady=10)
        Button(self.framePrivateRing, text="Uvezi ceo par kljuceva", width=40, padx=50, pady=10,
               command=lambda: self.uveziCeoKljucProzor()).grid(row=0, column=4, columnspan=3, pady=10)
        Label(self.framePrivateRing, text="Prsten Privatnih Kljuceva", padx=50, pady=10, bd=1, relief="solid") \
            .grid(row=1, column=0, columnspan=3, pady=10)
        self.popunjavanjePrivateRinga()

        # Crna linija
        separator = Frame(main_frame, height=2, bg="black")
        separator.grid(row=1, column=0, sticky="ew", padx=10, pady=20)

        # PUBLIC RING
        self.framePublicRing = Frame(main_frame, width=1300, pady=20)
        self.framePublicRing.grid(row=2, column=0)
        Label(self.framePublicRing, text="Prsten Javnih Kljuceva", padx=50, pady=10, bd=1, relief="solid") \
            .grid(row=0, column=0, columnspan=2, pady=10)
        Button(self.framePublicRing, text="Uvezi javni kljuc", width=40, padx=50, pady=10,
               command=lambda: self.uveziJavniKljucProzor()).grid(row=0, column=2, columnspan=1, pady=10)
        self.popunjavanjePublicRinga()

        # Crna linija
        separator = Frame(main_frame, height=2, bg="black")
        separator.grid(row=3, column=0, sticky="ew", padx=10, pady=20)

        # OSTALI DUGMICI
        self.frameSlanjePrijem = Frame(main_frame, pady=20)
        self.frameSlanjePrijem.grid(row=4, column=0)
        self.frameSlanjePrijem.grid_columnconfigure(0, weight=1)
        self.frameSlanjePrijem.grid_columnconfigure(1, weight=1)
        self.frameSlanjePrijem.grid_columnconfigure(2, weight=1)
        self.frameSlanjePrijem.grid_columnconfigure(3, weight=1)
        Button(self.frameSlanjePrijem, text="Posalji poruku", width=40, padx=50, pady=10,
               command=lambda: self.generisiProzorSlanje()).grid(row=0, column=1, pady=10)
        Button(self.frameSlanjePrijem, text="Primi poruku", width=40, padx=50, pady=10,
               command=lambda: self.generisiProzorPrijem()).grid(row=0, column=2, pady=10)



        # Configuracija skrolovanja za Canvas
        main_frame.update_idletasks()
        self.canvas.configure(scrollregion=self.canvas.bbox("all"))
        self.canvas.bind_all("<MouseWheel>", self.on_mousewheel)




    # ------------------------Public Ring i Uvoz javnog kljuca----------------------------------------------------------


    def uveziJavniKljucProzor(self):
        new_window = Toplevel(self.prozor)
        new_window.geometry("400x200")
        new_window.title("Uvoz kljuca")

        Label(new_window, text="Unesite ime fajla sa javnim kljucem").pack(pady=5)
        self.file_name_import = Entry(new_window)
        self.file_name_import.pack(pady=5)

        Label(new_window, text="Unesite mejl povezan sa ovim javnim kljucem").pack(pady=5)
        self.mejl_input = Entry(new_window)

        self.mejl_input.pack(pady=5)
        Button(new_window, text="Import", command=lambda arg=new_window: self.uveziJavniKljuc(arg)).pack(pady=5)

        new_window.grab_set()
        new_window.protocol("WM_DELETE_WINDOW", self.osvezi)

    def uveziJavniKljuc(self, prozor):
        file_path = os.path.join(self.glavnaApp.key_directory, self.file_name_import.get())
        mejl = self.mejl_input.get()
        if file_path == "" or mejl == "":
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Morate uneti oba podatka")
            return
        result = uvezi_javni_kljuc(file_path)
        if result == -1:
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Fajl nije pronadjen")
            return
        if result == -2:
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Pogresan format kljuca u fajlu")
            return
        javni_kljuc, timestamp, javni_kljuc_id = result

        for record in self.publicRing:
            if record["UserId"] == mejl:
                record["Timestamp"] = timestamp
                record["KeyId"] = javni_kljuc_id
                record["PublicKey"] = javni_kljuc
                self.perzistencijaPodataka.save_publicRing_record_to_file(record)
                prozor.destroy()
                self.osvezi()
                return

        record = {
            "Timestamp": timestamp,
            "KeyId": javni_kljuc_id,
            "PublicKey": javni_kljuc,
            "UserId": mejl
        }
        self.publicRing.append(record)
        self.perzistencijaPodataka.save_publicRing_record_to_file(record)

        prozor.destroy()
        self.osvezi()

    def popunjavanjePublicRinga(self):
        Label(self.framePublicRing, text="Timestamp", width=40, font=self.bold_font).grid(row=self.publicRingRow,
                                                                      column=0, sticky="EW", padx=10, pady=10)
        Label(self.framePublicRing, text="KeyId", width=50, font=self.bold_font).grid(row=self.publicRingRow,
                                                                  column=1, sticky="EW", padx=10, pady=10)
        Label(self.framePublicRing, text="PublicKey", width=40, font=self.bold_font).grid(row=self.publicRingRow,
                                                                      column=2, sticky="EW", padx=10, pady=10)
        Label(self.framePublicRing, text="UserId", width=50, font=self.bold_font).grid(row=self.publicRingRow,
                                                                   column=3, sticky="EW", padx=10, pady=10)

        for i in range(len(self.publicRing)):
            record = self.publicRing[i]
            Label(self.framePublicRing, text=record["Timestamp"], width=40).grid(row=self.publicRingRow+i+1,
                                                                         column=0, sticky="EW", padx=10, pady=10)
            Label(self.framePublicRing, text=record["KeyId"], width=50).grid(row=self.publicRingRow+i+1,
                                                                     column=1, sticky="EW", padx=10, pady=10)
            Button(self.framePublicRing, text="PublicKey",
                   width=40, command=lambda arg1=record["PublicKey"], arg2 = 1: self.showKeyFunc(arg1, arg2)).\
                grid(row=self.publicRingRow+i+1, column=2, sticky="EW", padx=10, pady=10)
            Label(self.framePublicRing, text=record["UserId"], width=50).grid(row=self.publicRingRow+i+1,
                                                                      column=3, sticky="EW", padx=10, pady=10)





    #------------------------Private Ring i Generisanje RSA-------------------------------------------------------------


    def generisiProzorRSA(self):
        new_window = Toplevel(self.prozor)
        new_window.geometry("400x300")
        new_window.title("Generisanje ključeva")

        Label(new_window, text="Unesite mejl:").pack(pady=5)
        self.mejl_input = Entry(new_window)
        self.mejl_input.pack(pady=5)

        Label(new_window, text="Unesite lozinku:").pack(pady=5)
        self.lozinka_input = Entry(new_window)
        self.lozinka_input.pack(pady=5)

        Label(new_window, text="Izaberite dužinu ključa:").pack(pady=5)
        self.duzina_kljuca_var = StringVar()
        self.duzina_kljuca_var.set("1024") # Podrazumevana vrednost

        self.duzina_kljuca_dropdown = ttk.Combobox(new_window, textvariable=self.duzina_kljuca_var)
        self.duzina_kljuca_dropdown['values'] = ("1024", "2048")
        self.duzina_kljuca_dropdown.pack(pady=5)

        Button(new_window, text="Generiši", command=lambda arg=new_window: self.generisiRSA(arg)).pack(pady=10)

        new_window.grab_set()
        new_window.protocol("WM_DELETE_WINDOW", self.osvezi)

    def generisiRSA(self, prozor):
        lozinka = self.lozinka_input.get()
        duzina_kljuca = int(self.duzina_kljuca_var.get())
        mejl = self.mejl_input.get()
        if mejl == "" or lozinka == "":
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Moraju postojati i mejl i lozinka")
            return

        for r in self.privateRing:
            if r["UserId"] == mejl:
                prozor.destroy()
                self.ispisi_gresku_na_korisnicki_prozor("Korisnik vec ima kljuc sa tim mejlom")
                return


        enc_privatni_kljuc, javni_kljuc, timestamp, key_id = generisi_rsa_kljuceve(duzina_kljuca, lozinka)

        record = {
            "Timestamp": timestamp,
            "KeyId": key_id,
            "PublicKey": javni_kljuc,
            "EncPrivateKey": enc_privatni_kljuc,
            "UserId": mejl
        }
        self.privateRing.append(record)
        self.perzistencijaPodataka.save_privateRing_record_to_file(record)

        prozor.destroy()
        self.osvezi()

    def popunjavanjePrivateRinga(self):

        Label(self.framePrivateRing, text="Timestamp", width=25, font=self.bold_font).grid(row=self.privateRingRow,
                                                                               column=0, sticky="EW", padx=10, pady=10)
        Label(self.framePrivateRing, text="KeyId", width=25, font=self.bold_font).grid(row=self.privateRingRow,
                                                                           column=1, sticky="EW", padx=10, pady=10)
        Label(self.framePrivateRing, text="PublicKey", width=25, font=self.bold_font).grid(row=self.privateRingRow,
                                                                    column=2, sticky="EW", padx=10, pady=10)
        Label(self.framePrivateRing, text="EncPrivateKey", width=25, font=self.bold_font).grid(row=self.privateRingRow,
                                                                    column=3, sticky="EW", padx=10, pady=10)
        Label(self.framePrivateRing, text="Brisanje", width=20, font=self.bold_font).grid(row=self.privateRingRow,
                                                                             column=4, sticky="EW", padx=10, pady=10)
        Label(self.framePrivateRing, text="Izvoz", width=20, font=self.bold_font).grid(row=self.privateRingRow,
                                                                     column=5, sticky="EW", padx=10, pady=10)
        Label(self.framePrivateRing, text="UserId", width=25, font=self.bold_font).grid(row=self.privateRingRow,
                                                                            column=6, sticky="EW", padx=10, pady=10)

        for i in range(len(self.privateRing)):
            record = self.privateRing[i]
            Label(self.framePrivateRing, text=record["Timestamp"], width=25).grid(row=self.privateRingRow+i+1,
                                                                         column=0, sticky="EW", padx=10, pady=10)
            Label(self.framePrivateRing, text=record["KeyId"], width=25).grid(row=self.privateRingRow+i+1,
                                                                         column=1, sticky="EW", padx=10, pady=10)
            Button(self.framePrivateRing, text="PublicKey", width=25,
                   command=lambda arg1=record["PublicKey"], arg2=1: self.showKeyFunc(arg1, arg2)).\
                    grid(row=self.privateRingRow+i+1, column=2, sticky="EW", padx=10, pady=10)
            Button(self.framePrivateRing, text="EncPrivateKey", width=25,
                   command=lambda arg1=b64encode(record["EncPrivateKey"]), arg2=2: self.showKeyFunc(arg1, arg2)).\
                grid(row=self.privateRingRow+i+1, column=3, sticky="EW", padx=10, pady=10)
            Button(self.framePrivateRing, text="Obrisi kljuc", width=20,
                   command=lambda arg=i: self.obrisiKljuc(arg)). \
                grid(row=self.privateRingRow + i + 1, column=4, sticky="EW", padx=10, pady=10)
            Button(self.framePrivateRing, text="Izvezi kljuc", width=20,
                   command=lambda arg=i: self.izveziKljucProzor(arg)). \
                grid(row=self.privateRingRow + i + 1, column=5, sticky="EW", padx=10, pady=10)
            # Label(self.framePrivateRing, text=record["Lozinka"], width=25).grid(row=self.privateRingRow+i+1,
            #                                                          column=4, sticky="EW", padx=10, pady=10)
            Label(self.framePrivateRing, text=record["UserId"], width=25).grid(row=self.privateRingRow+i+1,
                                                                       column=6, sticky="EW", padx=10, pady=10)

        #self.prozor.grid_rowconfigure(1, weight=1)
        #self.prozor.grid_columnconfigure(0, weight=1)

    def showKeyFunc(self, key, tip):
        new_window = Toplevel(self.prozor)
        new_window.geometry("600x500")
        new_window.title("Key")

        text = Text(new_window, wrap=WORD)
        text.insert(END, key.decode('utf-8'))
        text.pack(fill=BOTH, expand=True)

        scrollbar_y = Scrollbar(new_window, orient=VERTICAL, command=text.yview)
        text.configure(yscrollcommand=scrollbar_y.set)
        scrollbar_y.pack(side=RIGHT, fill=Y)

        scrollbar_x = Scrollbar(new_window, orient=HORIZONTAL, command=text.xview)
        text.configure(xscrollcommand=scrollbar_x.set)
        scrollbar_x.pack(side=BOTTOM, fill=X)

        if tip == 2:
            self.lozinka_input_ispis_pr_key = Entry(new_window)
            self.lozinka_input_ispis_pr_key.insert(0, "Lozinka")
            self.lozinka_input_ispis_pr_key.pack(pady=5)
            Button(new_window, text="Prikazi dekriptovan kljuc",
                   pady=10, command=lambda arg1=key, arg2=new_window: self.prikazi_dekriptovan_kljuc(arg1, arg2)).pack()

        new_window.grab_set()           #ne moze na ostalo da se klikce
        new_window.protocol("WM_DELETE_WINDOW", self.osvezi)


    def prikazi_dekriptovan_kljuc(self, key, prozor):
        lozinka = self.lozinka_input_ispis_pr_key.get()
        dekriptovan_kljuc = desifruj_kljuc(b64decode(key), lozinka)
        if dekriptovan_kljuc == -1:
            self.ispisi_gresku_na_korisnicki_prozor("Pogresna lozinka")
        else:
            self.showKeyFunc(dekriptovan_kljuc, 1)
        prozor.destroy()

    def obrisiKljuc(self, i):
        record = self.privateRing.pop(i)
        self.perzistencijaPodataka.delete_privateRing_record_from_file(record)
        self.osvezi()


    def on_radio_button_change(self):
        selected_option = self.radio_var.get()
        if selected_option == 2:
            self.password_entry.pack(pady=5)
        else:
            self.password_entry.pack_forget()

    def izveziKljucProzor(self, i):
        new_window = Toplevel(self.prozor)
        new_window.geometry("400x200")
        new_window.title("Key")

        self.radio_var = IntVar(value=1)  # Postavljanje podrazumevane vrednosti na prvi radio dugme

        radio_button1 = Radiobutton(new_window, text="Public key", variable=self.radio_var, value=1, command=self.on_radio_button_change)
        radio_button2 = Radiobutton(new_window, text="Ceo kljuc", variable=self.radio_var, value=2, command=self.on_radio_button_change)

        radio_button1.pack(pady=5)
        radio_button2.pack(pady=5)

        self.password_entry = Entry(new_window)
        self.password_entry.insert(0, "Lozinka")
        self.password_entry.pack_forget()  # Sakrivanje polja za unos lozinke dok se ne izabere druga opcija

        submit_button = Button(new_window, text="Potvrdi", command=lambda arg1=i, arg2=new_window: self.izveziKljuc(arg1, arg2))
        submit_button.pack(pady=20)

    def izveziKljuc(self, i, prozor):
        lozinka = self.password_entry.get()

        selected_option = self.radio_var.get()
        if selected_option == 1:
            public_key = self.privateRing[i]["PublicKey"]
            mejl = self.privateRing[i]["UserId"]
            file_name = self.imeKorisnika + "_" + mejl + "_public_key.pem"
            file_path = os.path.join(self.glavnaApp.key_directory, file_name)
            with open(file_path, 'wb') as f:
                f.write(public_key)
        else:
            key = self.privateRing[i]["EncPrivateKey"]
            private_key = desifruj_kljuc(key, lozinka)
            if private_key == -1:
                self.osvezi()
                Label(self.prozor, text="Pogresna lozinka", fg="red").grid(row=self.porukaGreskeRow, column=0)
            else:
                public_key = self.privateRing[i]["PublicKey"]
                mejl = self.privateRing[i]["UserId"]
                file_name = self.imeKorisnika + "_" + mejl + "_public_and_private_key.pem"
                file_path = os.path.join(self.glavnaApp.key_directory, file_name)
                with open(file_path, 'wb') as f:
                    # f.write(public_key)
                    # f.write(b'\n')
                    f.write(private_key)
        prozor.destroy()

    def uveziCeoKljucProzor(self):
        new_window = Toplevel(self.prozor)
        new_window.geometry("400x300")
        new_window.title("Uvoz kljuca")

        Label(new_window, text="Unesite mejl:").pack(pady=5)
        self.mejl_input = Entry(new_window)
        self.mejl_input.pack(pady=5)

        Label(new_window, text="Unesite lozinku:").pack(pady=5)
        self.lozinka_input = Entry(new_window)
        self.lozinka_input.pack(pady=5)

        Label(new_window, text="Unesite ime fajla sa celim kljucem").pack(pady=5)
        self.file_name_import = Entry(new_window)
        self.file_name_import.pack(pady=5)

        Button(new_window, text="Import", command=lambda arg=new_window: self.uveziCeoKljuc(arg)).pack(pady=10)

        new_window.grab_set()
        new_window.protocol("WM_DELETE_WINDOW", self.osvezi)

    def uveziCeoKljuc(self, prozor):
        file_path = os.path.join(self.glavnaApp.key_directory, self.file_name_import.get())
        mejl = self.mejl_input.get()
        lozinka = self.lozinka_input.get()
        if file_path == "":
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Morate uneti naziv fajla")
            return
        if mejl == "" or lozinka == "":
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Moraju postojati i mejl i lozinka")
            return

        result = uvezi_ceo_kljuc(file_path, lozinka)
        if result == -1:
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Fajl nije pronadjen")
            return
        if result == -2:
            prozor.destroy()
            self.ispisi_gresku_na_korisnicki_prozor("Pogresan format kljuca u fajlu")
            return

        for r in self.privateRing:
            if r["UserId"] == mejl:
                prozor.destroy()
                self.ispisi_gresku_na_korisnicki_prozor("Korisnik vec ima kljuc sa tim mejlom")
                return

        enc_privatni_kljuc, javni_kljuc, timestamp, key_id = result

        record = {
            "Timestamp": timestamp,
            "KeyId": key_id,
            "PublicKey": javni_kljuc,
            "EncPrivateKey": enc_privatni_kljuc,
            "UserId": mejl
        }

        self.privateRing.append(record)
        self.perzistencijaPodataka.save_privateRing_record_to_file(record)

        prozor.destroy()
        self.osvezi()



    #-------------------------Neke tehnicke metode----------------------------------------------------------------------

    def osvezi(self):
        for widget in self.prozor.winfo_children():
            widget.destroy()

        self.populate()

    def zatvoriProzor(self):
        self.prozor.destroy()
        self.glavniProzor.deiconify()

    def ispisi_gresku_na_korisnicki_prozor(self, tekst_greske):
        self.osvezi()
        Label(self.framePrivateRing, text=tekst_greske, fg="red").grid(row=self.porukaGreskeRow, column=3)

    def on_mousewheel(self, event):
        self.canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")

    # -------------------------Slanje i prijem----------------------------------------------------------------------

    def generisiProzorPrijem(self):
        self.prozor_prijem.prikaziProzor()

    def generisiProzorSlanje(self):
        self.prozor_slanje.prikaziProzor()



