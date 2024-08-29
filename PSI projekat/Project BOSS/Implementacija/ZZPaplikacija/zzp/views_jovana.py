from django.shortcuts import render, redirect
from django.http import HttpRequest
from .models import *
from .views_civotamarev import recenzija_req
from datetime import datetime
from django.db.models import F, Min

def kvizovi_req(request: HttpRequest):
    """
        Funkcija koja obradjuje zahtev za prikazom stranice kvizova

        Args:
            request (HttpRequest): zahtev za generisanje stranice

        Returns:
            HttpResponse: generise stranicu kvizova
    """

    user = request.user

    if user.is_authenticated:
        korisnik = Korisnik.objects.get(id=user.id)
        if korisnik.idulo == Uloga.objects.get(naziv="admin"):
            return redirect('nemate_privilegije')

    kvizovi = Kviz.objects.filter(datumvreme__gt=datetime.now()).order_by('-idkviz')

    contex = {
        "kvizovi": kvizovi,
        "poruka": ""
    }

    return render(request, "kvizovi.html", contex)

def organizatori_req(request: HttpRequest):
    """
        Funkcija koja obradjuje zahtev za prikazom stranice organizatora

        Args:
            request (HttpRequest): zahtev za generisanje stranice

        Returns:
            HttpResponse: generise stranicu organizatora
    """

    user = request.user

    if user.is_authenticated:
        korisnik = Korisnik.objects.get(id=user.id)
        if korisnik.idulo == Uloga.objects.get(naziv="admin"):
            return redirect('nemate_privilegije')

    organizatori = Organizator.objects.all()
    # kvizovi = Kviz.objects.filter(datumvreme__gt=datetime.now()).order_by('datumvreme')
    #
    # organizacije = Organizator.objects.values('idorg', 'naziv').annotate(
    #     najskoriji_kviz=Min('kviz__datumvreme')).order_by('najskoriji_kviz')
    #
    # for organizacija in organizacije:
    #     najskoriji_kviz = Kviz.objects.filter(idorg=organizacija['idorg'],
    #                                            datumvreme=organizacija['najskoriji_kviz']).first()

    contex = {
        "organizatori": organizatori,
        # "kvizovi": kvizovi,
    }

    return render(request, "organizacije.html", contex)

def org_req(request: HttpRequest, org_id):
    """
        Funkcija koja obradjuje zahtev za prikazom stranice izabranog organizatora

        Args:
            request (HttpRequest): zahtev za generisanje stranice, id organizacije ciju stranicu generisemo

        Returns:
            HttpResponse: generise stranicu odgovarajuceg organizatora
    """

    try:
        organizator = Organizator.objects.get(idorg=org_id)
    except Organizator.DoesNotExist:
        return redirect("nemate_privilegije")

    user = request.user
    if organizator.odobren == 0:
        if not user.is_authenticated: return redirect("nemate_privilegije")
        k = Korisnik.objects.get(id=user.id)
        if k.idulo != Uloga.objects.get(naziv='moderator'):
            return redirect("nemate_privilegije")

    context = {
        # "organizator": organizator
    }

    context = recenzija_req(request, org_id, context)

    if (context["ocene"] != []):
        prosek = sum(context["ocene"]) / len(context["ocene"])
        prosek = int(round(prosek))
    else:
        prosek = 0

    context["organizator"] = organizator
    context["prosek"] = prosek

    return render(request, 'org_strana.html', context)

