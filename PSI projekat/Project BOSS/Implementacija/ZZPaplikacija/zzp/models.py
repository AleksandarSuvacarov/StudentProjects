from django.db import models

# Create your models here.
from django.db import models
from django.contrib.auth.models import User, AbstractUser

# Create your models here.
"""
Model Korisnik koji cuva sve korisnik i ima polje uloge da bi se oni razlikovali
Dodatno ima polje odobren koje prati isto polje u modelu Organizator
Izveden iz klase AbstractUser
"""
class Korisnik(AbstractUser):
    idulo = models.ForeignKey('Uloga', models.DO_NOTHING, db_column='idUlo', null=True)  # Field name made lowercase.
    odobren = models.IntegerField(default=0)
    class Meta:
        db_table = 'korisnik'


"""Model Kviz cuva podatke neophodne za kvizove"""
class Kviz(models.Model):
    idkviz = models.AutoField(db_column='idKviz', primary_key=True)  # Field name made lowercase.
    idtem = models.ForeignKey('Tema', models.DO_NOTHING, db_column='idTem')  # Field name made lowercase.
    idorg = models.ForeignKey('Organizator', on_delete=models.CASCADE, db_column='idOrg')  # Field name made lowercase.
    naslov = models.CharField(max_length=255)
    opis = models.TextField()
    datumvreme = models.DateTimeField(db_column='datumVreme')  # Field name made lowercase.
    adresa = models.CharField(max_length=255)
    kotizacija = models.IntegerField()
    kapacitet = models.IntegerField()
    zauzetost = models.IntegerField(default=0)
    slika = models.ImageField(upload_to='imgs/', null=True)

    class Meta:
        db_table = 'kviz'


"""Model Organizator cuva podatke neophodne za organizatore i idnetifikaciono se odnosi na korisnika"""
class Organizator(models.Model):
    opis = models.TextField()
    brkvizova = models.IntegerField(db_column='brKvizova', default=0)  # Field name made lowercase.
    naziv = models.CharField(max_length=255)
    teme = models.CharField(max_length=255, null=True)
    idorg = models.OneToOneField(Korisnik, on_delete=models.CASCADE, db_column='idOrg', primary_key=True)  # Field name made lowercase.
    odobren = models.IntegerField(default=0)
    slika = models.ImageField(upload_to='imgs/', null=True)
    predstojecikviz = models.ForeignKey(Kviz, on_delete=models.CASCADE, db_column='predstojeciKviz', null=True) # Field name made lowercase.

    class Meta:
        db_table = 'organizator'


"""Model Prijava cuva podatke neophodne za prijavu"""
class Prijava(models.Model):
    idpri = models.AutoField(db_column='idPri', primary_key=True)  # Field name made lowercase.
    idkviz = models.ForeignKey(Kviz, on_delete=models.CASCADE, db_column='idKviz')  # Field name made lowercase.
    idkor = models.ForeignKey(Korisnik, on_delete=models.CASCADE, db_column='idKor')  # Field name made lowercase.
    nazivtima = models.CharField(db_column='nazivTima', max_length=45)  # Field name made lowercase.
    brclanova = models.IntegerField(db_column='brClanova')  # Field name made lowercase.

    class Meta:
        db_table = 'prijava'


"""Model Recenzija cuva podatke neophodne za recenzije"""
class Recenzija(models.Model):
    idrec = models.AutoField(db_column='idRec', primary_key=True)  # Field name made lowercase.
    idorgrec = models.ForeignKey(Organizator, on_delete=models.CASCADE, db_column='idOrgRec')  # Field name made lowercase.
    idkorrec = models.ForeignKey(Korisnik, on_delete=models.CASCADE, db_column='idKorRec')  # Field name made lowercase.
    ocena = models.IntegerField()
    komentar = models.TextField()
    datumvreme = models.DateTimeField(db_column='datumVreme')  # Field name made lowercase.

    class Meta:
        db_table = 'recenzija'


"""Model Tema cuva podatke neophodne za teme"""
class Tema(models.Model):
    idtem = models.AutoField(db_column='idTem', primary_key=True)  # Field name made lowercase.
    naziv = models.CharField(max_length=45)

    class Meta:
        db_table = 'tema'


"""Model Uloga cuva podatke neophodne za kvizove"""
class Uloga(models.Model):
    idulo = models.AutoField(db_column='idUlo', primary_key=True)  # Field name made lowercase.
    naziv = models.CharField(max_length=45)

    class Meta:
        db_table = 'uloga'
