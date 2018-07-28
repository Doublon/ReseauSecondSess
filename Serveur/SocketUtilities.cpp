//
// Created by Doublon on 28/07/2018.
//

#include "SocketUtilities.h"

using namespace std;
using namespace ServeurCheckIn;

SocketUtilities::~SocketUtilities()
{
}

void SocketUtilities::InitSocket()
{
    if( (_hSocket = socket(AF_INET, SOCK_STREAM, 0)) == -1)
        exit(0);
}

void SocketUtilities::GetInfoHost(string IP)
{
    if((_infoHost = gethostbyname(IP.c_str())) == 0)
        exit(1);
    else
      memcpy(&_ipAddress, _infoHost->h_addr, _infoHost->h_length);
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
    if( bind(_hSocket, (struct sockaddr *)&_socketAddress, sizeof(struct sockaddr_in)) == -1 )
    {
        //cerr << "Erreur de bind : " << errno << endl;
        perror("error bind ");
        exit(1);
    }

    else
        cout << "bind ok" << endl;

}


int SocketUtilities::hSocket() const
{
    return _hSocket;
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
