#ifndef LIBRAIRIECSV
#define LIBRAIRIECSV

#include "../UtileCheckIn.h"

#endif

char ** LireFichierCSV(char * nomFichier, int *nombreLus, pthread_mutex_t mutex);
void EcrireTableauFichierCSV(char *nomFichier, char tableau[MAXSTRING][MAXSTRING], int nombreElements);
