#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <random>
#include <algorithm>
#include <chrono>

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

// Struktura za cuvanje resenja
struct Resenje {
    double a, b, S, h, r;
    int dominacija; // 0 ili 1
};

double calculate_H(double a, double b, double S, double I, double sigma) {
    double d = std::sqrt(4 * S / M_PI);
    double N = b / d;
    double z_0 = b / 2 + std::pow(10, -2);

    double H = (N * I) / (2 * b) * ((z_0 + b / 2) / std::sqrt(a * a + (z_0 + b / 2) * (z_0 + b / 2)) -
        (z_0 - b / 2) / std::sqrt(a * a + (z_0 - b / 2) * (z_0 - b / 2)));
    return H;
}

double calculate_R(double a, double b, double S, double I, double sigma) {
    double d = std::sqrt(4 * S / M_PI);
    double N = b / d;
    double R = N * 2 * M_PI * a / (sigma * S);
    return R;
}

int main() {
    double I = 1;
    double sigma = 58;

    std::vector<Resenje> lista_resenja;

    // Generator slucajnih brojeva
    std::mt19937 gen(std::chrono::system_clock::now().time_since_epoch().count());
    std::uniform_real_distribution<> dist_a(1e-2, 5 * 1e-2);
    std::uniform_real_distribution<> dist_b(0.1, 0.4);
    std::uniform_real_distribution<> dist_S(0.5e-6, 3e-6);

    const int max_iter = 1e6;
    for (int i = 0; i < max_iter; ++i) {
        double a = dist_a(gen);
        double b = dist_b(gen);
        double S = dist_S(gen);

        //double a = 10e-2 + static_cast <float> (rand()) / (static_cast <float> (RAND_MAX / (5 * 10e-2 - 10e-2)));
        //double b = 0.1 + static_cast <float> (rand()) / (static_cast <float> (RAND_MAX / (0.4 - 0.1)));
        //double S = 0.5e-6 + static_cast <float> (rand()) / (static_cast <float> (RAND_MAX / (3e-6 - 0.5e-6)));

        double h = calculate_H(a, b, S, I, sigma);
        double r = calculate_R(a, b, S, I, sigma);

        lista_resenja.push_back({ a, b, S, h, r, 0 });
    }

    // Odredjivanje dominacije
    for (int i = 0; i < max_iter; ++i) {
        bool flag_dom = false;
        for (int j = 0; j < max_iter; ++j) {
            if (i == j) continue;
            if (lista_resenja[j].h >= lista_resenja[i].h && lista_resenja[j].r <= lista_resenja[i].r) {
                if (lista_resenja[j].h > lista_resenja[i].h || lista_resenja[j].r < lista_resenja[i].r) {
                    flag_dom = true;
                    break;
                }
            }
        }
        if (!flag_dom) {
            lista_resenja[i].dominacija = 1;
        }
        if (i % 1000 == 0) std::cout << i << std::endl;

    }

    // Cuvanje rezultata u datoteku
    std::ofstream rezultati_fajl("rezultati.txt");
    for (const auto& res : lista_resenja) {
        rezultati_fajl << "a: " << res.a << ", b: " << res.b << ", S: " << res.S << ", h: " << res.h << ", r: " << res.r << ", dominacija: " << res.dominacija << std::endl;
    }
    rezultati_fajl.close();

    return 0;
}