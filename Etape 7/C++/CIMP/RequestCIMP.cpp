//
// Created by Tusse on 25/08/2018.
//

#include <cstring>
#include <iostream>
#include "RequestCIMP.h"
#include "CSV.h"
#include "States.h"

using namespace std;
using namespace libCSV;
using namespace CIMP;

RequestCIMP::RequestCIMP(string separator, string endTrame) : _separator(separator), _endTrame(endTrame)
{
}

char* RequestCIMP::CreateLoginRequest(std::string user, std::string password)
{
    string requestConstruction = to_string(LOGIN_OFFICER)
            + _separator
            + user
            + _separator
            + password
            + _endTrame;

    return (char*) requestConstruction.c_str();
}

int RequestCIMP::AnalyseRequest(char *request)
{
    char *ptr, *token;

    std::cout << "Analyse de la requete : " << request << endl;

    token = strtok_r(request, _separator.c_str(), &ptr);

    switch(atoi(token))
    {
        case LOGIN_OFFICER :
            string user(strtok_r(nullptr, _separator.c_str(), &ptr));
            string password(strtok_r(nullptr, _endTrame.c_str(), &ptr));
            return ProcessLoginRequest(user, password);
        break;
    }
}

int RequestCIMP::ProcessLoginRequest(std::string user, std::string password)
{
    char *login, *pwd, *ptr;
    CSV csvFile("D:\\GitHub\\ReseauSecondSess\\Etape 7\\C++\\Socket\\logins.csv");
    char *csvContent = csvFile.ReadCSV();

    int trouve = 0;
    for(int i = 0; trouve == 0 && csvContent[i] != NULL && i < 244; i++)
    {
        login = strtok_r(&csvContent[i], ";", &ptr);
        pwd = strtok_r(NULL, "\r", &ptr);

        if(strcmp(login, user.c_str()) == 0 && strcmp(pwd, password.c_str()) == 0)
            trouve = 1;
    }

    if(trouve == 1)
    {
        cout << "Utilisateur trouvé !" << endl;
        return CONNECTED;
    }
    else
    {
        cout << "Utilisateur inconnu !" << endl;
        return DISCONNECTED;
    }
}
