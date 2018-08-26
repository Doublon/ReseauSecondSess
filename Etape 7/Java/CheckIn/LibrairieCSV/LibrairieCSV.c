#include "LibrairieCSV.h"

char ** LireFichierCSV(char * nomFichier, int *nombreLus, pthread_mutex_t mutex)
{
	FILE *fp;
	int i=0;
	char temp[MAXSTRING];
	static char * tabRetour[MAXSTRING];

	pthread_mutex_lock(&mutex);
	fp = fopen(nomFichier, "r");

	if(fp != NULL)
	{
		while(fgets(temp,MAXSTRING,fp))
		{
			tabRetour[i] = (char *) malloc (strlen(temp)+1);
			strcpy(tabRetour[i],temp);
			tabRetour[i][strlen(tabRetour[i])-2] = '\0';
			i++;
			(*nombreLus)++;
		}
		fclose(fp);
	}
	pthread_mutex_unlock(&mutex);

	return tabRetour;
}

void EcrireTableauFichierCSV(char *nomFichier, char tableau[MAXSTRING][MAXSTRING], int nombreElements)
{
	FILE *fp;
	int i=0;

	fp = fopen(nomFichier,"w");

	if(fp != NULL)
	{
		for(i=0 ; i < nombreElements ; i++)
		{
			fputs(tableau[i],fp);
		}
		fclose(fp);
	}
}
