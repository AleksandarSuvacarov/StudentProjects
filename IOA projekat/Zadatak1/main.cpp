
#include <iostream>
#include <time.h>

using namespace std;

int main() {

	//Varijanta pod A -> otkomentarisati celokupnu pretragu, a zakomentarisati optimizovanu
	//Varijanta pod B -> otkomentarisati optimizovanu pretragu, a zakomentarisati celokupnu

	clock_t start_time = clock();


	//A) celokupna pretraga

	/*int flag = 0;
	for (int i = 1; i <= 777; i++) {
		for (int j = 1; j <= 777; j++) {
			for (int k = 1; k <= 777; k++) {
				for (int q = 1; q <= 777; q++) {
					if ((i + j + k + q == 777) && (i * j * k * q == 777000000)) {
						printf("%d %d %d %d\n", i, j, k, q);
						flag = 1;
					}
					if (flag) break;
				}
				if (flag) break;
			}
			if (flag) break;
		}
		if(flag) break;
	}*/


	//B) optimizovana pretraga

	for (int i = 1; i <= 777; i++) {
		for (int j = 1; j <= 777; j++) {
			for (int k = 1; k <= 777; k++) {
				int q = 777 - i - j - k;
				if ((i + j + k + q == 777) && (i * j * k * q == 777000000)) {
					printf("%d %d %d %d\n", i, j, k, q);
				}
			}
		}
	}


	//Ispis vremena potrebnog za izvrsavanje

	clock_t end_time = clock();

	double time_taken = (double)(end_time - start_time) / CLOCKS_PER_SEC;

	printf("Vreme izvodjenja: %f sekundi\n", time_taken);

	return 0;



	return 0;
}