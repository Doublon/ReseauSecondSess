//
// Created by Tusse on 25/08/2018.
//

#include <iostream>
#include <cstring>
#include "CSV.h"

using namespace std;

libCSV::CSV::CSV(std::string pathCSV) : _pathCSV(pathCSV)
{
}

void libCSV::CSV::ReadCSV()
{
    char *csvContent[255];
    for(int i = 0; i < 255; i++)
        csvContent[i] = NULL;

    FILE *file = fopen(_pathCSV.c_str(), "rb");
    if(file == NULL)
    {
        cerr << "Erreur lors de la création de la socket : " << strerror(errno) << endl;
        exit(1);
    }
    else
    {
        char buffer[1024];
        int i = 0;
        while(fgets(buffer, 1024, file))
        {
            csvContent[i] = (char *) malloc(strlen(buffer+1));
            strcpy(csvContent[i], buffer);

            i++;
        }
        fclose(file);
        cout << "Fichier .csv chargé" << endl;
    }
    _content = *csvContent;
}

bool libCSV::CSV::Find(std::string element)
{
    bool find = false;
    char * content;
    for(int i = 0; find == false && _content[i] != NULL && i < 254; i++)
    {
        content = strtok(&_content[i], ";");
        string compare(content);
        if(compare + "#" == element)
            find = true;
    }

    return find;
}
