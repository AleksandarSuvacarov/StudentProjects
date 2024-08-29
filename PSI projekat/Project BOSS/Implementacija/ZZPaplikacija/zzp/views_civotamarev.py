from django.shortcuts import render, redirect
from django.http import HttpRequest, HttpResponse
from django.contrib.auth import login, authenticate, logout
from .models import *
from datetime import datetime

def recenzija_req(request: HttpRequest, org_id, context):
    """
            Funkcija koja obradjuje zahtev za prikaz recenzija na stranici org_stranica.html. Takodje obradjuje zahtev
            za brisanje recenzije, kao i ucitavanje predstojecih dogadjaja specificnih za organizaciju sa cije je stranice zahtev

            Args:
                request (HttpRequest): zahtev za generisanje
                org_id (int): id organizacije ciji se sadrzaj prikazuje na stranici
                context (python dictionary): prosledjen iz views_jovana.py

            Returns:
                context(python dictionary): vracen u views_jovana.py
        """
    context = {
        "poruka" : ""
    }

    user = request.user

    if (request.method == "POST"):

        if('rec_za_brisanje' in request.POST):
            obrisi= request.POST['rec_za_brisanje']
            # request.POST.pop('rec_za_brisanje')
            recenzija= Recenzija.objects.get(idrec=obrisi)
            recenzija.delete()
            context["poruka"] = "Recenzija je uspešno obrisana."

        else:
            komentar = request.POST['recenzijica']

            ocena=request.POST['nevidljiv']

            organizator = Organizator.objects.get(idorg=org_id)
            vreme = datetime.now()

            recenzija= Recenzija(komentar=komentar, idkorrec=user, idorgrec=organizator, ocena=ocena, datumvreme=vreme)
            recenzija.save()

    #recenzije = Recenzija.objects.get(idorgrec__exact=org_id)
    recenzije = Recenzija.objects.filter(idorgrec=org_id)
    ocene = [rec.ocena for rec in recenzije]
    dogadjaji = Kviz.objects.filter(datumvreme__gt=datetime.now()).filter(idorg=org_id).order_by('datumvreme')

    # context = {
    #     "recenzije": recenzije,
    #     "ocene": ocene,
    #     "user": user
    # }
    context["recenzije"] = recenzije
    context["ocene"] = ocene
    context["user"] = user
    context["dogadjaji"] = dogadjaji

    return context



def index_req(request: HttpRequest):
    """
        Funkcija koja obradjuje zahtev za prikazom glavne stranice i dinamicki ucitava njen sadrzaj

        Args:
            request (HttpRequest): zahtev za generisanje stranice

        Returns:
            HttpResponse: generise glavnu stranicu
    """

    user = request.user

    if user.is_authenticated:
        korisnik = Korisnik.objects.get(id=user.id)
        if korisnik.idulo == Uloga.objects.get(naziv="admin"):
            return redirect('administrativni_meni')

    dogadjaji = Kviz.objects.filter(datumvreme__gt=datetime.now()).order_by('datumvreme')
    organizacije = Organizator.objects.filter(odobren__exact="1")
    slike = []

    for org in organizacije:
        slike.append(org.slika.url)

    if len(dogadjaji) > 3:
        dogadjaji = dogadjaji[:3]

    context = {
        "dogadjaji" : dogadjaji,
        'slike': slike,
    }

    return render(request, 'index.html', context)
