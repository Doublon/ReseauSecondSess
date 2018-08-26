#include "SocketUtilities.h"

int InitSocket(struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in, char * address, int port)
{
	#ifdef _WIN32
		WSADATA wsaData; /*The first line is a data structure that holds data about the current winsock version.*/
		WSAStartup(0x0202, &wsaData); /*The second line initialises the winsock component so you can use it.*/
	#endif

	int hS;
	struct hostent * infosHost;

	hS = CreerSocket();
	infosHost = RecuperationInformationsHote(address);
	PreparationStructureSockAddr_In(pAdresseSocket,infosHost,port);

	return hS;
}

int CreerSocket()
{
	int hS;

	hS = socket(AF_INET, SOCK_STREAM, 0); /* au sens strict : PF_INET */
	if (hS == -1)
	{
		printf("Erreur de creation de la socket %d\n", errno);
		exit(1);
	}
	else 
	{
		printf("Creation de la socket OK\n");
	}

	return hS;
}

struct hostent * RecuperationInformationsHote(char * address)
{
	struct hostent * infosHost;
	struct in_addr adresseIP;

	if((infosHost = gethostbyname(address)) == 0)
	{
		printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
		exit(1);
	}
	else 
	{
		printf("Acquisition infos host OK\n");
		memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
		printf("Adresse IP = %s\n",inet_ntoa(adresseIP));
	}

	return infosHost;
}

void PreparationStructureSockAddr_In(struct sockaddr_in * pAdresseSocket, struct hostent * infosHost, int port)
{
	memset(pAdresseSocket,0, sizeof(struct sockaddr_in));
	pAdresseSocket->sin_family = AF_INET;
	pAdresseSocket->sin_port = htons(port);
	memcpy(&pAdresseSocket->sin_addr, infosHost->h_addr,infosHost->h_length);
}

void BindServeur(int *hS, struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in)
{
	if(bind(*hS, (struct sockaddr *)pAdresseSocket, sizeof(struct sockaddr_in)) == -1)
	{
		printf("Erreur sur le bind de la socket %d\n", errno);
		exit(1);
	}
	else 
	{
		printf("Bind adresse et port socket OK\n");
	}
}

void ConnexionClient(int *hS, struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in)
{
	int ret;

	(*pTailleSockaddr_in) = sizeof(struct sockaddr_in);
	if (( ret = connect(*hS, (struct sockaddr *)pAdresseSocket, *(pTailleSockaddr_in))) == -1)
	{
		printf("Erreur sur connect de la socket %d\n", errno);
		close(*(hS)); 
		exit(1);
	}
	else 
	{
		printf("Connect socket OK\n");
	}
}

int GestionConnexionEntrante(int *hS, struct sockaddr_in *pAdresseSocket, unsigned int *pTailleSockaddr_in)
{
	int hSS;

	MiseEnEcoute(*hS);
	*(pTailleSockaddr_in) = sizeof(struct sockaddr_in);
	hSS = AccepterConnexion(*hS,pAdresseSocket,pTailleSockaddr_in);

	return hSS;
}

void MiseEnEcoute(int hS)
{
	if(listen(hS,SOMAXCONN) == -1)
	{
		printf("Erreur sur le listen de la socket %d\n", errno);
		close(hS);
		exit(1);
	}
	else 
	{
		printf("Listen socket OK\n");	
	}
}

int AccepterConnexion(int hS, struct sockaddr_in *pAdresseSocket, unsigned int * pTailleSockaddr_in)
{
	int hSS;

	if((hSS = accept(hS, (struct sockaddr *)pAdresseSocket, pTailleSockaddr_in)) == -1)
	{
		printf("Erreur sur l'accept de la socket %d\n", errno);
		close(hS); 
		exit(1);
	}
	else 
	{
		printf("Accept socket OK\n");
	}

	return hSS;
}

int Recevoir(int hS, char * message, int taille, int flag)
{
	printf("1\n");
	int retRecv, tailleMsgRecu = 0, finDetectee = 0;
printf("2\n");
	char buffer[taille];
printf("3\n");
	memset(message,0,sizeof(message));
printf("4\n");

	do
	{
printf("5\n");
		retRecv = recv(hS, buffer, taille, 0);
printf("6\n");
		if(retRecv == -1)
		{
printf("7\n");
			printf("Erreur sur le recv de la socket %d\n", errno);
printf("8\n");
			close(hS);
printf("9\n");
			exit(1);
		}
		else 
		{
printf("10\n");
			strcat(message,buffer);
printf("11\n");
			tailleMsgRecu += retRecv;
printf("12\n");
			finDetectee = CharFinTrameRecu(buffer,retRecv);
printf("13\n");
			printf("Fin detectee : ");
printf("14\n");
			if(finDetectee == 1)
				printf("Oui\n");
			else
				printf("Non\n");
printf("15\n");
		}
	} while (retRecv != 0 && retRecv != -1 && finDetectee == 0 && tailleMsgRecu < taille);
	printf("Recv socket OK -- Nombre de bytes recus : %d | Total : %d\n", retRecv, tailleMsgRecu);

	return tailleMsgRecu;
}

int CharFinTrameRecu(char * pBuffer, int nbrBytesRecus)
{
	int retour = 0, i = 0;

	for(i=0 ; i < nbrBytesRecus && retour == 0; i++)
	{
		if(pBuffer[i] = '#')
		{
			retour = 1;
		}
	}

	return retour;
}

int MarqueurRecu(char * pBuffer, int nbrBytesRecus)
{
	static int demiTrouve = 0;
	int i;
	int trouve = 0;
	
	if(demiTrouve == 1 && pBuffer[0] == '\n')
	{
		return 1;
	}
	else
	{
		demiTrouve = 0;
	}

	for(i=0 ; i < nbrBytesRecus-1 ; i++)
	{
		if(pBuffer[i] == '\r' && pBuffer[i+1] == '\n')
		{
			trouve = 1;
		}
	}

	if(trouve == 1)
	{
		return 1;
	}
	else
	{
		if(pBuffer[nbrBytesRecus] == '\r')
		{
			demiTrouve = 1;
			return 0;
		}
		return 0;
	}
}

int Envoyer(int hS, char * message, int taille, int flag)
{
	int retSend;
	retSend = send(hS, message, taille, 0);

	if(retSend == -1)
	{
		printf("Erreur sur le send de la socket %d\n", errno);
		close(hS);
		exit(1);
	}
	else 
	{
		printf("Send socket OK -- Nombre de bytes envoyes : %d\n", retSend);
	}

	return retSend;
}

