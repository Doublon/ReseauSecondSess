//
// Created by Doublon on 28/07/2018.
//

#include "Serveur.h"

using namespace std;
using namespace ServeurCheckIn;

Serveur::Serveur(const Socket &socket) : _socket(socket)
{
}

void Serveur::InitSocket()
{
    _hSocket = _socket.InitSocket();
}

void Serveur::GetInfoHost(string IP)
{
    if((_infoHost = gethostbyname(IP.c_str())) == 0)
        exit(1);
    else
      memcpy(&_ipAddress, _infoHost->h_addr, sizeof(_infoHost->h_length));
}

int Serveur::hSocket() const
{
    return _hSocket;
}

struct hostent *Serveur::InfoHost() const
{
    return _infoHost;
}

