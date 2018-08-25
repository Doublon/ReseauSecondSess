//
// Created by Doublon on 28/07/2018.
//

#include "SocketUtilities.h"

using namespace std;
using namespace ServeurCheckIn;

SocketUtilities::SocketUtilities(int port) : _port(port)
{
}

SocketUtilities::~SocketUtilities()
{
    CloseConnexion();
}

void SocketUtilities::Init()
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

void SocketUtilities::PrepareSockAddrIn()
{
    memset(&_socketAddress,0, sizeof(struct sockaddr_in));
    _socketAddress.sin_family = AF_INET;
    _socketAddress.sin_port = htons(_port);

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

void SocketUtilities::SendMessage(int hSocket, std::string message)
{
    if(send(hSocket, message.c_str(), 1024, 0) == -1)
    {
        cerr << "Erreur lors de l'envoie du message " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
        cout << "Envoie du message : " << message << endl;
}

std::string SocketUtilities::ReceiveMessage(int socketTraited)
{
    char messageReceived[1024];

    //TODO : BOUCLE DE LECTURE
    if(recv(socketTraited, messageReceived, 1024, 0) == -1)
    {
        cerr << "Erreur de reception : " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
        //TODO : CLIENT PARTI;
    else
    {
        string message(messageReceived);
        cout << "message recu : " << message << endl;
        return  message;
    }
}

int SocketUtilities::getHListeningSocket() const
{
    return _hListeningSocket;
}
