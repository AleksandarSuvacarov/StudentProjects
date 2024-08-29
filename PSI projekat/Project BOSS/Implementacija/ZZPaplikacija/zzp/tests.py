import unittest

import datetime
import os
from io import BytesIO

from PIL import Image
from django.core.files.uploadedfile import SimpleUploadedFile
from django.test import TestCase

from django.urls import reverse
from django.contrib.auth import login
from django.template import Template, Context

from .models import *

def inicijalizacija():
    u1 = Uloga(naziv='admin')
    u1.save()
    u2 = Uloga(naziv='moderator')
    u2.save()
    u3 = Uloga(naziv='organizator')
    u3.save()
    u4 = Uloga(naziv='korisnik')
    u4.save()

    pera = Korisnik(username="pera")
    pera.set_password("pera123")
    pera.idulo = u4
    pera.save()

    mika = Korisnik(username="mika")
    mika.set_password("mika123")
    mika.idulo = u4
    mika.save()

    zika = Korisnik(username="zika")
    zika.set_password("zika123")
    zika.idulo = u4
    zika.save()

    org = Korisnik(username="org")
    org.set_password("org123")
    org.idulo = u3
    org.odobren = 1
    org.save()
    organizacija = Organizator(idorg=org, naziv="Org", opis="opis", teme="teme", odobren=1, slika="imgs/Dobra_slika_organizacija.jpg")
    organizacija.save()

    org2 = Korisnik(username="org2")
    org2.set_password("org1234")
    org2.idulo = u3
    org2.odobren = 0
    org2.save()
    organizacija2 = Organizator(idorg=org2, naziv="Org2", opis="opis2", teme="teme2", odobren=0,
                               slika="imgs/Dobra_slika_organizacija.jpg")
    organizacija2.save()

    mod = Korisnik(username="mod")
    mod.set_password("mod123")
    mod.idulo = u2
    mod.save()

    admin = Korisnik(username="admin")
    admin.set_password("admin")
    admin.idulo = u1
    admin.save()

    admin2 = Korisnik(username="admin2")
    admin2.set_password("admin2")
    admin2.idulo = u1
    admin2.save()

    tema = Tema(naziv="tema")
    tema.save()

    for i in range(4):
        kviz = Kviz(naslov="Naslov" + str(i), datumvreme=datetime.datetime.now() + datetime.timedelta(hours=24), opis="Opis", adresa="Adresa",
                    kotizacija=50, kapacitet=50, zauzetost=30, idtem=tema, idorg=organizacija, slika="imgs/Dobra_slika_organizacija.jpg")
        kviz.save()

    for i in range(3):
        prijava = Prijava(idkviz=Kviz.objects.get(naslov="Naslov1"), idkor_id=Korisnik.objects.get(username="pera").id, nazivtima="JedvaSmoSeSkupili", brclanova=3)
        prijava.save()

    rec = Recenzija(ocena=3, datumvreme=datetime.datetime.now(), idorgrec=organizacija, idkorrec=pera, komentar="Za moderatora")
    rec.save()


class Testovi(TestCase):
    #========================================= Aleksandar Suvačarov 2020/0355 ==================================================================
    def setUp(self):
        inicijalizacija()           #ovo treba
        self.username = 'pera'
        self.password = 'pera123'

        # Simulirajte prijavu korisnika
        self.client.login(username=self.username, password=self.password)

    def test_ostavljanje_recenzija(self):
        kororg = Korisnik.objects.get(username="org")
        org = Organizator.objects.get(idorg=kororg)

        # Dolazim na stranu organizacije koja ne postoji
        url = reverse('org_strana', kwargs={'org_id': 9999})
        response = self.client.get(url)
        self.assertRedirects(response, expected_url=reverse("nemate_privilegije"), status_code=302)

        #Neulogovan dolazim do org koja nije odobrena
        org.odobren = 0
        org.save()
        self.client.logout()
        url = reverse('org_strana', kwargs={'org_id': kororg.id})
        response = self.client.get(url)
        self.assertRedirects(response, expected_url=reverse("nemate_privilegije"), status_code=302)

        #Ulogovan, ali ne kao moderator, dolazim do org koja nije odobrena
        self.client.login(username=self.username, password=self.password)
        url = reverse('org_strana', kwargs={'org_id': kororg.id})
        response = self.client.get(url)
        self.assertRedirects(response, expected_url=reverse("nemate_privilegije"), status_code=302)

        #Odobravam organizaciju ponovo
        org.odobren = 1
        org.save()
        #Dolazim na zeljenu stranu organizacije "org"
        url = reverse('org_strana', kwargs={'org_id': kororg.id})
        response = self.client.get(url)

        #Proveravam da li sam stigao na tu stranu
        self.assertContains(response, org.naziv)

        response = self.client.post(url, data={"recenzijica": "Komentar", "nevidljiv": "3"})

        rec = Recenzija.objects.get(komentar__contains="Komentar")
        self.assertIsNotNone(rec)

    def test_revizija_recenzija(self):
        #Logout korisnika "pera"
        response = self.client.post(reverse('logout'))
        self.client.login(username="mod", password="mod123")

        #Login moderatora
        response = self.client.post(reverse("index"))
        self.assertContains(response, "Zahtevi")

        #Provera da li sam na strani moderatora
        kororg = Korisnik.objects.get(username="org")
        org = Organizator.objects.get(idorg=kororg)

        # Dolazim na zeljenu stranu organizacije "org"
        url = reverse('org_strana', kwargs={'org_id': kororg.id})
        response = self.client.get(url)

        # Proveravam da li sam stigao na tu stranu i ostavljam recenziju
        self.assertContains(response, org.naziv)

        rec = Recenzija.objects.all()
        rec_id = rec[0].idrec
        response = self.client.post(url, data={"rec_za_brisanje": rec_id})
        self.assertEqual(response.status_code, 200)     #provera da li je vracena strana

        try:
            rec = Recenzija.objects.get(idrec=rec_id)
        except Recenzija.DoesNotExist:
            rec = None

        self.assertIsNone(rec)

    def test_registracija_organizatora(self):
        #Dolazim na stranu registracija
        url = reverse('registracija')

        # Jos uvek sam ulogovan pa se javlja greska sa permisijama
        response = self.client.get(url)
        # Redirect na obavestenje o permisijama
        self.assertRedirects(response, expected_url=reverse("nemate_privilegije"), status_code=302)
        self.client.post(reverse('logout'))


        #Nisu popunjena sva polja
        response = self.client.post(url, data={'korime': "org_proba",
                                              "sifra1": "sifra",
                                              "sifra2": "sifra",
                                              "vrstaKorisnika": "organizatorNov",
                                              "imeorg": "ime",
                                              "opisorg": "opis",
                                              "slikaorg": "imgs/Dobra_slika_organizacija.jpg",
                                              "temeorg": ""
                                               })
        #Poruka da se popune sva polja se pojavila
        self.assertContains(response, "Popunite sva polja")

        #Nije dodata slika
        response = self.client.post(url, data={'korime': "org_proba",
                                               "sifra1": "sifra",
                                               "sifra2": "sifra",
                                               "vrstaKorisnika": "organizatorNov",
                                               "imeorg": "ime",
                                               "opisorg": "opis",
                                               "temeorg": "tema",
                                               "slikaorg": ""
                                               })
        # Poruka da nije stavljena slika se pojavila
        self.assertContains(response, "Slika nije dodata")

        file_content = b'This is a sample text file.'
        file = SimpleUploadedFile('my_file.txt', file_content, content_type='text/plain')

        # Nije poslata slika nego neki drugi format
        response = self.client.post(url, data={'korime': "org_proba",
                                               "sifra1": "sifra",
                                               "sifra2": "sifra",
                                               "vrstaKorisnika": "organizatorNov",
                                               "imeorg": "ime",
                                               "opisorg": "opis",
                                               "temeorg": "tema",
                                               "slikaorg": file
                                               })


        # Poruka da nije poslata slika nego nesto drugo se pojavila
        self.assertContains(response, "Fajl mora da bude slika")

        # Relativna putanja do slike
        image_path = 'images\imgs\Losa_slika_organizacija.jpg'
        image = Image.open(image_path)

        # Pretvorite sliku u binarni oblik
        image_io = BytesIO()
        image.save(image_io, format='JPEG')
        image_io.seek(0)

        # Nije poslata slika dobrih dimenzija
        response = self.client.post(url, data={'korime': "org_proba",
                                               "sifra1": "sifra",
                                               "sifra2": "sifra",
                                               "vrstaKorisnika": "organizatorNov",
                                               "imeorg": "ime",
                                               "opisorg": "opis",
                                               "temeorg": "tema",
                                               "slikaorg": image_io
                                               })
        #print(response.content.decode())  # Printovanje responsa u terminal
        # Poruka da dimenzije nisu dobre se pojavila
        self.assertContains(response, "Slika mora da bude u formatu 1:1")

        #Provera da nije dodato nesto u bazu tokom ovih neuspelih pokusaja
        try:
            org = Korisnik.objects.get(username="org_proba")
        except Korisnik.DoesNotExist:
            org = None

        self.assertIsNone(org)


        #Uspeno dodavanje organizatora
        image_path = 'images\imgs\Dobra_slika_organizacija.jpg'
        image = Image.open(image_path)
        image_io = BytesIO()
        image.save(image_io, format='JPEG')
        image_io.seek(0)

        response = self.client.post(url, data={'korime': "org_proba",
                                               "sifra1": "sifra",
                                               "sifra2": "sifra",
                                               "vrstaKorisnika": "organizatorNov",
                                               "imeorg": "ime",
                                               "opisorg": "opis",
                                               "temeorg": "tema",
                                               "slikaorg": image_io
                                               }, follow=True)          #to kada ide redirect

        org = Korisnik.objects.get(username="org_proba")


        self.assertIsNotNone(org)
        self.assertRedirects(response, expected_url=reverse('index'), status_code=302)
        self.assertContains(response, 'org_proba')

    #========================================= Jovana Simić 2020/0360 ==================================================================
    def test_kreiranje_objave(self):

        #NEMA PRIVILEGIJE

        url = reverse("kreiranje_objave")
        response = self.client.get(url, follow=True)
        # Proveravam da li sam stigao na tu stranu
        self.assertRedirects(response, expected_url=reverse('nemate_privilegije'), status_code=302)
        self.assertContains(response, "Nemate privijelige")


        #logovanje kao organizator
        response = self.client.post(reverse('logout'))
        self.client.login(username="org", password="org123")

        url = reverse("kreiranje_objave")
        response = self.client.get(url)
        # Proveravam da li sam stigao na tu stranu
        self.assertContains(response, "Kreiraj objavu")

        #pogresno popunjeno polje kapacitet
        image_path = 'images\imgs\Dobra_slika_organizacija.jpg'
        image = Image.open(image_path)
        image_io = BytesIO()
        image.save(image_io, format='JPEG')
        image_io.seek(0)

        response = self.client.post(url, data={
            "naziv_kviza": "kvizProba",
            "opis_kviza": "opis",
            "adresa_kviza": "adresa",
            "kotizacija_kviza": 120,
            "kapacitet_kviza": 20,
            "datum_vreme_kviza": datetime.datetime.now() + datetime.timedelta(hours=24),
            "tema_kviza": Tema.objects.all()[0].naziv,
            "slika_kviza": image_io
        }, follow=True)
        self.assertRedirects(response, expected_url=reverse('kvizovi'), status_code=302)
        self.assertContains(response, 'kvizProba')
        # self.assertContains(response, "Pogresno popunjena forma. Polje kapacitet moze sadrzati samo brojeve")

        # self.assertContains(response, "Pogresno popunjena forma. Morate popuniti sva polja")


    def test_administriranje(self):

        #NEMA PRIVILEGIJE
        url = reverse("administrativni_meni")
        response = self.client.get(url, follow=True)
        # Proveravam da li sam stigao na tu stranu
        self.assertRedirects(response, expected_url=reverse('nemate_privilegije'), status_code=302)
        self.assertContains(response, "Nemate privijelige")

        #logovanje
        response = self.client.post(reverse('logout'))
        self.client.login(username="admin", password="admin")

        url = reverse("administrativni_meni")
        response = self.client.get(url)
        # Proveravam da li sam stigao na tu stranu
        self.assertContains(response, "admin")

        # nalog ne postoji
        response = self.client.post(url, data={
            "dodaj_moderatora": "ne_postoji"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik sa ovim korisnickim imenom ne postoji"
        self.assertEqual(alert_message, expected_message)

        # superusera
        response = self.client.post(url, data={
            "dodaj_moderatora": "admin"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Superuser admin ne moze da se modifikuje!"
        self.assertEqual(alert_message, expected_message)

        #DODAVANJE MODERATORA

        #uspesno dodavanje moderatora
        response = self.client.post(url, data={
            "dodaj_moderatora" : "pera"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Uspesno dodeljena uloga moderatora korisniku pera."
        self.assertEqual(alert_message, expected_message)

        #neuspesno dodavanje moderatora
        response = self.client.post(url, data={
            "dodaj_moderatora": "mod"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik mod vec ima moderatorsku ulogu."
        self.assertEqual(alert_message, expected_message)

        # neuspesno dodavanje moderatora
        response = self.client.post(url, data={
            "dodaj_moderatora": "admin2"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik admin2 je administrator."
        self.assertEqual(alert_message, expected_message)

        # neuspesno dodavanje moderatora
        response = self.client.post(url, data={
            "dodaj_moderatora": "org"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik org je organizator i ne moze biti moderator."
        self.assertEqual(alert_message, expected_message)



        #BRISANJE MODERATORA

        #uspesno brisanje moderatora
        response = self.client.post(url, data={
            "obrisi_moderatora": "mod"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Uspesno je uklonjena uloga korisniku mod."
        self.assertEqual(alert_message, expected_message)

        #neuspesno brisanje moderatora
        response = self.client.post(url, data={
            "obrisi_moderatora": "org"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik org nema ulogu moderatora."
        self.assertEqual(alert_message, expected_message)



        #DODAVANJE ADMINISTRATORA
        #neuspesno dodavanje administratora
        response = self.client.post(url, data={
            "dodaj_administratora": "org"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik org je organizator i ne moze biti administrator."
        self.assertEqual(alert_message, expected_message)

        # neuspesno dodavanje administratora
        response = self.client.post(url, data={
            "dodaj_administratora": "admin2"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Korisnik admin2 vec ima administratorsku ulogu."
        self.assertEqual(alert_message, expected_message)

        # uspesno dodavanje administratora
        response = self.client.post(url, data={
            "dodaj_administratora": "pera"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Uspesno dodeljena uloga administratora korisniku pera."
        self.assertEqual(alert_message, expected_message)


        #BRISANJE NALOGA
        id = Korisnik.objects.get(username="zika")
        id_kor = id.id
        prijava = Prijava(idkviz=Kviz.objects.all()[0],idkor=id, nazivtima="primer", brclanova=3)
        print(prijava.brclanova)
        prijava.save()
        response = self.client.post(url, data={
            "obrisi_nalog": "zika"
        })
        alert_message = response.context.get('poruka', '')
        expected_message = "Nalog sa korisnickim imenom zika je uspesno obrisan."
        self.assertEqual(alert_message, expected_message)
        try:
            prijava = Prijava.objects.get(idkor=id_kor)

        except Prijava.DoesNotExist:
            prijava = None
        self.assertIsNone(prijava)

    #========================================= Andrija Ognjanović 2020/0261 ==================================================================

    def test_login(self):
        self.client.logout()

        # Uspesno logovan korisnik
        url = reverse('login')

        response = self.client.post(url, data={'korime': "pera",
                                               'sifra': "pera123"
                                               },
                                    follow=True)

        self.assertRedirects(response, expected_url=reverse('index'), status_code=302)
        self.assertContains(response, 'pera')

        # Uspesno logovanje administrator (posto ima posebnu stranu za sebe)
        self.client.logout()

        url = reverse('login')

        response = self.client.post(url, data={'korime': "admin",
                                               'sifra': "admin"
                                               },
                                    follow=True)


        self.assertRedirects(response, expected_url=reverse('administrativni_meni'), status_code=302)
        self.assertContains(response, 'admin')

        #Neuspesno logovanje korisnika, missing user and/or pass
        self.client.logout()

        response = self.client.post(url, data={'korime': "",
                                               'sifra': ""
                                               },
                                    follow=True)

        #Poruka da nisu lepo popunjeni podaci
        self.assertContains(response, 'Logovanje nije uspelo! Proverite podatke!')

    def test_pregledEkipa(self):



        kviz = Kviz.objects.get(naslov="Naslov1")

        #Greska, obican korisnik pokusava da pregleda ekipe
        # Dolazim na stranu zeljenog kviza
        url = reverse('pregled_ekipa', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.get(url)

        self.assertRedirects(response,expected_url=reverse("nemate_privilegije"), status_code=302)

        #Greska, pokusavaju da se pregledaju ekipe od nepostojeceg kviza
        self.client.logout()
        self.client.login(username="org", password="org123")

        url = reverse('pregled_ekipa', kwargs={'kviz_id': 5})
        response = self.client.get(url)

        self.assertRedirects(response,expected_url=reverse("nemate_privilegije"), status_code=302)


        #Uspesan pregled ekipa od strane organizatora
        self.client.logout()
        self.client.login(username="org", password="org123")

        url = reverse('pregled_ekipa', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.get(url)

        self.assertContains(response, kviz.naslov)

    #Admin bi nasilno da pogleda kvizove, iako nema tu privilegiju
    def test_adminHoceDaGledaKvizove(self):
        self.client.logout()
        self.client.login(username="admin", password="admin")

        url = reverse('kvizovi')
        response = self.client.get(url)

        self.assertRedirects(response, expected_url=reverse("nemate_privilegije"), status_code=302)

    def test_adminHoceDaGledaIndex(self):
        self.client.logout()
        self.client.login(username="admin", password="admin")

        url = reverse('index')
        response = self.client.get(url)

        self.assertRedirects(response, expected_url=reverse("administrativni_meni"), status_code=302)

    def test_adminHoceDaGledaOrganizacije(self):
        self.client.logout()
        self.client.login(username="admin", password="admin")

        url = reverse('organizacije')
        response = self.client.get(url)

        self.assertRedirects(response, expected_url=reverse("nemate_privilegije"), status_code=302)

    def test_pregledOrganizacija(self):

        url = reverse('organizacije')
        response = self.client.get(url)

        self.assertEqual(response.status_code, 200)
        self.assertContains(response, "Organizacije")


    #========================================= Nikola Babić 2020/0363 ==================================================================
    def test_registracija(self):

        """ Testira registracionu formu za novog korisinka """

        # Stranica za registraciju
        url = reverse('registracija')

        response = self.client.get(url)
        # Preusmerava ga na stranicu sa obavestenjem o permisijama
        self.assertRedirects(response, expected_url=reverse('nemate_privilegije'), status_code=302)

        #Izloguje ulogovanog korisnika
        self.client.post(reverse('logout'))

        response = self.client.get(url)
        self.assertEqual(response.status_code, 200)

        # Jedno od polja nije popunjeno ili nije izabran tip korisinka koji se registruje
        response = self.client.post(url, data={'korime': 'korisnik',
                                               'sifra1': '',
                                               'sifra2': 'sifra123',
                                               'vrstaKorisnika': 'korisnikNov'})

        # Prikaz poruke da je neophodno popuniti sva polja
        self.assertContains(response, 'Popunite sva polja')

        # Sifra i ponovljena sifra se ne poklapaju
        response = self.client.post(url, data={'korime': 'korisnik',
                                               'sifra1': 'sifra123',
                                               'sifra2': 'sifra13',
                                               'vrstaKorisnika': 'korisnikNov'})

        # Prikaz poruke da se sifre ne podudaraju
        self.assertContains(response, 'Sifra se ne podudara')

        # Pokusaj pravljenja naloga sa korisnickim imenom koje je zauzeto
        response = self.client.post(url, data={'korime': 'pera',
                                               'sifra1': 'sifra123',
                                               'sifra2': 'sifra123',
                                               'vrstaKorisnika': 'korisnikNov'})

        # Prikaz poruke da je korisnicko ime zauzeto
        self.assertContains(response, 'Korisnik sa ovakvim korisnickim imenom vec postoji')

        try:
            korisnik = Korisnik.objects.get(username='korisnik')
        except Korisnik.DoesNotExist:
            korisnik = None

        # Provera da li je neki od nespelih pokusaja dodao nesto u bazu
        self.assertIsNone(korisnik)

        # Ispravno popunjena sva polja i uspesna registracija korisnika na sistem
        response = self.client.post(url, data={'korime': 'korisnik',
                                               'sifra1': 'sifra123',
                                               'sifra2': 'sifra123',
                                               'vrstaKorisnika': 'korisnikNov'},
                                    follow=True)

        try:
            korisnik = Korisnik.objects.get(username='korisnik')
        except Korisnik.DoesNotExist:
            korisnik = None

        # Provera da li je sacuvan uspesno u bazu
        self.assertIsNotNone(korisnik)
        # Provera da li je uspesno prikazana index stranica sa ulogovanim novokreiranim korisnikom
        self.assertRedirects(response, expected_url=reverse('index'), status_code=302)
        self.assertContains(response, 'korisnik')

    def test_prijavi_ekipu(self):

        """ Testira funkcionalnost prijave ekipe na kviz za ulogovanog korisnika """

        kviz = Kviz.objects.get(naslov='Naslov1')

        self.client.post(reverse('logout'))

        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.get(url)

        # neregistrovan korisnik nema pristupa registraciji na kviz kroz url
        self.assertRedirects(response, expected_url=reverse('nemate_privilegije'), status_code=302)

        # admin ili organizator nema prava pristupa prijavi na kviz
        self.client.login(username='admin', password='admin')
        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.get(url)
        self.assertRedirects(response, expected_url=reverse('nemate_privilegije'), status_code=302)

        # Prijava na kviz koji ne postoji
        self.client.post(reverse('logout'))
        self.client.login(username='mod', password='mod123')

        url = reverse('prijavi_ekipu', kwargs={'kviz_id': 200})
        response = self.client.get(url)
        self.assertRedirects(response, expected_url=reverse('nemate_privilegije') ,status_code=302)

        # Prijava bez naziva ekipe
        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.post(url, data={'naziv_ekipe': 'ekipa',
                                    'br_clanova': 7})
        self.assertContains(response, 'Morate da unesete broj izmedju 2 i 6')

        # Prijava bez naziva ekipe
        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.post(url, data={'naziv_ekipe': '',
                                               'br_clanova': 4})
        self.assertContains(response, 'Morate da unesete ime ekipe')

        # Prijava bez naziva ekipe
        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.post(url, data={'naziv_ekipe': 'ekipa',
                                               'br_clanova': 'asda'})
        self.assertContains(response, 'Morate da unesete ceo broj za broj clanova ekipe')

        kviz.zauzetost = kviz.kapacitet
        kviz.save()

        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.post(url, data={'naziv_ekipe': 'ekipa',
                                               'br_clanova': 4})
        self.assertContains(response, 'Nema dovljno mesta')

        kviz.zauzetost = 0
        kviz.save()

        url = reverse('prijavi_ekipu', kwargs={'kviz_id': kviz.idkviz})
        response = self.client.post(url, data={'naziv_ekipe': 'ekipa',
                                               'br_clanova': 4})
        self.assertContains(response, 'Ekipa ekipa se uspešno prijavila za Naslov1')



    def test_odobravanje_zahteva(self):

        """ Testira funkcionalnost odbravanja organizacije """

        url = reverse('odobravanje_zahteva')
        response = self.client.get(url)

        self.assertRedirects(response, expected_url=reverse('nemate_privilegije') ,status_code=302)

        self.client.logout()

        url = reverse('odobravanje_zahteva')
        response = self.client.get(url)

        self.assertRedirects(response, expected_url=reverse('nemate_privilegije'), status_code=302)

        self.client.login(username="mod", password='mod123')

        org = Korisnik.objects.get(username='org2')

        response = self.client.post(url, data={
            'org_id_za_odobrenje': org.id
        }, follow=True)

        self.assertEqual(response.status_code, 200)

        print(response.content.decode())




