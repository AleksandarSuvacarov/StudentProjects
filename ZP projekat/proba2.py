from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
import base64

# Funkcija za šifrovanje
def encrypt(text, public_key):
    rsa_public_key = RSA.import_key(public_key)
    cipher = PKCS1_OAEP.new(rsa_public_key)
    encrypted_bytes = cipher.encrypt(text.encode('utf-8'))
    encrypted_text = base64.b64encode(encrypted_bytes).decode('utf-8')
    return encrypted_text

# Funkcija za dešifrovanje
def decrypt(encrypted_text, private_key):
    rsa_private_key = RSA.import_key(private_key)
    cipher = PKCS1_OAEP.new(rsa_private_key)
    encrypted_bytes = base64.b64decode(encrypted_text)
    decrypted_bytes = cipher.decrypt(encrypted_bytes)
    decrypted_text = decrypted_bytes.decode('utf-8')
    return decrypted_text


key = RSA.generate(2048)
public_key = key.publickey().export_key()
private_key = key.export_key()

marko_public_str = """-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDm1nMa1hVPBdOUFZj5BqvgedsX
yveTzu1t4x8O8IiqC1nUMTTjQjxbz2gDHAqNcaFd6Q/ccPvdcm/wL+L8b90FMSUC
hAwDHPY+5un1+BQq3lAQE0i38K/+nOQEE0JJxbWcvwS4h7acY0sZVjYSKfIAANrz
ruPriFr/wzkDGXb8AQIDAQAB
-----END PUBLIC KEY-----"""

marko_private_str = """-----BEGIN RSA PRIVATE KEY-----
MIICXgIBAAKBgQDm1nMa1hVPBdOUFZj5BqvgedsXyveTzu1t4x8O8IiqC1nUMTTj
Qjxbz2gDHAqNcaFd6Q/ccPvdcm/wL+L8b90FMSUChAwDHPY+5un1+BQq3lAQE0i3
8K/+nOQEE0JJxbWcvwS4h7acY0sZVjYSKfIAANrzruPriFr/wzkDGXb8AQIDAQAB
AoGASNeGFgNVUQzlIZcZIQf+Z8T8IottSaqkGKdxbwzvCz2uuHtYlZgapNwTlduc
5tpjEdfQJf9PMyd/9DqgNYl1JCsuV+6o9ECakIx3cZV9Cfq6b6EcDVKiSDFQqjTj
8AuWHbxvdYuavRqmX1b8ZoQrAk3BZvXO0SJf+birOLMRHksCQQDnkqghXDztg1ws
is2wPcn1PdXZd1yfffeN331VoPJjlJyPdvYbwMN8ob13HuMvvbfbCcNG2UdOlMXA
Q4qCkHwrAkEA/y/wq5+9DiVWMnoq00bqK2kJHljTN03Ic52ofBUoheU+hMReazB4
gjOcm7KE3FcMTiakvv47gs1AGaM3KfNWgwJBAJfDL1idoHhv7EpstRxZgi+s1Kdu
aE9GOKTL4NHrZq8ISAplhijuDa9yOD8HCRDSntl37nhSET9YlLjKCs5Cbn0CQQCg
k3MXG8okF5x44OUH04zQyiRhwzUNjNWA4wk9Izc/ofRIKSTcwEBkdcOrOZde6yuk
Fa7/PA0Ijs0yFMFGlYevAkEAhbX0iFYMzaG4oI8myTyKK/r2C3t/F5w2LKbWZ0VT
9aPt6TVjz900sNKKCY0WjWxyInPuc3TAAs1yUiEUKBPiUA==
-----END RSA PRIVATE KEY-----"""


text1 = "macka"
text2 = "pas"
text3 = "konj"

encrypted_text = encrypt(text1, public_key)
print("Šifrovani tekst:", encrypted_text)


decrypted_text = decrypt(encrypted_text, private_key)
print("Dešifrovani tekst:", decrypted_text)

encrypted_text = encrypt(text2, marko_public_str)
print("Šifrovani tekst:", encrypted_text)


decrypted_text = decrypt(encrypted_text, marko_private_str)
print("Dešifrovani tekst:", decrypted_text)



# with open('Kljucevi/Marko_a_public_key.pem', 'r') as file:
#     public_key_str = file.read()
with open('Kljucevi/k.pem', 'r') as file:
    key_str = file.read()

# encrypted_text = encrypt(text3, key_str)
# print("Šifrovani tekst:", encrypted_text)
#
#
# decrypted_text = decrypt(encrypted_text, marko_private_str)
# print("Dešifrovani tekst:", decrypted_text)

j = RSA.import_key(key_str)
print(j.export_key())
print(j.publickey().export_key())