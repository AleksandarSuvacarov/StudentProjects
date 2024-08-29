#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <cmath>
#include <iostream>
#include <fstream>
#include <sstream>
#include <time.h>

//Aleksandar Suvacarov 2020/0355 - Geneticki algoritam

using namespace std;


const int N = 64;
long F_size = pow(2, 25);
long F_size2 = pow(2, 26);
int s[N] = { 173669, 275487, 1197613, 1549805, 502334, 217684, 1796841, 274708, 631252, 148665, 150254, 4784408, 344759, 440109, 4198037, 329673, 28602, 144173, 1461469, 187895, 369313, 959307, 1482335, 2772513, 1313997, 254845, 486167, 2667146, 264004, 297223, 94694, 1757457, 576203, 8577828, 498382, 8478177, 123575, 4062389, 3001419, 196884, 617991, 421056, 3017627, 131936, 1152730, 2676649, 656678, 4519834, 201919, 56080, 2142553, 326263, 8172117, 2304253, 4761871, 205387, 6148422, 414559, 2893305, 2158562, 465972, 304078, 1841018, 1915571 };


long h_min = 1;
long h_max = 10;
int h_i = 0;
long f1 = 0;
long f2 = 0;
long f_opt_proslo = 0;
long f_opt = 0;
float a = 0.95;
double T0 = 64 * 1024 * 1024;
double T = 64 * 1024 * 1024;
long i_tot = 100000;
int broj = 0;
int broj_ponavljanja = 20;
int broj_iner = 0;

long f_min = T0;
int niz_x3[N];
float srednje_najbolje = 0;

int const Npop = 20000;
int populacija[Npop][N];
int funkcije_populacije[Npop];
int const mi = 3000;
int roditelji[mi][N];
int ukrst_min = 12;
int ukrst_max = 52;
float p_ukrst = 0.8;

float p_mutacija = 0.2;

int broj_generacija = 50;

int hamming_distance(int h_min, int h_max, int i, int i_tot) {
    double h_i = 0;
    h_i = 1. * (h_min - h_max) / (i_tot - 1) * i + h_max;
    return int(h_i);
}

int random_int(int a, int b) {
    int res;
    int r = rand();
    res = a + r % (b - a + 1);
    return res;
}

void random_combination(int n, int k, int* P) {
    if (k > n) return;

    int i, j, r, c;
    int* Q = new int[n];

    for (i = 0; i < n; i++)
        Q[i] = 0;

    for (i = 0; i < k; i++) {

        r = random_int(0, n - 1 - i);
        c = 0;
        for (j = 0; j < n; j++) {
            if (Q[j] == 0) {
                if (r == c) {
                    Q[j]++;
                    break;
                }
                c++;
            }

        }

    }

    c = 0;
    for (i = 0; i < n; i++) {
        if (Q[i] == 1) {
            P[c] = i + 1;
            c++;
        }
    }

    delete[] Q;

}

long F_opt(int niz_dosao[]) {
    f1 = 0;
    f2 = 0;

    //Racunanje F1 i F2
    for (int i = 0; i < N; i++) {
        if (niz_dosao[i] == 1) {
            f1 += s[i];
        }
        if (niz_dosao[i] == 2) {
            f2 += s[i];
        }
    }
    f1 = F_size - f1;
    f2 = F_size - f2;

    if (f1 >= 0 && f2 >= 0) {
        f_opt = f1 + f2;
    }
    else {
        f_opt = F_size2;
    }
    return f_opt;
}

void decimacija() {
    int min_index = 0;

    for (int i = 0; i < Npop - 1; i++) {
        min_index = i;
        for (int j = i + 1; j < Npop; j++) {
            if (funkcije_populacije[min_index] > funkcije_populacije[j]) {
                min_index = j;
            }
        }
        if (min_index != i) {
            int temp = funkcije_populacije[i];
            funkcije_populacije[i] = funkcije_populacije[min_index];
            funkcije_populacije[min_index] = temp;

            for (int k = 0; k < N; k++) {
                temp = populacija[i][k];
                populacija[i][k] = populacija[min_index][k];
                populacija[min_index][k] = temp;
            }
        }
        if (i < mi) {
            for (int k = 0; k < N; k++) {
                roditelji[i][k] = populacija[i][k];
            }
        }
        else {
            break;
        }
    }
    
}

void turnir() {

    int curr = 0;
    int last = Npop - 1;
    while (curr < mi) {

        int r1 = random_int(0, last);
        int r2 = random_int(0, last);

        if (funkcije_populacije[r1] > funkcije_populacije[r2]) {
            r1 = r2;
        }

        int temp = funkcije_populacije[r1];
        funkcije_populacije[r1] = funkcije_populacije[last];
        funkcije_populacije[last] = temp;

        for (int k = 0; k < N; k++) {
            roditelji[curr][k] = populacija[r1][k];
            temp = populacija[r1][k];
            populacija[r1][k] = populacija[last][k];
            populacija[last][k] = temp;
        }
        last--;
        curr++;

    }

}

void ukrstanje() {
    int i = 0;
    while(i < Npop) {
        int r1 = rand() % mi;
        int r2 = rand() % mi;
        int cross_point = random_int(ukrst_min, ukrst_max);

        float randa = 1.0 * rand() / RAND_MAX;
        if (randa < p_ukrst) {
            
            for (int j = 0; j < N; j++) {
                if (j < cross_point) {
                    populacija[i][j] = roditelji[r1][j];
                    populacija[i + 1][j] = roditelji[r2][j];
                }
                else {
                    populacija[i][j] = roditelji[r2][j];
                    populacija[i + 1][j] = roditelji[r1][j];
                }
            }
            i += 2;
        }
    }

}

void mutacija() {
    for (int i = 0; i < Npop; i++) {
        float randa = 1.0 * rand() / RAND_MAX;
        if (randa < p_mutacija) {
            int j = rand() % N;
            int izabrani_gen = populacija[i][j];
            int zamena = rand() % 3;
            //if (izabrani_gen == zamena) zamena = (zamena + 1) % 3;
            while (izabrani_gen == zamena) zamena = rand() % 3;
            populacija[i][j] = zamena;
        }
    }
}

void geneticki_algoritam(ofstream& fajl) {

    for (int i = 0; i < Npop; i++) {
        funkcije_populacije[i] = F_opt(populacija[i]);
        if (funkcije_populacije[i] < f_min) {
            f_min = funkcije_populacije[i];
            for (int j = 0; j < N; j++) {
                niz_x3[j] = populacija[i][j];
            }
        }
        fajl << broj_iner * Npop + i << ", " << f_min << endl;
    }
    decimacija();
    //turnir();

    ukrstanje();

    mutacija();

}

void inicijalizacija() {

    for (int i = 0; i < Npop; i++) {
        for (int j = 0; j < N; j++) {
            int lll = rand() % 3;
            populacija[i][j] = lll;
        }
    }
    
}

int main() {

    //clock_t start_time = clock();


    srand(time(NULL));

    // Deklaracija i inicijalizacija niza od 64 elemenata
    int niz_x[N];
    int niz_x2[N];

    //ofstream najbolji("najbolji.txt");
    

    for (broj = 0; broj < broj_ponavljanja; broj++) {

        inicijalizacija();
        f_min = T0;
        std::stringstream naziv;
        naziv << "rezultat_" << broj << ".txt";
        ofstream outfile(naziv.str());
        if (!outfile.is_open()) {
            cerr << "Nije moguce otvoriti fajl za pisanje." << endl;
            return -42;
        }
        
        for (broj_iner = 0; broj_iner < broj_generacija; broj_iner++) {
            
            geneticki_algoritam(outfile);

            /*for (int i = 0; i < N; i++) {
                std::cout << niz_x3[i] << ", ";
            }
            std::cout << endl;
            std::cout << f_min << endl;
            std::cout << "Sledeca Iteracija" << endl;*/
        }
        outfile.close();

        srednje_najbolje += f_min;

        //Ispis u konzolu
        cout << broj << " :" << endl;
        /*for (int i = 0; i < N; i++) {
            std::cout << niz_x3[i] << ", ";
        }
        std::cout << endl;*/
        std::cout << f_min << endl;

        //Ispis u fajl najbolji
        /*najbolji << broj + 1 << ": " << f_min << endl;
        for (int i = 0; i < N; i++) {
            najbolji << niz_x3[i] << ((i == N - 1) ? "\n" : ", ");
        }
        najbolji << endl;*/
    }
    //Ispis srednje najbolje u fajl najbolji
   /* srednje_najbolje /= broj_ponavljanja;

    najbolji << "Srednje najbolje resenje: " << srednje_najbolje << endl;
    najbolji.close();*/


    //Ispis vremena potrebnog za izvrsavanje

    /*clock_t end_time = clock();

    double time_taken = (double)(end_time - start_time) / CLOCKS_PER_SEC;

    printf("Vreme izvrsavanja: %f sekundi\n", time_taken);*/


    return 0;


}