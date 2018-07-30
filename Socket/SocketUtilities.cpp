//
// Created by Doublon on 28/07/2018.
//

#include "SocketUtilities.h"

using namespace std;
using namespace ServeurCheckIn;

SocketUtilities::~SocketUtilities()
{
    CloseConnexion();
}

void SocketUtilities::InitSocket()
{
    if( (_hListeningSocket = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        cerr << "Erreur lors de la création de la socket : " << strerror(errno) << endl;
        exit(1);
    }
    else
        cout << "Socket créée" << endl;
}

void SocketUtilities::GetInfoHost(string IP)
{
    if((_infoHost = gethostbyname(IP.c_str())) == 0)
    {
        cerr << "Erreur de recuperation des informations host : " << strerror(errno) << endl;
        exit(1);
    }
    else
    {
        memcpy(&_ipAddress, _infoHost->h_addr, _infoHost->h_length);
        cout << "Information host récupérée : " <<  endl;
    }
}

void SocketUtilities::PrepareSockAddrIn(int port)
{
    memset(&_socketAddress,0, sizeof(struct sockaddr_in));
    _socketAddress.sin_family = AF_INET;
    _socketAddress.sin_port = htons(port);

    memcpy(&_socketAddress.sin_addr, _infoHost->h_addr, _infoHost->h_length);
}

void SocketUtilities::CloseConnexion()
{
    close(_hListeningSocket);
    cout << "Socket d'écoute fermée" << endl;
}

int SocketUtilities::ListenningSocket() const
{
    return _hListeningSocket;
}

struct hostent *SocketUtilities::InfoHost() const
{
    return _infoHost;
}

struct sockaddr_in SocketUtilities::SocketAddress() const
{
    return _socketAddress;
}