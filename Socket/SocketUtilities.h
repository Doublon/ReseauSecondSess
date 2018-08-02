//
// Created by Doublon on 28/07/2018.
//

#ifndef CHECKIN_SocketUtilities_H
#define CHECKIN_SocketUtilities_H

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>


namespace ServeurCheckIn
{

class SocketUtilities
{
    public:
        SocketUtilities(int port);
        virtual ~SocketUtilities();

        virtual void Init();
        void GetInfoHost(std::string IP);
        void PrepareSockAddrIn();
        virtual void CloseConnexion();

        int ListenningSocket() const;
        struct hostent* InfoHost() const;
        struct sockaddr_in SocketAddress() const;

    protected:
        int _port, _hListeningSocket;
        struct hostent *_infoHost;
        struct in_addr _ipAddress;
        struct sockaddr_in _socketAddress;
};
}

#endif //CHECKIN_SocketUtilities_H
