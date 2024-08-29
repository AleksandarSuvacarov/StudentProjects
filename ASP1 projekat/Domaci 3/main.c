#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define SAFE_MALLOC(pointer, n, size)\
	pointer = malloc(n * size);\
	if (pointer == NULL){printf("MEM_GRESKA"); exit (0); }\

#define SAFE_CALLOC(pointer, count, size)\
    pointer = calloc(count, size);\
    if (pointer == NULL) {printf("MEM_GRESKA"); exit(0);}\

#define SAFE_REALLOC(pointer, size)\
    pointer = realloc(pointer, size);\
    if (pointer == NULL) {printf("MEM_GRESKA"); exit(0);}\


int** napravi_graf(int** graf, int br_cvorova, char* spisak_slova) {
	SAFE_CALLOC(graf, br_cvorova, sizeof(int*));
	for (int i = 0; i < br_cvorova; i++) {
		SAFE_CALLOC(graf[i], br_cvorova, sizeof(int));
	}

	for (int i = 0; i < br_cvorova; i++) {
		printf("Unesite oznaku za %d. cvor: ", i);
		getchar();
		spisak_slova[i] = getchar();
	}
	return graf;
}

int** dodaj_cvor(int** graf, char slovo, int* br_cvorova, char* spisak_slova) {
	int a, *row;

	if (*br_cvorova != 0) {
		a = (*br_cvorova) + 1;
		SAFE_REALLOC(graf, a * sizeof(int*));
		SAFE_CALLOC(row, a, sizeof(int));
		graf[a - 1] = row;
		for (int i = 0; i < a - 1; i++) {
			graf[i] = realloc(graf[i], a * sizeof(int));
		}
		for (int i = 0; i < a; i++) {
			graf[i][a - 1] = 0;
		}
	}

	spisak_slova[*br_cvorova] = slovo;
	++(*br_cvorova);

	return graf;

}

int** izbaci_cvor(int** graf, char slovo, int* br_cvorova, char* spisak_slova) {
	int i, pos, j;

	for (i = 0; i < *br_cvorova; i++) {
		if (spisak_slova[i] == slovo) break;
	}

	pos = i;
	for (i = pos; i < *br_cvorova - 1; i++) {
		spisak_slova[i] = spisak_slova[i + 1];
	}

	for (i = 0; i < *br_cvorova; i++) {
		for (j = pos; j < *br_cvorova - 1; j++) {
			graf[i][j] = graf[i][j + 1];
		}
	}

	for (i = 0; i < *br_cvorova; i++) { 
		SAFE_REALLOC(graf[i], (*br_cvorova - 1) * sizeof(int)); 
	}

	for (i = pos; i < *br_cvorova - 1; i++) {
		graf[i] = graf[i + 1];
	}
	SAFE_REALLOC(graf, (*br_cvorova - 1) * sizeof(int*));

	(*br_cvorova)--;

	return graf;

}

void ispisi_graf(int** graf, int br_cvorova, char* spisak_slova) {
	int i, j;

	putchar('\n');
	putchar(' ');
	putchar(' ');
	for (i = 0; i < br_cvorova; i++) {
		printf("%c ", spisak_slova[i]);
	}
	putchar('\n');
	for (i = 0; i < br_cvorova; i++) {
		printf("%c ", spisak_slova[i]);
		for (j = 0; j < br_cvorova; j++) printf("%d ", graf[i][j]);
		putchar('\n');
	}

}

int** dodaj_granu(int** graf, char slovo1, char slovo2, int br_cvorova, char* spisak_slova) {
	int i, j;

	for (i = 0; i < br_cvorova; i++) {
		if (spisak_slova[i] == slovo1) break;
	}
	for (j = 0; j < br_cvorova; j++) {
		if (spisak_slova[j] == slovo2) break;
	}

	graf[i][j] = 1;

	return graf;

}

int** izbaci_granu(int** graf, char slovo1, char slovo2, int br_cvorova, char* spisak_slova) {
	int i, j;

	for (i = 0; i < br_cvorova; i++) {
		if (spisak_slova[i] == slovo1) break;
	}
	for (j = 0; j < br_cvorova; j++) {
		if (spisak_slova[j] == slovo2) break;
	}

	graf[i][j] = 0;

	return graf;

}

void obrisi_graf(int** graf, int br_cvorova) {
	for (int i = 0; i < br_cvorova; i++) free(graf[i]);
	free(graf);
}

void meni() {
	putchar('\n');
	printf("Napravite novi graf.......1\n");
	printf("Dodajte cvor..............2\n");
	printf("Izbacite cvor.............3\n");
	printf("Dodajte granu.............4\n");
	printf("Izbacite granu............5\n");
	printf("Ispisite graf.............6\n");
	printf("Obrisite graf.............7\n");
	printf("Zavrsite program..........8\n");
	putchar('\n');
}

void pogreska() {
	printf("Pogresni podaci! Pokusajte ponovo!\n");
	meni();
}

int main() {
	int unos, br_cvorova, n, ind, ind1, ind2;
	int** graf;
	char spisak_slova[30], slovo, slovo1, slovo2;

	br_cvorova = 0;
	graf = NULL;
	ind2 = 0;

	meni();

	while (1) {
		ind = scanf("%d", &unos);

		if (ind == 1) {

			switch (unos)
			{
			case 1:
				printf("Unesite inicijalni broj cvorova: ");
				ind1 = scanf("%d", &br_cvorova);
				if (ind1 == 0) { pogreska(); break; }
				graf = napravi_graf(graf, br_cvorova, spisak_slova);
				meni();
				break;
			case 2:
				if (br_cvorova == 0) { pogreska(); break; }
				getchar();
				printf("Unesite slovo oznake za novi cvor: ");
				slovo = getchar();
				graf = dodaj_cvor(graf, slovo, &br_cvorova, spisak_slova);
				meni();
				break;
			case 3:
				if (br_cvorova == 0) { pogreska(); break; }
				getchar();
				printf("Unesite slovo oznake cvora koji izbacujete: ");
				slovo = getchar();
				graf = izbaci_cvor(graf, slovo, &br_cvorova, spisak_slova);
				meni();
				break;
			case 4:
				if (br_cvorova == 0) { pogreska(); break; }
				getchar();
				printf("Unesite slova oznake za pocetni i krajnji cvor sa razmakom: ");
				slovo1 = getchar();
				getchar();
				slovo2 = getchar();
				graf = dodaj_granu(graf, slovo1, slovo2, br_cvorova, spisak_slova);
				meni();
				break;
			case 5:
				if (br_cvorova == 0) { pogreska(); break; }
				getchar();
				printf("Unesite slova oznake za pocetni i krajnji cvor sa razmakom: ");
				slovo1 = getchar();
				getchar();
				slovo2 = getchar();
				graf = izbaci_granu(graf, slovo1, slovo2, br_cvorova, spisak_slova);
				meni();
				break;
			case 6:
				if (br_cvorova == 0) { pogreska(); break; }
				ispisi_graf(graf, br_cvorova, spisak_slova);
				meni();
				break;
			case 7:
				if (br_cvorova == 0) { pogreska(); break; }
				obrisi_graf(graf, br_cvorova);
				graf = NULL;
				br_cvorova = 0;
				meni();
				break;
			case 8:
				ind2 = 1;
				break;
			default:
				pogreska();
				break;
			}
		
		}
		else {
			pogreska();
		}

		if (ind2) break;

	}
	return 0;
}