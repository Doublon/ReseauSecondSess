//
// Created by Doublon on 27/07/2018.
//

#include "Socket.h"

using namespace ServeurCheckIn;

Socket::Socket(int domain, int type, int protocol) :
        _domain(domain), _type(type), _protocol(protocol)
{
}

int Socket::Domain()
{
    return _domain;
}

int Socket::Type()
{
    return _type;
}

int Socket::Protocol()
{
    return _protocol;
}

int Socket::InitSocket()
{
    return socket(_domain, _type, _protocol);
}
