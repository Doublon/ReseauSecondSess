#ifndef LIBRAIRIESERVEUR
#define LIBRAIRIESERVEUR

#include "../UtileCheckIn.h"
#include "../LibrairieCSV/LibrairieCSV.h"
#include "../SocketUtilities/SocketUtilities.h"

#endif

char * DecoupeMessage(char * message, int * pTypeRequete, char * sep_trame, char * fin_trame);
int VerificationCSV(char * chaine, char * nomFichier, pthread_mutex_t * mutex);
void DecoupeBillet(char * billet, char * numVol, char * numFichier, char * finBillet);
char * VerificationBillet(char * chaine, char * adresse, int port);
char * InsertionBagages(char tableauBagages[MAXSTRING][MAXSTRING], char * numBillet, int longueurTableauBagages, char * address, int port);
