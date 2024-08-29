from django.contrib import admin
from django.urls import path
from . import views
from .views import *

urlpatterns = [
    path('', index, name='index'),
    # path('org_strana/', views.org_strana, name='org_strana'),
    # path('org_strana/<int:org_id>', views.org_strana, name='org_strana'),
    path('kvizovi/', views.kvizovi, name='kvizovi'),
    path('organizacije/', views.organizacije, name='organizacije'),
    path('organizacije/<int:org_id>', org_strana, name='org_strana'),

    path('registracija/', registracija, name='registracija'),
    path('login/', login, name='login'),
    path('logout/', logout, name='logout'),
    path("kreiranje_objave/", kreiraj_objavu, name='kreiranje_objave'),

    path("kvizovi/<int:kviz_id>/", prijavi_ekipu, name="prijavi_ekipu"),
    path("kvizovi/prikaz_ekipe/<int:kviz_id>", pregled_ekipa, name="pregled_ekipa"),

    path('administrativni_meni/', administrativni_meni, name='administrativni_meni'),

    path('odobravanje_zahteva/', odobravanje_zahteva, name="odobravanje_zahteva"),

    path('nemate_privilegije', nemate_privilegije, name="nemate_privilegije")

]