//
// Created by Tusse on 25/08/2018.
//

#ifndef CHECKIN_REQUESTCIMP_H
#define CHECKIN_REQUESTCIMP_H


#include <string>

namespace CIMP
{
//enum RequestType
//{
//    LOGIN_OFFICER = 1,
//    CHECK_TICKET = 2
//};

class RequestCIMP
{
public:

    static const int LOGOUT_OFFICER = 0;
    static const int LOGIN_OFFICER = 1;
    static const int CHECK_TICKET = 2;
    static const int CHECK_LUGGAGE = 3;
    static const int PAYMENT_DONE = 4;

    RequestCIMP(std::string separator, std::string endTrame);

    char *CreateLoginRequest(std::string user, std::string password);

    int AnalyseRequest(char *request);

    int ProcessLoginRequest(std::string user, std::string password);
    int ProcessCheckInTicket(std::string ticket);

private:
    std::string _separator;
    std::string _endTrame;

};
}


#endif //CHECKIN_REQUESTCIMP_H
