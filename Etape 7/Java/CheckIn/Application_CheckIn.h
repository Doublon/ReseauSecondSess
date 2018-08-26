#include "UtileCheckIn.h"

#define VERIF_BILLET 1
#define DECONNEXION 2
#define QUITTER 10

struct _bagages
{
	float poids;
	char valise;
};
typedef struct _bagages Bagages;

char * StructLoginToChar(RequeteLogin param);
char * StructBilletToChar(RequeteBillet param);
void SaisieConnexion(char * login, char * password);
int Menu();
void SaisieBillet(char * billet, int * nbAcc);
float SaisieBagages(int *cptBagages, Bagages * tab);
void AffichageCompteRenduBagages(float poidsTotalBagages, float excedentPoidsBagages, float aPayer);
float CalculExcedentBagages(float pExcedent, float pPoidsTotal, int pNbAcc);

char sep_trame[2],fin_trame[2],sep_csv[2];
char address[25];
int port_chck;
