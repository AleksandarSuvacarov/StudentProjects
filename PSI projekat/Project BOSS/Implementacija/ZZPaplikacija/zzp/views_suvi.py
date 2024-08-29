from django.shortcuts import render, redirect
from django.http import HttpRequest, HttpResponse
from django.contrib.auth import login, authenticate, logout

import re
from PIL import Image
from django.utils.datastructures import MultiValueDictKeyError

# from django.conf import settings
# from django.core.files.storage import FileSystemStorage

from .models import *

def login_req(request: HttpRequest):
    """
        Pogled za logovanje registrovanog korisnika
        @param request HttpRequest
        @return render()
    """
    if(request.user.is_authenticated): return redirect('nemate_privilegije')
    context = {
        "poruka": ""
    }

    if(request.method == "POST"):

        ime = request.POST.get("korime")
        sifra = request.POST.get("sifra")

        korisnik = authenticate(username=ime, password=sifra)


        if korisnik:

            if korisnik.idulo.naziv == 'admin':
                login(request, korisnik)
                return redirect("administrativni_meni")

            login(request, korisnik)
            return redirect("index")
        else:
            context = {
                "poruka": "Logovanje nije uspelo! Proverite podatke!"
            }

    return render(request, 'Autentifikacija/login.html', context)

def logout_req(request: HttpRequest):
    """
        Pogled za logout registrovanog korisnika
        @param request HttpRequest
        @return HttpResponse
    """
    if (not request.user.is_authenticated): return redirect('nemate_privilegije')
    logout(request)
    return redirect("index")

#Radili Aleksandar Suvačarov 2020/0355, Jovana Simić 2020/0360
def registracija_req(request: HttpRequest):
    """
        Pogled za registrovanje korisnika bez naloga
        Kreiraju se ili obican korisnik ili organizator i odmah se loguju ako je uspesno
        @param request HttpRequest
        @return HttpResponse
    """
    if (request.user.is_authenticated): return redirect('nemate_privilegije')
    context = {
        "poruka": ""
    }

    if(request.method == "POST"):
        ime = request.POST.get("korime")
        sifra = request.POST.get("sifra1")
        sifra2 = request.POST.get("sifra2")
        vrstaKorisnika = request.POST.get("vrstaKorisnika")
        # print(vrstaKorisnika)
        if (ime == "" or sifra == "" or sifra2 == "" or vrstaKorisnika == None):
            return render(request, 'Autentifikacija/registracija.html', {"poruka": "Popunite sva polja"})

        if (sifra != sifra2):
            return render(request, 'Autentifikacija/registracija.html', {"poruka": "Sifra se ne podudara"})

        provera = Korisnik.objects.filter(username__exact=ime).exists()

        if(provera):
            context = {
                "poruka": "Korisnik sa ovakvim korisnickim imenom vec postoji"
            }
            return render(request, "Autentifikacija/registracija.html", context)

        # pravimo korisnika
        if vrstaKorisnika == "korisnikNov":
            user = Korisnik(username=ime, idulo=Uloga.objects.get(naziv="korisnik"))
            user.set_password(sifra)
            user.save()
            login(request, user)
            return redirect("index")
        # pravimo organizatora
        elif vrstaKorisnika == "organizatorNov":

            imeorg = request.POST.get("imeorg")
            opis = request.POST.get("opisorg")
            teme = request.POST.get("temeorg")
            if (imeorg == "" or opis == "" or teme == ""):
                return render(request, 'Autentifikacija/registracija.html', {"poruka": "Popunite sva polja"})

            try:
                slika = request.FILES['slikaorg']
                # slika nepronadjena
            except MultiValueDictKeyError:
                return render(request, 'Autentifikacija/registracija.html', {"poruka": "Slika nije dodata"})

            # provera dimenzija slike
            try:
                img = Image.open(slika)
            except IOError:
                return render(request, 'Autentifikacija/registracija.html', {"poruka": "Fajl mora da bude slika"})

            sirina, visina = img.size
            if sirina != visina:
                return render(request, 'Autentifikacija/registracija.html', {"poruka": "Slika mora da bude u formatu 1:1"})
            user = Korisnik(username=ime, idulo=Uloga.objects.get(naziv="organizator"))
            user.set_password(sifra)
            user.save()
            # login(request, user)

            organizator = Organizator(opis=opis, naziv=imeorg, teme=teme, idorg=user, slika=slika)
            organizator.save()
            login(request, user)
            return redirect("index")



    return render(request, "Autentifikacija/registracija.html", context)


def kreiraj_objavu_req(request: HttpRequest):
    """
        Pogled za kreiranje objave registrovanog organizatora
        Stavljaju se potrebni podaci za kviz u bazu
        @param request Httprequest
        @return HttpResponse
    """

    teme = Tema.objects.all()

    user = request.user

    if (not user.is_authenticated): return redirect('nemate_privilegije')
    k = Korisnik.objects.get(id=user.id)
    if(k.idulo != Uloga.objects.get(naziv="organizator") or k.odobren == 0):
        return redirect('nemate_privilegije')

    context = {
        "teme": teme,
        "poruka": "Sva polja su obavezna"
    }

    if(request.method == "POST"):
        context["poruka"] = "Stigao zahtev"
        # return render(request, "Kreiranje objave/kreiranje_objave.html", context)

        slika = request.FILES['slika_kviza']
        # provera dimenzija slika
        # img = Image.open(slika)
        # sirina, visina = img.size
        # if sirina != visina:
        #     return render(request, "Autentifikacija/registracija.html", {"poruka": "Slika mora da bude u formatu 1:1", "ind": "1"})


        naziv = request.POST['naziv_kviza']
        opis = request.POST['opis_kviza']
        adresa = request.POST['adresa_kviza']
        kotizacija = int(request.POST['kotizacija_kviza'])
        kapacitet = int(request.POST['kapacitet_kviza'])
        datum_vreme = request.POST['datum_vreme_kviza']     #TODO nema provere da li e pre danasnjeg
        tema = Tema.objects.get(naziv__exact=request.POST['tema_kviza'])
        organizator = Organizator.objects.get(idorg=user)


        kviz = Kviz(naslov=naziv, opis=opis, datumvreme=datum_vreme, adresa=adresa, kotizacija=kotizacija,
                    kapacitet=kapacitet, slika=slika, idorg=organizator, idtem=tema)

        kviz.save()

        organizator.predstojecikviz = kviz
        organizator.save()

        br_kvizova = int(organizator.brkvizova)
        br_kvizova += 1
        organizator.brkvizova = br_kvizova
        organizator.save()

        return redirect("kvizovi")

    return render(request, "Kreiranje objave/kreiranje_objave.html", context)

def prijavi_ekipu_req(request:HttpRequest, kviz_id):
    """
        Pogled za prijavu ekipe na kviz
        Proverava se da li su podaci validni i onda se ili vraca poruka o gresci
        ili stavljaju podaci u bazu i prijava je zabelezena

        @param request Httprequest
        @param kviz_id int

        @return HttpResponse
    """

    kvizovi = Kviz.objects.all()

    context = {
        "kvizovi": kvizovi,
        "poruka": ""
    }

    user = request.user

    if (not user.is_authenticated): return redirect('nemate_privilegije')
    k = Korisnik.objects.get(id=user.id)
    if (k.idulo != Uloga.objects.get(naziv="korisnik") and k.idulo != Uloga.objects.get(naziv="moderator")):
        return redirect('nemate_privilegije')

    try:
        kviz = Kviz.objects.get(idkviz=kviz_id)
    except Kviz.DoesNotExist:
        return redirect('nemate_privilegije')

    naziv_ekipe = request.POST["naziv_ekipe"]
    pattern = r'\b\w*[a-zA-Z0-9]\w*\b'

    br_clanova = str(request.POST["br_clanova"])

    if re.search(pattern, naziv_ekipe) is None:
        context["poruka"] = "Morate da unesete ime ekipe"
        return render(request, "kvizovi.html", context)

    if not br_clanova.isdigit():
        context["poruka"] = "Morate da unesete ceo broj za broj clanova ekipe"
        return render(request, "kvizovi.html", context)

    br_clanova = int(br_clanova)

    if(br_clanova < 2 or br_clanova > 6):
        context["poruka"] = "Morate da unesete broj izmedju 2 i 6"
        return render(request, "kvizovi.html", context)


    if kviz.kapacitet - kviz.zauzetost < br_clanova:
        context["poruka"] = "Nema dovljno mesta"
        return render(request, "kvizovi.html", context)

    kviz.zauzetost = kviz.zauzetost + br_clanova
    kviz.save()

    prijava = Prijava(idkviz=kviz, idkor=user, brclanova=br_clanova, nazivtima=naziv_ekipe)

    prijava.save()
    context["poruka"] = f"Ekipa {naziv_ekipe} se uspešno prijavila za {kviz.naslov}!"

    return render(request, "kvizovi.html", context)

def odobravanje_zahteva_req(request:HttpRequest):
    """
        Pogled za odobravanje zahteva organizatora za kreiranje
        Moderator odobrava zahtev i onda se ovde samo ta promena zabelezi
        @param request Httprequest
        @return HttpResponse
    """
    user = request.user

    if (not user.is_authenticated): return redirect('nemate_privilegije')
    k = Korisnik.objects.get(id=user.id)
    if (k.idulo != Uloga.objects.get(naziv="moderator")):
        return redirect('nemate_privilegije')

    if request.method == "POST":
        org_id = request.POST["org_id_za_odobrenje"]
        org = Organizator.objects.get(idorg=org_id)
        org.odobren = 1
        org.save()
        korisnik = Korisnik.objects.get(id=org_id)
        korisnik.odobren = 1
        korisnik.save()

    organizatori = Organizator.objects.filter(odobren=0)

    context = {
        "organizatori": organizatori
    }



    return render(request, "Odobravanje zahteva/odobravanje_zahteva.html", context)