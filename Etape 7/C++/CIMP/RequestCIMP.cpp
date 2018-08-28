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

    if(strcmp(token, "LOGIN_OFFICER") == 0)
    {
        string user(strtok_r(nullptr, _separator.c_str(), &ptr));
        string password(strtok_r(nullptr, _endTrame.c_str(), &ptr));
        return ProcessLoginRequest(user, password);
    }
    else if(strcmp(token, "CHECK_TICKET") == 0)
    {
        string ticket(strtok_r(nullptr, _separator.c_str(), &ptr));
        return ProcessCheckInTicket(ticket);
    }
}

int RequestCIMP::ProcessLoginRequest(std::string user, std::string password)
{
    char *login, *pwd, *ptr;
    CSV csvFile("D:\\GitHub\\ReseauSecondSess\\Etape 7\\C++\\Socket\\logins.csv");
    csvFile.ReadCSV();
    bool trouve = csvFile.Find("test");

    if(trouve == true)
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

int RequestCIMP::ProcessCheckInTicket(std::string ticket)
{
    cout << "Traitement de la requete CHECK_TICKET" << endl;

    CSV csvFile("D:\\GitHub\\ReseauSecondSess\\Etape 7\\C++\\Socket\\tickets.csv");
    csvFile.ReadCSV();
    bool find = csvFile.Find(ticket);

    if(find == true)
    {
        cout << "Ticket trouvé !" << endl;
        return States::TICKET_CHECKED_OK;
    }
    else
    {
        cout << "Ticket non reconnu !" << endl;
        return States::TICKET_CHECKED_OK;
    }
}

