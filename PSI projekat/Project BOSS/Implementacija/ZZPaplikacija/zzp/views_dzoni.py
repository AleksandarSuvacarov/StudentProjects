# Nikola Babić 2020/0363

from django.shortcuts import render, redirect
from django.http import HttpRequest
from datetime import datetime

from .models import *


def admin_meni_req(request: HttpRequest):
    """
        Funkcija koja obradjuje zahtev za prikazom stranice administrativnog menija i implementira funkcionalnosti formi sa nje

        Args:
            request (HttpRequest): zahtev za generisanje stranice

        Returns:
            HttpResponse: generise stranicu administrativnog menija u zavisnosti od tipa metode kojim je generisanje stranice zahtevano POST ili GET
    """

    user = request.user

    if not user.is_authenticated: return redirect('nemate_privilegije')
    korisnik = Korisnik.objects.get(id=user.id)
    if korisnik.idulo != Uloga.objects.get(naziv="admin"):
        return redirect('nemate_privilegije')

    context = {
        'poruka': ''
    }

    if request.method == "POST":
        # Ograniceno je da samo jedna stvar prosedjena POST zahtevom
        forma, username = list(request.POST.items())[-1]
        korisnik = None
        try:
            korisnik = Korisnik.objects.get(username=username)
        except Korisnik.DoesNotExist:
            context = {
                'poruka': "Korisnik sa ovim korisnickim imenom ne postoji"
            }

        if (username == "admin"):
            context['poruka'] = "Superuser admin ne moze da se modifikuje!"
            return render(request, 'Administracija/administrativni_meni.html', context)

        if context['poruka'] == '':
            poruka = ''

            if forma == 'dodaj_moderatora':
                if korisnik.idulo.naziv == 'moderator':
                    poruka = f'Korisnik {username} vec ima moderatorsku ulogu.'
                elif korisnik.idulo.naziv == 'admin':
                    poruka = f'Korisnik {username} je administrator.'
                elif korisnik.idulo.naziv == 'organizator':
                    poruka = f'Korisnik {username} je organizator i ne moze biti moderator.'
                else:
                    korisnik.idulo = Uloga.objects.get(naziv__exact='moderator')
                    poruka = f'Uspesno dodeljena uloga moderatora korisniku {username}.'
                    korisnik.save()

            elif forma == 'obrisi_moderatora':
                if korisnik.idulo.naziv != 'moderator':
                    poruka = f'Korisnik {username} nema ulogu moderatora.'
                else:
                    korisnik.idulo = Uloga.objects.get(naziv__exact='korisnik')
                    poruka = f'Uspesno je uklonjena uloga korisniku {username}.'
                    korisnik.save()

            elif forma == 'dodaj_administratora':
                if korisnik.idulo.naziv == 'admin':
                    poruka = f'Korisnik {username} vec ima administratorsku ulogu.'
                elif korisnik.idulo.naziv == 'organizator':
                    poruka = f'Korisnik {username} je organizator i ne moze biti administrator.'
                else:
                    korisnik.idulo = Uloga.objects.get(naziv__exact='admin')
                    poruka = f'Uspesno dodeljena uloga administratora korisniku {username}.'
                    korisnik.save()

            elif forma == 'obrisi_nalog':

                prijave = Prijava.objects.filter(idkor=korisnik.id)

                for p in prijave:
                    kviz = p.idkviz
                    kviz.zauzetost -= p.brclanova
                    kviz.save()

                korisnik.delete()

                poruka = f'Nalog sa korisnickim imenom {username} je uspesno obrisan.'

            else:
                #   Poslat je POST bez  da li je moguce?
                pass

            context['poruka'] = poruka

    return render(request, 'Administracija/administrativni_meni.html', context)

def pregled_ekipa_req(request: HttpRequest, kviz_id):
    """
            Funkcija koja obradjuje zahtev za prikazom stranice sa kvizovima iz perspektive organizatora koji trazi pregled prijavljenih ekipa na njegov kviz

            Args:
                request (HttpRequest): zahtev za generisanje stranice
                kviz_id (int): prosledjuje se kroz url i sluzi za dohavtanje kviza za koji je zahtevan prikaz

            Returns:
                HttpResponse: generise stranicu kvizova sa popup prozorom koji prikazuje prijavljene ekipe tog organizatora
        """

    user = request.user

    if not user.is_authenticated: return redirect('nemate_privilegije')
    korisnik = Korisnik.objects.get(id=user.id)
    try:
        kviz = Kviz.objects.get(idkviz=kviz_id)
    except Kviz.DoesNotExist:
        return redirect('nemate_privilegije')

    if korisnik.idulo != Uloga.objects.get(naziv="organizator") or kviz.idorg.idorg_id != korisnik.id:
        return redirect('nemate_privilegije')

    kvizovi = Kviz.objects.all()
    ekipe = []
    popunjenost = 0

    prijave = Prijava.objects.filter(idkviz__exact=kviz_id)
    for prijava in prijave:
        ekipe.append((prijava.nazivtima, prijava.brclanova))
        popunjenost += prijava.brclanova

    context = {
        'kvizovi': kvizovi,
        'ekipe': ekipe,
        'popunjenost': popunjenost,
        'naziv_kviza': Kviz.objects.get(idkviz=kviz_id).naslov,
        'kapacitet_kviza': Kviz.objects.get(idkviz=kviz_id).kapacitet,
    }

    return render(request, 'kvizovi.html', context)


