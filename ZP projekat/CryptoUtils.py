from Crypto.Cipher import AES, DES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP


def generisi_aes_kljuc_sesije():
    return get_random_bytes(16)  # 128-bitni ključ za AES

def generisi_tripledes_kljuc_sesije():
    return get_random_bytes(16)  # 128-bitni ključ za TripleDES

def enkriptuj_poruku_sa_kljucem_sesije(poruka, kljuc_sesije, algoritam):
    # Konvertuj poruku u bytes ako već nije
    if isinstance(poruka, str):
        poruka = poruka.encode('utf-8')
    if algoritam == "AES128":
        iv = get_random_bytes(16)  # Inicijalizacioni vektor (IV) za AES CBC mod
        cipher = AES.new(kljuc_sesije, AES.MODE_CBC, iv)
        padded_data = pad(poruka, AES.block_size)
    elif algoritam == "TripleDES":
        kljuc_sesije = kljuc_sesije[:8]
        iv = get_random_bytes(8)  # Inicijalizacioni vektor (IV) za TripleDES CBC mod
        cipher = DES.new(kljuc_sesije, DES.MODE_CBC, iv)
        padded_data = pad(poruka, DES.block_size)
    else:
        raise ValueError("Nepodržan algoritam")

    ciphertext = cipher.encrypt(padded_data)
    return iv + ciphertext

def dekriptuj_poruku_sa_kljucem_sesije(enkriptovani_podaci, kljuc_sesije, algoritam):
    if algoritam == "AES128":
        len(kljuc_sesije)
        iv = enkriptovani_podaci[:16]
        ciphertext = enkriptovani_podaci[16:]
        cipher = AES.new(kljuc_sesije, AES.MODE_CBC, iv)
        decrypted_data = cipher.decrypt(ciphertext)
        unpadded_data = unpad(decrypted_data, AES.block_size)
    elif algoritam == "TripleDES":
        kljuc_sesije = kljuc_sesije[:8]
        iv = enkriptovani_podaci[:8]
        ciphertext = enkriptovani_podaci[8:]
        cipher = DES.new(kljuc_sesije, DES.MODE_CBC, iv)
        decrypted_data = cipher.decrypt(ciphertext)
        unpadded_data = unpad(decrypted_data, DES.block_size)
    else:
        raise ValueError("Nepodržan algoritam")

    #uvek su bajtovi
    # if isinstance(unpadded_data, bytes):
    #     unpadded_data = unpadded_data.decode('utf-8')

    return unpadded_data

def enkriptuj_kljuc_sesije_sa_rsa( kljuc_sesije, javni_kljuc):
    rsa_javni_kljuc = RSA.import_key(javni_kljuc)
    rsa_cipher = PKCS1_OAEP.new(rsa_javni_kljuc)
    kriptovani_kljuc_sesije = rsa_cipher.encrypt(kljuc_sesije)
    return kriptovani_kljuc_sesije

def dekriptuj_kljuc_sesije_sa_rsa(kriptovani_kljuc_sesije, privatni_kljuc):
    rsa_privatni_kljuc = RSA.import_key(privatni_kljuc)
    rsa_cipher = PKCS1_OAEP.new(rsa_privatni_kljuc)
    kljuc_sesije = rsa_cipher.decrypt(kriptovani_kljuc_sesije)
    return kljuc_sesije
