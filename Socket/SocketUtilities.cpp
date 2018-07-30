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
    if( (_listeningSocket = socket(AF_INET, SOCK_STREAM, 0)) == -1)
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
        cout << "Information host récupérée" << endl;
    }
}

void SocketUtilities::PrepareSockAddrIn(int port)
{
    memset(&_socketAddress,0, sizeof(struct sockaddr_in));
    _socketAddress.sin_family = AF_INET;
    _socketAddress.sin_port = htons(port);

    memcpy(&_socketAddress.sin_addr, _infoHost->h_addr, _infoHost->h_length);
}

void SocketUtilities::SocketBind()
{
    if( bind(_listeningSocket, (struct sockaddr *)&_socketAddress, sizeof(struct sockaddr_in)) == -1 )
    {
        cerr << "Erreur de bind : " << strerror(errno) << endl;
        exit(1);
    }
    else
        cout << "bind() ok !" << endl;

}

void SocketUtilities::Listen()
{
    if(listen(_listeningSocket,SOMAXCONN) == -1)
    {
        cerr << "Erreur d'écoute : " << strerror(errno) << endl;
        exit(1);
    }
    else
        cout << "listen() ok !" << endl;
}

void SocketUtilities::Accept(struct sockaddr_in clientAddress)
{
    int structSize = sizeof(struct sockaddr_in);

    if(_serviceSocket = accept(_listeningSocket,(struct sockaddr*)&clientAddress, &structSize) == -1)
    {
        cerr << "Erreur d'accept : " << strerror(errno) << endl;
        exit(1);
    }
    else
        cout << "accept() ok !" << endl;
}

void SocketUtilities::Connect(struct sockaddr_in serveurAddress)
{
    unsigned int sizeSockaddr_in = sizeof(struct sockaddr_in);

    if( connect(_listeningSocket, (struct sockaddr *)&serveurAddress, sizeSockaddr_in) == -1)
    {
        cerr << "Erreur de connect : " << strerror(errno) << endl;
        exit(1);
    }
    else
        cout << "connect() ok !" << endl;
}

void SocketUtilities::CloseConnexion()
{
    close(_listeningSocket);
    cout << "Socket d'écoute fermée" << endl;

    close (_serviceSocket);
    cout << "Socket de service fermée" << endl;
}

int SocketUtilities::ListenningSocket() const
{
    return _listeningSocket;
}

int SocketUtilities::ServiceSocket() const
{
    return _serviceSocket;
}

struct hostent *SocketUtilities::InfoHost() const
{
    return _infoHost;
}

struct sockaddr_in SocketUtilities::SocketAddress() const
{
    return _socketAddress;
}

std::string SocketUtilities::IpAddressHost() const
{
    return _ipAddressHost;
}

int SocketUtilities::Port() const
{
    return _port;
}