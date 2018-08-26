#include "UtileCheckIn.h"

void LireChaine(char *pChaine, short maxElem)
{
	char c;
	short i,valide=0;
	char *pDbt;

	pDbt = pChaine;

	while(valide == 0)
	{
		i=0;
		valide=1;
		pChaine = pDbt;
		printf("\nVotre saisie : ");
		fflush(stdin);
		c=getchar();
		while(c != '\n' && i < maxElem-1 && valide == 1)
		{
			*pChaine = c;
			pChaine++;
			i++;
			c=getchar();
		}

		if(valide == 1)
		{
			*pChaine = '\0';
		}
	}
}

void LireFichierConf(char * filename, char * address, int * port, char * sep_trame, char * fin_trame, char *sep_csv)
{
	FILE *fp;
	char buffer[MAXSTRING];
	char * token, *pTok;

	fp = fopen(filename, "r");

	if(fp != NULL)
	{
		fgets(buffer,MAXSTRING,fp);
		do
		{
			token = strtok_r(buffer,"=",&pTok);
			if(strcmp("PORT_CHCK",token) == 0)
			{
				token = strtok_r(NULL,"\n",&pTok);
				*port = atoi(token);
			}
			else
			{
				if(strcmp("ADDRESS",token) == 0)
				{
					token = strtok_r(NULL,"\n",&pTok);
					strcpy(address,token);
					address[strlen(address)] = '\0';
				}
				else
				{
					if(strcmp("SEP-TRAME",token) == 0)
					{
						token = strtok_r(NULL,"\n",&pTok);
						strcpy(sep_trame,token);
						sep_trame[strlen(sep_trame)] = '\0';
					}
					else
					{
						if(strcmp("FIN-TRAME",token) == 0)
						{
							token = strtok_r(NULL,"\n",&pTok);
							strcpy(fin_trame,token);
							fin_trame[strlen(fin_trame)] = '\0';
						}
						else
						{
							if(strcmp("SEP-CSV",token) == 0)
							{
								token = strtok_r(NULL,"\n",&pTok);
								strcpy(sep_csv,token);
								sep_csv[strlen(sep_csv)] = '\0';
							}
						}
					}
				}
			}
		} while(fgets(buffer,MAXSTRING,fp));
		fclose(fp);
	}
	else
	{
		printf("Impossible d'ouvrir le fichier de configuration.");
		exit(1);
	}
}

void LireFichierConfEtapeSept(char * filename, char * address, int * port, char * sep_trame, char * fin_trame, char *sep_csv, char * address_bagages, int * port_bagages)
{
	FILE *fp;
	char buffer[MAXSTRING];
	char * token, *pTok;

	fp = fopen(filename, "r");

	if(fp != NULL)
	{
		fgets(buffer,MAXSTRING,fp);
		do
		{
			token = strtok_r(buffer,"=",&pTok);
			if(strcmp("PORT_CHCK",token) == 0)
			{
				token = strtok_r(NULL,"\n",&pTok);
				*port = atoi(token);
			}
			else
			{
				if(strcmp("ADDRESS",token) == 0)
				{
					token = strtok_r(NULL,"\n",&pTok);
					strcpy(address,token);
					address[strlen(address)] = '\0';
				}
				else
				{
					if(strcmp("SEP-TRAME",token) == 0)
					{
						token = strtok_r(NULL,"\n",&pTok);
						strcpy(sep_trame,token);
						sep_trame[strlen(sep_trame)] = '\0';
					}
					else
					{
						if(strcmp("FIN-TRAME",token) == 0)
						{
							token = strtok_r(NULL,"\n",&pTok);
							strcpy(fin_trame,token);
							fin_trame[strlen(fin_trame)] = '\0';
						}
						else
						{
							if(strcmp("SEP-CSV",token) == 0)
							{
								token = strtok_r(NULL,"\n",&pTok);
								strcpy(sep_csv,token);
								sep_csv[strlen(sep_csv)] = '\0';
							}
							else
							{
								if(strcmp("ADDRESS_BAGAGES", token) == 0)
								{
									token = strtok_r(NULL,"\n",&pTok);
									strcpy(address_bagages,token);
									address_bagages[strlen(address_bagages)] = '\0';
								}
								else
								{
									token = strtok_r(NULL,"\n",&pTok);
									*port_bagages = atoi(token);
								}
								
							}
						}
					}
				}
			}
		} while(fgets(buffer,MAXSTRING,fp));
		fclose(fp);
	}
	else
	{
		printf("Impossible d'ouvrir le fichier de configuration.");
		exit(1);
	}
}

