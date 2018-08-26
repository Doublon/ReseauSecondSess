#ifndef SOCKETUTILITIES
#define SOCKETUTILITIES

#include "../UtileCheckIn.h"

#endif

int InitSocket(struct sockaddr_in *adrSocket, unsigned int *tailleSockaddr_in, char * address, int port);
struct hostent * RecuperationInformationsHote(char * address);
void PreparationStructureSockAddr_In(struct sockaddr_in * pAdresseSocket, struct hostent * infosHost, int port);
void BindServeur(int *hS, struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in);
void ConnexionClient(int *hS, struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in);
int GestionConnexionEntrante(int *hS, struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in);
void MiseEnEcoute(int hS);
int AccepterConnexion(int hS, struct sockaddr_in *pAdresseSocket, unsigned int * pTailleSockaddr_in);
int Recevoir(int hS, char * message, int taille, int flag);
int CharFinTrameRecu(char * pBuffer, int nbrBytesRecus);
int MarqueurRecu(char * pBuffer, int nbrBytesRecus);
int Envoyer(int hS, char * message, int taille, int flag);
