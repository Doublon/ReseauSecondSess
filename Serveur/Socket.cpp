//
// Created by Doublon on 27/07/2018.
//

#include "Socket.h"

using namespace Serveur;

Socket::Socket(int domaine, int type, int protocole) :
        _domaine(domaine), _type(type), _protocole(protocole)
{
}

int Socket::Domaine()
{
    return _domaine;
}

int Socket::Type()
{
    return _type;
}

int Socket::Protocole()
{
    return _protocole;
}
