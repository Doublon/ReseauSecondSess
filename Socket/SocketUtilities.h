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

static std::string _ipAddressHost = "127.0.0.1";
static int _port = 8081;

class SocketUtilities
{
    public:
        virtual ~SocketUtilities();

        void InitSocket();
        void GetInfoHost(std::string IP);
        void PrepareSockAddrIn(int port);
        void Bind();
        void Listen();
        void Accept(struct sockaddr_in clientAddress);
        void Connect(struct sockaddr_in serveurAddress);
        void CloseConnexion();

        int ListenningSocket() const;
        int ServiceSocket() const;
        struct hostent* InfoHost() const;
        struct sockaddr_in SocketAddress() const;
        std::string IpAddressHost() const;
        int Port() const;

    private:
        int _listeningSocket, _serviceSocket;
        struct hostent *_infoHost;
        struct in_addr _ipAddress;
        struct sockaddr_in _socketAddress;
};
}

#endif //CHECKIN_SocketUtilities_H
