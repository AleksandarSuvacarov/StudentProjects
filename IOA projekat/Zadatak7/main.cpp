#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <cmath>
#include <iostream>
#include <fstream>
#include <sstream>

//Aleksandar Suvacarov 2020/0355

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

void simulirano_kaljenje(int* niz_x, int* niz_x2, ofstream& fajl) {

    for (int iter = 0; iter < i_tot; iter++) {
        h_i = hamming_distance(h_min, h_max, iter, i_tot);
        int* P = new int[h_i];
        random_combination(N, h_i, P);

        for (int j = 0; j < h_i; j++) {
            niz_x[P[j] - 1] = rand() % 3;
        }
        delete[] P;

        f1 = 0;
        f2 = 0;

        //Racunanje F1 i F2
        for (int i = 0; i < N; i++) {
            if (niz_x[i] == 1) {
                f1 += s[i];
            }
            if (niz_x[i] == 2) {
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

        if (f_opt < f_min) {
            f_min = f_opt;
            for (int i = 0; i < N; i++) {
                niz_x3[i] = niz_x[i];
            }
            
        }
        long delta_f = f_opt - f_opt_proslo;

        if (delta_f < 0) {
            for (int i = 0; i < N; i++) {
                niz_x2[i] = niz_x[i];
            }
            f_opt_proslo = f_opt;
        }
        else {
            float p;
            if (delta_f == 0)
                p = 0.5;
            else
                p = exp(-delta_f / T);
            float randa = 1.0 * rand() / RAND_MAX;
            if (randa < p) {
                for (int i = 0; i < N; i++) {
                    niz_x2[i] = niz_x[i];
                }
                f_opt_proslo = f_opt;
            }
            else {
                for (int i = 0; i < N; i++) {
                    niz_x[i] = niz_x2[i];
                }
            }
        }
        T = a * T;
        //if(iter < 100)
            fajl << iter + broj_iner * i_tot << ", " << f_min << endl;
        
        if (iter % 10000 == 0) {
            /*std::cout << f_opt_proslo << endl;
            std::cout << T << endl;*/
            /*for (int i = 0; i < N; i++) {
                cout << niz_x[i] << ", ";
            }
            cout << endl;
            for (int i = 0; i < N; i++) {
                cout << niz_x2[i] << ", ";
            }
            cout << endl;*/
        }

    }

    //std::cout << "Poslednji proracun: " << f_opt_proslo << endl;
}

void inicijalizacija(int* niz_x, int* niz_x2) {
    for (int i = 0; i < N; i++) {
        int lll = rand() % 3;
        niz_x[i] = lll;
        niz_x2[i] = lll;
    }

    T = T0;
    for (int i = 0; i < N; i++) {
        if (niz_x[i] == 1) {
            f1 += s[i];
        }
    }
    f1 = F_size - f1;
    for (int i = 0; i < N; i++) {
        if (niz_x[i] == 2) {
            f2 += s[i];
        }
    }
    f2 = F_size - f2;
    if (f1 >= 0 && f2 >= 0) {
        f_opt_proslo = f1 + f2;
    }
    else {
        f_opt_proslo = F_size2;
    }
}

int main() {

    srand(time(NULL));

    // Deklaracija i inicijalizacija niza od 64 elemenata
    int niz_x[N];
    int niz_x2[N];

    ofstream najbolji("najbolji.txt");
    

    for (broj = 0; broj < broj_ponavljanja; broj++) {

        inicijalizacija(niz_x, niz_x2);
        f_min = T0;
        std::stringstream naziv;
        naziv << "rezultat_" << broj << ".txt";
        ofstream outfile(naziv.str());
        if (!outfile.is_open()) {
            cerr << "Nije moguce otvoriti fajl za pisanje." << endl;
            return -42;
        }
        for (broj_iner = 0; broj_iner < 10; broj_iner++) {
            simulirano_kaljenje(niz_x, niz_x2, outfile);
            T = T0;
            for (int i = 0; i < N; i++) {
                niz_x[i] = niz_x3[i];
                niz_x2[i] = niz_x3[i];
            }
            /*for (int i = 0; i < N; i++) {
                niz_x[i] = niz_x2[i];
            }
            for (int i = 0; i < N; i++) {
                std::cout << niz_x[i] << ", ";
            }
            std::cout << endl;
            std::cout << f_opt_proslo << endl;
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
        najbolji << broj + 1 << ": " << f_min << endl;
        for (int i = 0; i < N; i++) {
            najbolji << niz_x3[i] << ((i == N - 1) ? "\n" : ", ");
        }
        najbolji << endl;
    }
    srednje_najbolje /= broj_ponavljanja;

    najbolji << "Srednje najbolje resenje: " << srednje_najbolje << endl;
    najbolji.close();

    return 0;


}