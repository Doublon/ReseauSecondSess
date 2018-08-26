#include "UtileCheckIn.h"
#include "SocketUtilities/SocketUtilities.h"
#include "LibrairieServeur/LibrairieServeur.h"

#define NB_MAX_CLIENTS 5 /* Nombre maximum de clients connectes */
#define affThread(num, msg) printf("th_%s> %s\n", num, msg)

void * fctThread(void * param);
char * getThreadIdentity();

pthread_mutex_t mutexIndiceCourant;
pthread_cond_t condIndiceCourant;
int indiceCourant=-1;
pthread_t threadHandle[NB_MAX_CLIENTS]; /* Threads pour clients */
int hSocketConnectee[NB_MAX_CLIENTS]; /* Sockets pour clients */
pthread_mutex_t mutexFichierLogin, mutexFichierBillets;

char sep_trame[2],fin_trame[2],sep_csv[2];
char address[25];
int port_chck;

int port_bagages;
char address_bagages[25];
