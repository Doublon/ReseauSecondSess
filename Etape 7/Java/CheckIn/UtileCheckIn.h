/**************************************************************************************************/
/* Auteurs : RORIVE Olivier / VERWIMP Jim                                                         */
/* Groupe : 2302                                                                                  */
/* Date de dernière mise à jour : 21/01/2018 00h08                                                */
/**************************************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <pthread.h>

#ifdef _WIN32
    #include <winsock2.h>
#else
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <errno.h>
    #include <netdb.h>
    #include <netinet/in.h>
    #include <netinet/tcp.h>
    #include <arpa/inet.h>
#endif // _WIN32

#define EOC "END_OF_CONNEXION"
#define DOC "DENY_OF_CONNEXION"

#define MAXSTRING 1000

#define LOGIN_OFFICER 1
#define LOGOUT_OFFICER 2
#define CHECK_TICKET 3
#define CHECK_LUGGAGE 4
#define PAYMENT_DONE 5

#define QUOTAPERSONNE 20 /* Le poids de bagages maximum par personne */

#define FICHIER_LOGIN "Files/login.csv"
#define FICHIER_BILLETS "Files/billets.csv"
#define FICHIER_CONFIG_SERVEUR "serveur_checkin.conf"
#define FICHIER_CONFIG_CLIENT "application_checkin.conf"
#define DOSSIERBAGAGES "Bagages/"

#define MAXBAGAGES 25

#ifndef STRUCTURES
	#define STRUCTURES
	struct _RequeteLogin
	{
		int requete;
		char login[MAXSTRING];
		char password[MAXSTRING];
	};
	typedef struct _RequeteLogin RequeteLogin;

	struct _RequeteBillet
	{
		int requete;
		char nom[MAXSTRING];
		char billet[MAXSTRING];
		int nombreAccompagnants;
	};
	typedef struct _RequeteBillet RequeteBillet;

	struct _RequeteBagages
	{
		int requete;
		float poidsTotalBagages;
		int compteurBagages;
	};
	typedef struct _RequeteBagages RequeteBagage;

	struct _RequetePaiement
	{
		int requete;
		char nom[MAXSTRING];
		char billet[MAXSTRING];
		char valises[MAXBAGAGES];
		float poids[MAXBAGAGES];
	};
	typedef struct _RequetePaiement RequetePaiement;
#endif

void LireChaine(char *pChaine, short maxElem);
void LireFichierConf(char * filename, char * address, int * port, char * sep_trame, char * fin_trame, char * sep_csv);
void LireFichierConfEtapeSept(char * filename, char * address, int * port, char * sep_trame, char * fin_trame, char * sep_csv, char * address_bagages, int * port_bagages);
