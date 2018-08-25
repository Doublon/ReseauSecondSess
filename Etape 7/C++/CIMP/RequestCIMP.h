//
// Created by Tusse on 25/08/2018.
//

#ifndef CHECKIN_REQUESTCIMP_H
#define CHECKIN_REQUESTCIMP_H


#include <string>

class RequestCIMP
{
public:

    static const int LOGOUT_OFFICER = 0;
    static const int LOGIN_OFFICER = 1;
    static const int CHECK_TICKET = 1;
    static const int CHECK_LUGGAGE = 1;
    static const int PAYMENT_DONE = 1;

    RequestCIMP(std::string separator, std::string endTrame);

    char* CreateLoginRequest(std::string user, std::string password);

    int AnalyseRequest(char* request);
    int ProcessLoginRequest(std::string user, std::string password);

private:
    std::string _separator;
    std::string _endTrame;

};


#endif //CHECKIN_REQUESTCIMP_H
