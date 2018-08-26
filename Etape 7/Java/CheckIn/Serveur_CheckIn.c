#include "Serveur_CheckIn.h"

int main(void)
{
	int hSocketEcoute,hSocketService;
	int i,j,ret,retRecv,retSend;
	struct sockaddr_in adresseSocket;
	unsigned int tailleSockaddr_in;
	char msgServeur[MAXSTRING];
	
	printf("***** Thread principal serveur demarre ! *****\n");
	printf("Identite = %d.%u\n", getpid(), pthread_self());

	//LireFichierConf(FICHIER_CONFIG_SERVEUR,address,&port_chck,sep_trame,fin_trame,sep_csv);
	LireFichierConfEtapeSept(FICHIER_CONFIG_SERVEUR,address,&port_chck,sep_trame,fin_trame,sep_csv,address_bagages,&port_bagages);

	pthread_mutex_init(&mutexIndiceCourant, NULL);
	pthread_mutex_init(&mutexFichierLogin, NULL);
	pthread_mutex_init(&mutexFichierBillets, NULL);
	pthread_cond_init(&condIndiceCourant, NULL);

	for (i=0; i<NB_MAX_CLIENTS; i++) hSocketConnectee[i] = -1;
	
	hSocketEcoute = InitSocket(&adresseSocket,&tailleSockaddr_in,address,port_chck);
	BindServeur(&hSocketEcoute,&adresseSocket,&tailleSockaddr_in);

	for(i=0; i<NB_MAX_CLIENTS; i++)
	{
		ret = pthread_create(&threadHandle[i],NULL,fctThread, (void*)i);
		printf("Thread secondaire %d lance !\n", i+1);
		ret = pthread_detach(threadHandle[i]);
	}

	do
	{
		printf("Thread principal : en attente d'une connexion\n");
		hSocketService = GestionConnexionEntrante(&hSocketEcoute,&adresseSocket,&tailleSockaddr_in);

		printf("Recherche d'une socket connectee libre ...\n");
		for (j=0; j<NB_MAX_CLIENTS && hSocketConnectee[j] !=-1; j++);

		if(j == NB_MAX_CLIENTS)
		{
			printf("Plus de connexion disponible\n");
			sprintf(msgServeur,DOC);
			retSend = Envoyer(hSocketService,msgServeur,MAXSTRING,0);
			close(hSocketService);
		}
		else
		{
			printf("Connexion sur la socket num. %d\n", j+1);
			pthread_mutex_lock(&mutexIndiceCourant);
			hSocketConnectee[j] = hSocketService;
			indiceCourant=j;
			pthread_mutex_unlock(&mutexIndiceCourant);
			pthread_cond_signal(&condIndiceCourant);
			sprintf(msgServeur,"Connexion OK");
			retSend = Envoyer(hSocketService,msgServeur,MAXSTRING,0);
		}
	}
	while (1);	

	close(hSocketEcoute);
	printf("Socket serveur fermee\n");
	
	#ifdef _WIN32
		WSACleanup();
	#endif // _WIN32
	
	return 0;
}


void * fctThread (void *param)
{
	int indiceSocket = (int) param, retRecv, retSend;
	int hSocketServ;
	int finDialogue=0, iCliTraite;
	int i,j;

	float excedent = 0;

	char msgClient[MAXSTRING], msgServeur[MAXSTRING];
	char numVol[MAXSTRING], numFichier[MAXSTRING], finBillet[MAXSTRING];
	char nomFichier[MAXSTRING], tabEcritureFichier[MAXSTRING][MAXSTRING];;
	char *buf = (char*)malloc(100);
	char * numThr = getThreadIdentity();

	char * retourDecoupe;
	RequeteLogin * reqLog;
	int typeRequete,retour;
	char * chaine;
	RequeteBillet * reqBil;
	RequeteBagage * reqBag;
	RequetePaiement * reqPay;

	printf("Debut de thread : Identite = %d.%d\n", getpid(), pthread_self());
	printf("Je travaille sur la socket de service %d\n", indiceSocket+1);

	while (1)
	{
		pthread_mutex_lock(&mutexIndiceCourant);
		while (indiceCourant == -1)
			pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);
		iCliTraite = indiceCourant; 
		indiceCourant=-1;
		hSocketServ = hSocketConnectee[iCliTraite];
		pthread_mutex_unlock(&mutexIndiceCourant);
		sprintf(buf,"Je m'occupe du numero %d ...", iCliTraite+1);
		affThread(numThr, buf);

		finDialogue=0;
		do
		{
			sprintf(buf,"En attente d'une requete ...");
			affThread(numThr,buf);

			retRecv = Recevoir(hSocketServ,msgClient,MAXSTRING,0);

			if (retRecv == 0)
			{
				sprintf(buf,"Le client est parti !"); 
				affThread(numThr, buf);
				finDialogue=1;
				break;
			}

			if (strcmp(msgClient, EOC)==0)
			{
				sprintf(buf, "Message recu : %s\n", msgClient);
				affThread(numThr, buf);
				finDialogue=1; 
				break;
			}
	
			retourDecoupe = DecoupeMessage(msgClient, &typeRequete, sep_trame, fin_trame);

			if(retourDecoupe != NULL)
			{
				switch(typeRequete)
				{
					case LOGIN_OFFICER:
						reqLog = (RequeteLogin *) retourDecoupe;
						chaine = (char *) malloc(strlen(reqLog->login) + strlen(reqLog->password));
						sprintf(chaine, "%s%s%s", reqLog->login, sep_csv, reqLog->password);

						retour = VerificationCSV(chaine,FICHIER_LOGIN,&mutexFichierLogin);

						if(retour == 1)
						{
							sprintf(buf, "Login reussi !");
						}
						else
						{
							sprintf(buf, "Le login ou le mot de passe est incorrect.");
						}
						affThread(numThr,buf);
						strcpy(msgServeur, buf);

						retSend = Envoyer(hSocketServ,msgServeur,MAXSTRING,0);
					break;

					case LOGOUT_OFFICER:							
						printf("L'utilisateur %s s'est deconnecte.\n", reqLog->login);
					break;

					case CHECK_TICKET:
						reqBil = (RequeteBillet *) retourDecoupe;
						chaine = (char *) malloc(strlen(reqBil->billet)+3);
						sprintf(chaine,"%s%s%d", reqBil->billet, sep_csv, reqBil->nombreAccompagnants);

						/*retour = VerificationCSV(chaine,FICHIER_BILLETS,&mutexFichierBillets);
						

						if(retour == 1)
						{
							sprintf(buf, "Billet valide !");

						}
						else
						{
							sprintf(buf, "Le billet n'a pas ete trouve...");

						}
						affThread(numThr,buf);
						strcpy(msgServeur, buf);

						retSend = Envoyer(hSocketServ,msgServeur,MAXSTRING,0);*/

						char * messageRetour = VerificationBillet(chaine, address_bagages, port_bagages);

						char * token, *pTok;
						token = strtok_r(messageRetour, sep_trame, &pTok);
						int codeRetour = atoi(token);

						if(codeRetour == 200)
						{
							sprintf(buf, "Billet valide !");

						}
						else
						{
							sprintf(buf, "Le billet n'a pas ete trouve...");

						}
						affThread(numThr,buf);
						strcpy(msgServeur, buf);

						retSend = Envoyer(hSocketServ,msgServeur,MAXSTRING,0);
					break;

					case CHECK_LUGGAGE:
						reqBag = (RequeteBagage *) retourDecoupe;

						if(reqBag->poidsTotalBagages > QUOTAPERSONNE*reqBag->compteurBagages)
						{
							excedent = reqBag->poidsTotalBagages - (QUOTAPERSONNE*reqBag->compteurBagages);
						}
						else
						{
							excedent = 0;
						}

						sprintf(msgServeur,"%.2f#", excedent);
						retSend = Envoyer(hSocketServ,msgServeur,MAXSTRING,0);
					break;

					case PAYMENT_DONE:
						reqPay = (RequetePaiement *) retourDecoupe;

						char numVol[MAXSTRING],numFichier[MAXSTRING];
						DecoupeBillet(reqPay->billet, numVol, numFichier, finBillet);

						//sprintf(nomFichier, "%s%s_%s_lug.csv", DOSSIERBAGAGES, numVol, numFichier);
						for(i=0 ; i < strlen(reqBil->nom) ; i++)
						{
							reqBil->nom[i] = toupper(reqBil->nom[i]);
						}

						for(i=0 ; i < reqBag->compteurBagages ; i++)
						{
							sprintf(tabEcritureFichier[i], "%s-%s-%s-%s-%03d%s", numVol, reqBil->nom, numFichier, finBillet, i+1, sep_trame);
							/*if(toupper(reqPay->valises[i]) == 'O')
								sprintf(tabEcritureFichier[i] + strlen(tabEcritureFichier[i]), "VALISE");
							else
								sprintf(tabEcritureFichier[i] + strlen(tabEcritureFichier[i]), "PAS VALISE");*/
							if(toupper(reqPay->valises[i]) == 'O')
								sprintf(tabEcritureFichier[i] + strlen(tabEcritureFichier[i]), "O%s%.2f", sep_trame, reqPay->poids[i]);
							else
								sprintf(tabEcritureFichier[i] + strlen(tabEcritureFichier[i]), "N%s%.2f", sep_trame, reqPay->poids[i]);
						}

						//EcrireTableauFichierCSV(nomFichier,tabEcritureFichier,reqBag->compteurBagages);
						//sprintf(buf, "Les bagages ont été ajoutés avec succès.");
						messageRetour = InsertionBagages(tabEcritureFichier, reqBil->billet, reqBag->compteurBagages, address_bagages, port_bagages);

						token = strtok_r(messageRetour, sep_trame, &pTok);
						codeRetour = atoi(token);

						if(codeRetour == 201)
						{
							sprintf(buf, "Les bagages ont été ajoutés avec succès.");

						}
						else
						{
							sprintf(buf, "Une erreur est survenue lors de l'ajout des bagages.");

						}
						affThread(numThr,buf);
						strcpy(msgServeur, buf);

						retSend = Envoyer(hSocketServ,msgServeur,MAXSTRING,0);
					break;
				}
			}
		} while (!finDialogue);

		pthread_mutex_lock(&mutexIndiceCourant);
		hSocketConnectee[iCliTraite]=-1;
		pthread_mutex_unlock(&mutexIndiceCourant);
	}

	close (hSocketServ);
	return (void *)indiceSocket;
}

char * getThreadIdentity()
{
	char *buf = (char *)malloc(30);
	sprintf(buf, "%d.%d", getpid(), pthread_self());
	return buf;
}

