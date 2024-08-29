from Crypto.PublicKey import RSA
from Crypto.Random import get_random_bytes
from datetime import datetime

from Crypto.Cipher import CAST
from Crypto.Signature import PKCS1_v1_5
from Crypto.Hash import SHA1
from base64 import b64encode, b64decode
from Crypto.Util.Padding import unpad, pad

# Generisanje RSA ključeva i sifrovanje privatnog dela lozinkom

s1 = None
s2 = None

def generisi_rsa_kljuceve(duzina, lozinka):
    duzina_kljuca = duzina
    rsa_kljuc = RSA.generate(duzina_kljuca, get_random_bytes)

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    privatni_kljuc = rsa_kljuc.export_key()
    javni_kljuc = rsa_kljuc.publickey().export_key()
    #print(privatni_kljuc.decode())
    javni_kljuc_int = rsa_kljuc.publickey().n
    najnizih_64_bita = javni_kljuc_int & ((1 << 64) - 1)
    enc_privatni_kljuc = sifruj_privatni_kljuc_rsa(privatni_kljuc, lozinka)

    return enc_privatni_kljuc, javni_kljuc, timestamp, format(najnizih_64_bita, '016x')

def generisi_sha1_hash(lozinka):
    sha1_hash = SHA1.new()
    sha1_hash.update(lozinka.encode('utf-8'))
    return sha1_hash.digest()

#funkcija za pravljenje potpisa
def napraviPotpis(poruka, timestamp, private_key_str):
    #prima u bajtovima
    private_key = RSA.import_key(private_key_str)
    data_to_hash = f"{poruka}{timestamp}".encode('utf-8')
    sha1_hash = SHA1.new(data_to_hash)
    signer = PKCS1_v1_5.new(private_key)
    potpis = signer.sign(sha1_hash)
    return potpis

def verifikujPotpis(poruka, timestamp, potpis, public_key_str):
    public_key = RSA.import_key(public_key_str)
    data_to_hash = f"{poruka}{timestamp}".encode('utf-8')
    sha1_hash = SHA1.new(data_to_hash)
    verifier = PKCS1_v1_5.new(public_key)
    try:
        verifier.verify(sha1_hash, potpis)
        #print("Potpis je validan.")
        return 1
    except (ValueError, TypeError):
        #print("Potpis NIJE validan.")
        return -1

def izvadi_vodeca_dva_okteta(potpis):
    vodeci_okteti = potpis[:2]
    return vodeci_okteti

# Funkcija za šifrovanje privatnog dela RSA ključa CAST-128 algoritmom
def sifruj_privatni_kljuc_rsa(privatni_kljuc, lozinka):
    sha1_hash = generisi_sha1_hash(lozinka)
    key_for_cast_128 = sha1_hash[-16:]
    cipher = CAST.new(key_for_cast_128, CAST.MODE_ECB)
    padded_privatni_kljuc = pad(privatni_kljuc, CAST.block_size)
    enc_privatni_kljuc = cipher.encrypt(padded_privatni_kljuc)
    return enc_privatni_kljuc

def desifruj_kljuc(enc_privatni_kljuc, lozinka):
    try:
        sha1_hash = generisi_sha1_hash(lozinka)
        key_for_cast_128 = sha1_hash[-16:]
        cipher = CAST.new(key_for_cast_128, CAST.MODE_ECB)
        padded_desifrirani_privatni_kljuc = cipher.decrypt(enc_privatni_kljuc)
        desifrirani_privatni_kljuc = unpad(padded_desifrirani_privatni_kljuc, CAST.block_size)
        return desifrirani_privatni_kljuc
    except Exception:
        return -1

def uvezi_javni_kljuc(file_path):
    try:
        with open(file_path, "r") as f:
            javni_kljuc_str = f.read()
            javni_kljuc = RSA.import_key(javni_kljuc_str)
            timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

            javni_kljuc_int = javni_kljuc.n
            najnizih_64_bita = javni_kljuc_int & ((1 << 64) - 1)

            return javni_kljuc.export_key(), timestamp, format(najnizih_64_bita, '016x')
    except FileNotFoundError:   #fajl nije pronadjen
        return -1
    except ValueError:  #nije moguce raspakovati kljuc
        return -2

def uvezi_ceo_kljuc(file_path, lozinka):
    try:
        with open(file_path, "r") as f:
            kljuc_str = f.read()
            rsa_kljuc = RSA.import_key(kljuc_str)
            timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            privatni_kljuc = rsa_kljuc.export_key()
            javni_kljuc = rsa_kljuc.publickey().export_key()
            # print(privatni_kljuc.decode())
            javni_kljuc_int = rsa_kljuc.publickey().n
            najnizih_64_bita = javni_kljuc_int & ((1 << 64) - 1)
            enc_privatni_kljuc = sifruj_privatni_kljuc_rsa(privatni_kljuc, lozinka)

            return enc_privatni_kljuc, javni_kljuc, timestamp, format(najnizih_64_bita, '016x')
    except FileNotFoundError:   #fajl nije pronadjen
        return -1
    except ValueError:  #nije moguce raspakovati kljuc
        return -2

# def uporedi_privatne_kljuceve(kljuc1, kljuc2):
#     if kljuc1 == kljuc2:
#         print("Isto")
#         return True
#     else:
#         print("Nije isto")
#         return False

# Primer korišćenja:
# enc_privatni_kljuc, javni_kljuc, timestamp, najnizih_64_bita = generisi_rsa_kljuceve(2048, "mama")
# kljuc = desifruj_kljuc(enc_privatni_kljuc, "mama")

# print("Privatni ključ:")
# print(privatni_kljuc.decode('utf-8'))
#
# print("\nJavni ključ:")
# print(javni_kljuc.decode('utf-8'))
#
# print("\nTimestamp generisanja ključeva:")
# print(timestamp)
#
# print("\nNajnižih 64 bita javnog ključa:")
# print(f"{najnizih_64_bita:064b} ({najnizih_64_bita})")