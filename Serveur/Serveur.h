//
// Created by Doublon on 28/07/2018.
//

#ifndef CHECKIN_SERVEUR_H
#define CHECKIN_SERVEUR_H

#include <iostream>
#include <cstdint>
#include <cstring>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include "Socket.h"

namespace ServeurCheckIn
{
class Serveur
{
    public:
        Serveur(const Socket &socket);

        void InitSocket();
        void GetInfoHost(std::string IP);

        int hSocket() const;
        struct hostent* InfoHost() const;
    private:
        Socket _socket;
        int _hSocket;
        struct hostent *_infoHost;
        struct in_addr _ipAddress;
        struct sockaddr_in _SocketAddress;
};
}

#endif //CHECKIN_SERVEUR_H
