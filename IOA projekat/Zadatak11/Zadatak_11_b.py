import matplotlib.pyplot as plt

def ucitaj_podatke(fajl_ime):
    lista_resenja = []
    with open(fajl_ime, 'r') as fajl:
        for linija in fajl:
            delovi = linija.split(',')
            a = float(delovi[0].split(': ')[1])
            b = float(delovi[1].split(': ')[1])
            S = float(delovi[2].split(': ')[1])
            h = float(delovi[3].split(': ')[1])
            r = float(delovi[4].split(': ')[1])
            dominacija = int(delovi[5].split(': ')[1])
            lista_resenja.append([a, b, S, h, r, dominacija])
    return lista_resenja

def iscrtaj_podatke(lista_resenja):
    # x_koordinate = [element[3] for element in lista_resenja]  # h vrednosti
    # y_koordinate = [element[4] for element in lista_resenja]  # r vrednosti
    # boje = ['blue' if element[5] == 0 else 'red' for element in lista_resenja]
    #
    # plt.scatter(x_koordinate, y_koordinate, color=boje, s=5)

    x_plave = [element[3] for element in lista_resenja if element[5] == 0]
    y_plave = [element[4] for element in lista_resenja if element[5] == 0]
    x_crvene = [element[3] for element in lista_resenja if element[5] == 1]
    y_crvene = [element[4] for element in lista_resenja if element[5] == 1]

    # Iscrtavanje plavih tačaka
    plt.scatter(x_plave, y_plave, c='blue', s=10, label='Ostala resenja')

    # Iscrtavanje crvenih tačaka preko plavih
    plt.scatter(x_crvene, y_crvene, c='red', s=10, label='Pareto front')


    plt.title('Pareto front optimizacije elektromagneta')
    plt.xlabel('H_max')
    plt.ylabel('R_min')
    plt.legend()

    plt.savefig("grafik_11_pareto_front")
    plt.show()

# Ime fajla gde su sačuvani podaci
fajl_ime = "D:/Materijali/7. semestar/IOA/Zadaci/ProjekatIOA/ProjekatIOA/rezultati.txt"
lista_resenja = ucitaj_podatke(fajl_ime)
iscrtaj_podatke(lista_resenja)