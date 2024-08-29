from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
import base64

# Učitavanje javnog ključa
public_key_str = """-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtdiB0SjReId2CWiG/nol2VqM8
K0ZjDP6JTWSZAPs2WqG8N4M7KX+IuD0n8HQn108DWWYRmm5Vt32zDKniowyXWEaM
Sp9PQZ8TQHKsIQrUQPOXUQfRiJLjqvbYQgXLAvtIju2DGoF881Hq0cjTrGQhefem
mgbcFAR0rmJ2mUOqqQIDAQAB
-----END PUBLIC KEY-----"""

public_key = RSA.import_key(public_key_str)

# Pretvaranje reči u bajtove
text = "macka"
text_bytes = text.encode('utf-8')

# Šifrovanje
cipher = PKCS1_OAEP.new(public_key)
encrypted_bytes = cipher.encrypt(text_bytes)

# Pretvaranje šifrovanih podataka u Base64 string
encrypted_text = base64.b64encode(encrypted_bytes).decode('utf-8')
print("Šifrovani tekst:", encrypted_text)