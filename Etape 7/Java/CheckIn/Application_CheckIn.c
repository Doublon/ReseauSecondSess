#include "Application_CheckIn.h"

int main()
{
	// Permet le fonctionnement depuis/sur une machine Windows
	#ifdef _WIN32
		WSADATA wsaData; /* The first line is a data structure that holds data about the current winsock version. */
		WSAStartup(0x0202, &wsaData); /* The second line initialises the winsock component so you can use it. */
	#endif

	int hSocket, choix = 0, connecte, cpt=0, retConnexion, cptBagages = 0, i, encore, retR, retS;
	struct sockaddr_in adresseSocket;
	unsigned int tailleSockaddr_in;
	char *msgClient, msgServeur[MAXSTRING],reqPaymentDone[MAXSTRING],username[MAXSTRING],pass[MAXSTRING];
	RequeteLogin reqLog;
	RequeteBillet reqBil;
	Bagages tabBagages[MAXBAGAGES];
	float poidsTotalBagages = 0, excedentPoidsBagages = 0, aPayer = 0;

	LireFichierConf(FICHIER_CONFIG_CLIENT,address,&port_chck,sep_trame,fin_trame,sep_csv);

	hSocket = InitSocket(&adresseSocket,&tailleSockaddr_in,address,port_chck);
	retConnexion = ConnexionClient(&hSocket,&adresseSocket,&tailleSockaddr_in);

	retR = Recevoir(hSocket,msgServeur,MAXSTRING, 0);
	
	if(strcmp(msgServeur,DOC) != 0)
	{
		printf("Connexion etablie avec le serveur CheckIn !\n");
		do
		{
			printf("Vous devez vous connecter avant de continuer :\n");
			connecte=0;

			reqLog.requete = LOGIN_OFFICER;
			SaisieConnexion(reqLog.login, reqLog.password);
			msgClient = StructLoginToChar(reqLog);

			retS = Envoyer(hSocket,msgClient,MAXSTRING,0);
			retR = Recevoir(hSocket,msgServeur,MAXSTRING, 0);
			printf("Resultat du login : %s\n", msgServeur);

			if(strcmp("Login reussi !",msgServeur) == 0)
			{
				strcpy(username,reqLog.login);
				strcpy(pass,reqLog.password);
				connecte=1;
				printf("Utilisateur connecte : %s\n\n", username);
				do
				{
					choix = Menu();

					switch(choix)
					{
						case VERIF_BILLET:
							reqBil.requete = CHECK_TICKET;

							printf("Nom du client ? : ");
							LireChaine(reqBil.nom,MAXSTRING);

							SaisieBillet(reqBil.billet,&reqBil.nombreAccompagnants);
							msgClient = StructBilletToChar(reqBil);

							retS = Envoyer(hSocket,msgClient,MAXSTRING,0);
							retR = Recevoir(hSocket,msgServeur,MAXSTRING,0);
							printf("Resultat de la verification du billet : %s\n", msgServeur);

							if(strcmp(msgServeur, "Billet valide !") == 0)
							{
								cptBagages = 0;
								poidsTotalBagages = SaisieBagages(&cptBagages,&tabBagages[0]);
								
								sprintf(msgClient, "%d%s%.2f%s%d%s", CHECK_LUGGAGE, sep_trame, poidsTotalBagages, sep_trame, cptBagages, fin_trame);
	
								retS = Envoyer(hSocket,msgClient,MAXSTRING,0);	
								retR = Recevoir(hSocket,msgServeur,MAXSTRING,0);

								excedentPoidsBagages = atof(msgServeur);
								aPayer = CalculExcedentBagages(excedentPoidsBagages, poidsTotalBagages, reqBil.nombreAccompagnants);
								AffichageCompteRenduBagages(poidsTotalBagages,excedentPoidsBagages,aPayer);

								sprintf(msgClient, "%d%s%s%s%s", PAYMENT_DONE, sep_trame, reqBil.nom, sep_trame, reqBil.billet);
								for(i=0 ; i < cptBagages ; i++)
								{
									sprintf(msgClient + strlen(msgClient),"%s%.2f%s%c", sep_trame, tabBagages[i].poids, sep_trame, tabBagages[i].valise);
								}
								sprintf(msgClient+strlen(msgClient),"%s", fin_trame);

								retS = Envoyer(hSocket,msgClient,MAXSTRING,0);
								retR = Recevoir(hSocket,msgServeur,MAXSTRING,0);
								printf("Resultat : %s\n", msgServeur);
							}
						break;

						case DECONNEXION:
							connecte=0;
							reqLog.requete = LOGOUT_OFFICER;
							msgClient = StructLoginToChar(reqLog);
							retS = Envoyer(hSocket,msgClient,MAXSTRING,0);	
						break;

						case QUITTER:
							connecte=0;
							strcpy(msgClient,EOC);
							retS = Envoyer(hSocket,msgClient,MAXSTRING,0);
						break;
				
					}
				} while(connecte == 1);
			}
			else
			{
				if(strcmp(msgServeur,DOC) != 0)
				{
					do
					{
						printf("Voulez-vous essayer a nouveau essayer de vous connecter ?\n");
						printf("1. Oui\n");
						printf("%d. Non\n", QUITTER);
						printf("Votre choix : ");
						fflush(stdin);
						scanf("%d", &choix);
					} while(choix != 1 && choix != QUITTER);

					if(choix == QUITTER)
					{
						strcpy(msgClient,EOC);
						retS = Envoyer(hSocket,msgClient,MAXSTRING,0);
						printf("Message envoye = %s\n", msgClient);
					}
				}
			}
		} while (choix != QUITTER);
	}
	else
	{
		printf("Impossible de connecter au serveur. L'application va se fermer.\n");
	}

	close(hSocket);
	printf("Socket client fermee\n");

	#ifdef _WIN32
	WSACleanup();
	#endif // _WIN32

	return 0;
}

char * StructLoginToChar(RequeteLogin param)
{
	char * retour = (char *) malloc (MAXSTRING);	

	sprintf(retour, "%d%s%s%s%s%s", param.requete, sep_trame, param.login, sep_trame, param.password, fin_trame); 
	
	return retour;
}

char * StructBilletToChar(RequeteBillet param)
{
	char * retour = (char *) malloc (MAXSTRING);	

	sprintf(retour, "%d%s%s%s%s%s%d%s", param.requete, sep_trame, param.nom, sep_trame, param.billet, sep_trame, param.nombreAccompagnants, fin_trame); 
	
	return retour;
}

void SaisieConnexion(char * login, char * password)
{
	printf("Login");
	LireChaine(login,MAXSTRING);
	printf("Password");
	LireChaine(password,MAXSTRING);;
}

int Menu()
{
	int choix;

	do 
	{
		printf("\t\tMenu principal :\n");
		printf("\t\t-------------------------------\n");
		printf("\t\t1. Verifier un billet\n");
		printf("\t\t2. Deconnexion\n");
		printf("\t\t%d. Quitter\n", QUITTER);
		printf("\t\t-------------------------------\n");
		printf("\t\tVotre choix : ");
		fflush(stdin);
		scanf("%d", &choix);
	} while (((choix < 1 || choix > 2) && choix != QUITTER));

	return choix;
}

void SaisieBillet(char * billet, int * nbAcc)
{
	printf("\nNumero de billet ? : ");
	LireChaine(billet,MAXSTRING);

	printf("Nombre d'accompagnants ? : ");
	fflush(stdin);
	scanf("%d", nbAcc);
}

float SaisieBagages(int *cptBagages, Bagages * tab)
{
	int encore=1;
	char saisiePoids[10];
	float poidsTotalBagages = 0;
	
	while(encore == 1 && *cptBagages < MAXBAGAGES)
	{
		do
		{
			printf("Poids du bagage n°%d : ", (*cptBagages)+1);
			LireChaine(saisiePoids,10);
			tab->poids = atof(saisiePoids);
			if(tab->poids < 0)
				printf("Le poids doit etre strictement positif.\n");
		} while(tab->poids < 0);

		do
		{
			printf("Valise (O ou N) ? : ");
			fflush(stdin);
			scanf("%c", &tab->valise);
			poidsTotalBagages += tab->poids;
			if(tab->valise != 'O' && tab->valise != 'N')
				printf("Veuillez entrer O pour oui et N pour non.\n");
		} while(tab->valise != 'O' && tab->valise != 'N');
	
		tab++;
		(*cptBagages)++;
		printf("Y a-t-il encore des bagages a encoder ? (1. Oui / 0. Non) :\n");
		do
		{
			printf("Votre choix : ");
			fflush(stdin);
			scanf("%d", &encore);
		} while (encore != 0 && encore != 1);
	}

	return poidsTotalBagages;
}

void AffichageCompteRenduBagages(float poidsTotalBagages, float excedentPoidsBagages, float aPayer)
{
	printf("Poids total bagages : %.2f kg\n", poidsTotalBagages);
	printf("Excedent bagages : %.2f kg\n", excedentPoidsBagages);
	printf("Supplement a payer : %.2f EUR\n", aPayer);					
	printf("Paiement effectue ? : O\n");
}

float CalculExcedentBagages(float pExcedent, float pPoidsTotal, int pNbAcc)
{
	float aPayer = 0;

	if(pExcedent > 0)
		aPayer = pPoidsTotal/pExcedent*pNbAcc;

	return aPayer;
}

