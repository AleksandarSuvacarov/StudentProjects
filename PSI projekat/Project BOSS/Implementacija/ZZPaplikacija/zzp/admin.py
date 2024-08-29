from django.contrib import admin

# Register your models here.
from .models import *

admin.site.register(Korisnik)
admin.site.register(Organizator)
admin.site.register(Uloga)
admin.site.register(Kviz)
admin.site.register(Tema)