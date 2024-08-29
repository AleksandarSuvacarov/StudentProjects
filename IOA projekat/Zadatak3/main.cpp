#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <stdio.h>
#include <time.h>

using namespace std;

//void variations_with_repetition(int n, int k) {
//
//	int q;
//	int* P = new int[k];
//
//	for (int i = 0; i < k; i++)
//		P[i] = 0;
//
//	do {
//		for (int i = 0; i < k; i++)
//			printf("%5d", P[i] + 1);
//		printf("\n");
//
//		q = k - 1;
//
//		while (q >= 0) {
//			P[q]++;
//			if (P[q] == n) {
//				P[q] = 0;
//				q--;
//			}
//			else
//				break;
//		}
//
//
//	} while (q >= 0);
//
//	delete [] P;
//
//}

void SequenceToSpanningTree(int* P, int len, int* T) {
	int i, j, q = 0;
	int n = len + 2;
	int* V = new int[n];

	for (i = 0; i < n; i++)
		V[i] = 0;

	for (i = 0; i < len; i++)
		V[P[i] - 1] += 1;

	for (i = 0; i < len; i++)
	{
		for (j = 0; j < n; j++)
		{
			if (V[j] == 0)
			{
				V[j] = -1;
				T[q++] = j + 1;
				T[q++] = P[i];
				V[P[i] - 1]--;
				break;
			}
		}
	}
	j = 0;
	for (i = 0; i < n; i++) {
		if (V[i] == 0 && j == 0) {
			T[q++] = i + 1;
			j++;
		}
		else if (V[i] == 0 && j == 1) {
			T[q++] = i + 1;
			break;
		}
	}
	delete[] V;
}



int main() {
	int const N = 10;
	int n = 10;
	int k = 8;

	int q;
	int* P = new int[k];
	//int len = sizeof(P) / sizeof(P[0]);
	int len = 8;
	//bool proba = 1;

	clock_t start_time = clock();

	int matrica[N][N]; // Deklaracija matrice
	FILE* fajl;

	fajl = fopen("tabela.txt", "r");

	if (fajl == NULL) {
		printf("Nije moguce otvoriti fajl.\n");
		return 1;
	}

	// Citanje vrednosti iz fajla i smeštanje u matricu
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			fscanf(fajl, "%d", &matrica[i][j]);
		}
	}

	//Ispis matrice
	/*for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			printf("%d\t", matrica[i][j]);
		}
		printf("\n");
	}*/



	for (int i = 0; i < k; i++)
		P[i] = 0;


	long min_dalj = 99999;
	int* res_niz = new int[2 * len + 2];

	do {
		for (int i = 0; i < k; i++) {
			//printf("%5d", P[i] + 1);
			//varijacija
			P[i]++;
		}


		//SpanningTree

		int* T = new int[2 * (len + 1)];
		SequenceToSpanningTree(P, len, T);

		long dalj = 0;
		for (int i = 0; i < 2 * (len + 1) - 1; i += 2) {
			//printf("%d", T[i]);
			dalj += matrica[T[i] - 1][T[i + 1] - 1];
		}

		for (int i = 0; i < n; i++) {
			int br_pon = 0;
			for (int r = 0; r < 2 * (len + 1); r++) {
				if (T[r] == i + 1) br_pon++;
			}
			if (br_pon >= 4) dalj += (br_pon - 3) * 250;
		}

		/*if (proba) {
			printf("%d\n", dalj);
			proba = 0;
		}*/

		if (dalj < min_dalj) {
			min_dalj = dalj;
			for (int i = 0; i < 2 * (len + 1); i++) {
				res_niz[i] = T[i];
			}
		}

		delete[] T;

		for (int i = 0; i < k; i++) {
			P[i]--;
		}
		q = k - 1;

		while (q >= 0) {
			P[q]++;
			if (P[q] == n) {
				P[q] = 0;
				q--;
			}
			else
				break;
		}


	} while (q >= 0);

	delete[] P;

	char Niz_slova[N] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };

	for (int i = 0; i < 2 * (len + 1); i++) {
		//Ispis SpanningTree
		printf("%c", Niz_slova[res_niz[i] - 1]);
		if ((i + 1) % 2 == 0 && i < 2 * len)
			printf(" - ");
	}
	printf("\n%d", min_dalj);


	//Ispis vremena potrebnog za izvrsavanje
	clock_t end_time = clock();

	double time_taken = (double)(end_time - start_time) / CLOCKS_PER_SEC;

	printf("\n");
	printf("Vreme izvodjenja: %f sekundi\n", time_taken);

	return 0;
}