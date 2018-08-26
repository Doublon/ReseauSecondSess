#include "LibrairieServeur.h"

char * DecoupeMessage(char * message, int * pTypeRequete, char * sep_trame, char * fin_trame)
{
	printf("Le message recu : %s\n", message);
	char * token, *pTok;
	RequeteLogin * reqLog;
	RequeteBillet * reqBil;
	RequeteBagage * reqBag;
	RequetePaiement * reqPay;
	int i = 0;
	token = strtok_r(message, sep_trame, &pTok);
	(*pTypeRequete) = atoi(token);
	switch(*pTypeRequete)
	{
		case LOGIN_OFFICER:
			reqLog = (RequeteLogin *) malloc (sizeof(RequeteLogin));
			reqLog->requete = LOGIN_OFFICER;
			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				strcpy(reqLog->login,token);
			else
				strcpy(reqLog->login,"");
			token = strtok_r(NULL,fin_trame,&pTok);
			if(token != NULL)
				strcpy(reqLog->password,token);
			else
				strcpy(reqLog->password,"");
			return (char *) reqLog;
		break;

		case LOGOUT_OFFICER:
			reqLog = (RequeteLogin *) malloc (sizeof(RequeteLogin));
			reqLog->requete = LOGOUT_OFFICER;

			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				strcpy(reqLog->login,token);
			else
				strcpy(reqLog->login,"");

			token = strtok_r(NULL,fin_trame,&pTok);
			if(token != NULL)
				strcpy(reqLog->password,token);
			else
				strcpy(reqLog->password,"");

			return (char *) reqLog;
		break;

		case CHECK_TICKET:
			reqBil = (RequeteBillet *) malloc (sizeof(RequeteBillet));
			reqBil->requete = CHECK_TICKET;

			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				strcpy(reqBil->nom, token);
			else
				strcpy(reqBil->nom, "");

			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				strcpy(reqBil->billet,token);
			else
				strcpy(reqBil->billet,"");

			token = strtok_r(NULL,fin_trame,&pTok);
			if(token != NULL)
				reqBil->nombreAccompagnants = atoi(token);
			else
				reqBil->nombreAccompagnants = 0;

			return (char *) reqBil;
		break;

		case CHECK_LUGGAGE:
			reqBag = (RequeteBagage *) malloc (sizeof(RequeteBagage));
			reqBag->requete = CHECK_LUGGAGE;

			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				reqBag->poidsTotalBagages = atoi(token);
			else
				reqBag->poidsTotalBagages = 0;

			token = strtok_r(NULL,fin_trame,&pTok);
			if(token != NULL)
				reqBag->compteurBagages = atoi(token);
			else
				reqBag->compteurBagages = 0;

			return (char *) reqBag;			
		break;

		case PAYMENT_DONE:
			reqPay = (RequetePaiement *) malloc (sizeof(RequetePaiement));
			reqPay->requete = PAYMENT_DONE;

			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				strcpy(reqPay->nom, token);
			else
				strcpy(reqPay->nom,"");

			token = strtok_r(NULL,sep_trame,&pTok);
			if(token != NULL)
				strcpy(reqPay->billet, token);
			else
				strcpy(reqPay->billet, "");

			while(token != NULL)
			{
				token = strtok_r(NULL,sep_trame,&pTok);
				if(token != NULL)
				{
					reqPay->poids[i] = atof(token);	
					token = strtok_r(NULL,sep_trame,&pTok);				
					reqPay->valises[i] = token[0];
					i++;				
					if(token[1] == '#')
					{
						break;
					}
						
				}
			}

			return (char *) reqPay;
		break;

		default: 
			return NULL;
		break;
	}
}

int VerificationCSV(char * chaine, char * nomFichier, pthread_mutex_t * mutex)
{
	int retour = 0, nbrLus = 0, i = 0;
	char ** tableauCSV;

	tableauCSV = LireFichierCSV(nomFichier,&nbrLus,*mutex);

	retour=0;
	for(i=0 ; i < nbrLus ; i++)
	{	
		if((strcmp(tableauCSV[i],chaine) == 0))
		{
			retour=1;
			i=nbrLus;
		}
	}

	return retour;
}

void DecoupeBillet(char * billet, char * numVol, char * numFichier, char * finBillet)
{
	char * token, * pTok;

	token = strtok_r(billet,"-",&pTok);
	strcpy(numVol,token);
	
	token = strtok_r(NULL,"-",&pTok);
	strcpy(numFichier,token);

	token = strtok_r(NULL, "\0",&pTok);
	strcpy(finBillet,token);
}

char *  VerificationBillet(char * chaine, char * address, int port)
{
	printf("CHaine : %s\n", chaine);
	int type = 1;
	int hSocket, retSend, retRecv;
	struct sockaddr_in adresseSocket;
	unsigned int tailleSockaddr_in;
	char msgClient[MAXSTRING], * msgServeur;

	sprintf(msgClient, "%d%c%d%c%s", strlen(chaine)+2, '|', type, ';', chaine);

	hSocket = InitSocket(&adresseSocket,&tailleSockaddr_in,address,port);
	ConnexionClient(&hSocket,&adresseSocket,&tailleSockaddr_in);

	printf("Envoyé : %s\n", msgClient);
	retSend = Envoyer(hSocket,msgClient,MAXSTRING,0);
	retRecv = Recevoir(hSocket,msgServeur,MAXSTRING,0);

	close(hSocket);

	return msgServeur;
}

char * InsertionBagages(char tableauBagages[MAXSTRING][MAXSTRING], char * numBillet, int longueurTableauBagages, char * address, int port)
{
	int type = 2;
	int hSocket, retSend, retRecv;
	struct sockaddr_in adresseSocket;
	unsigned int tailleSockaddr_in;
	char chaine[MAXSTRING],msgClient[MAXSTRING], * msgServeur;
	int i = 0;
	msgServeur = (char *) malloc (sizeof(MAXSTRING));
	strcpy(chaine, "");
	for(i = 0 ; i < longueurTableauBagages ; i++)
	{
		strcat(chaine, tableauBagages[i]);
		printf("Chaine : %s\n", chaine);
		if(i != longueurTableauBagages-1)
			strcat(chaine, ";");
	}

	int longueur = 2 + strlen(numBillet) + 1 + strlen(chaine);
	sprintf(msgClient, "%d%c%d%c%s%c%s", longueur, '|', type, ';', numBillet, ';', chaine);
	printf("Message : %s\n", msgClient);
	
	hSocket = InitSocket(&adresseSocket,&tailleSockaddr_in,address,port);
	ConnexionClient(&hSocket,&adresseSocket,&tailleSockaddr_in);

	retSend = Envoyer(hSocket,msgClient,MAXSTRING,0);
	retRecv = Recevoir(hSocket,msgServeur,MAXSTRING,0);

	close(hSocket);

	return msgServeur;
}

