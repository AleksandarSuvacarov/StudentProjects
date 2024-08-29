from django.shortcuts import render
from .views_suvi import *
from .views_jovana import *

from .views_dzoni import *

from .views_civotamarev import *

# Create your views here.
""" U ovom dokumentu se nalaze samo omotacke funkcije koje pozivaju implementacije iz razlicitih views fajlova"""

def index(request):
    return index_req(request)

def kvizovi(request):
    return kvizovi_req(request)
    #return render(request, "kvizovi.html", {})

def org_strana(request, org_id):
    return org_req(request, org_id)

def organizacije(request):
    return organizatori_req(request)

def registracija(request):
    return registracija_req(request)

def login(request):
    return login_req(request)

def logout(request):
    return logout_req(request)

def kreiraj_objavu(request):
    return kreiraj_objavu_req(request)

def administrativni_meni(request):
    return admin_meni_req(request)

def prijavi_ekipu(request, kviz_id):
    return prijavi_ekipu_req(request, kviz_id)

def pregled_ekipa(request, kviz_id):
    return pregled_ekipa_req(request, kviz_id)

def odobravanje_zahteva(request):
    return odobravanje_zahteva_req(request)

def nemate_privilegije(request):
    return render(request, 'nemate_privilegije.html', {})
