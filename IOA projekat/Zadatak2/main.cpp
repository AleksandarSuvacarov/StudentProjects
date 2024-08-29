

#include <iostream>
#include <cfloat>

using namespace std;

int next_permutation(const int N, int* P) {

	int s;
	int* first = &P[0];
	int* last = &P[N - 1];
	int* k = last - 1;
	int* l = last;

	while (k > first) {
		if (*k < *(k + 1)) 
			break;
		k--;
	}

	if (*k > *(k + 1))
		return 0;

	while (l > k) {
		if (*l > *k)
			break;
		l--;
	}

	s = *k;
	*k = *l;
	*l = s;

	first = k + 1;

	while (first < last) {
		s = *first;
		*first = *last;
		*last = s;

		first++;
		last--;
	}

	return 1;
}


int main() {

	//Za promenu iz 8 u 12 i obrnuto samo promeniti N i otkomentarisati odgovarajuci niz, a onaj drugi zakoemntarisati

	const int N = 8;
	int* P = new int[N];

	for (int i = 0; i < N; i++)
		P[i] = i + 1;

	//Za 8 tacaka
	double X[N] = {2.7, 2.7, 9.1, 9.1, 15.1, 15.3, 21.5, 22.9};
	double Y[N] = { 33.1, 56.8, 40.3, 52.8, 49.6, 37.8, 45.8, 32.7};

	//Za 12 tacaka
	/*double X[N] = { 2.7, 2.7, 9.1, 9.1, 15.1, 15.3, 21.5, 22.9, 33.4, 28.4, 34.7, 45.7 };
	double Y[N] = { 33.1, 56.8, 40.3, 52.8, 49.6, 37.8, 45.8, 32.7, 60.5, 31.7, 26.4, 25.1 };*/

	double distances[N][N];

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			distances[i][j] = sqrt((X[i] - X[j]) * (X[i] - X[j]) + (Y[i] - Y[j]) * (Y[i] - Y[j]));
		}
	}


	//ispis rastojanja
	/*for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			printf("%lf\t", distances[i][j]);
		}
		printf("\n");
	}*/

	double min_udalj = FLT_MAX;

	do {
		double udalj = 0;

		for (int i = 0; i < N - 1; i++) {
			udalj += distances[P[i] - 1][P[i + 1] - 1];
		}
		if (udalj < min_udalj) {
			min_udalj = udalj;
			printf("%lf\n", min_udalj);
			for (int i = 0; i < N; i++) {
				printf("%2d ", P[i]);
			}
			printf("\n");
		}

	} while (next_permutation(N, P));
	
	delete[] P;

}