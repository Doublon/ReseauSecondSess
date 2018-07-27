//
// Created by Doublon on 27/07/2018.
//

#ifndef PROJECT_SOCKET_H
#define PROJECT_SOCKET_H


#include <sys/socket.h>
#include <netinet/in.h>

namespace Serveur
{
class Socket
{
    public:
        Socket(int domaine, int type, int protocole);

        int Domaine();
        int Type();
        int Protocole();

    private:
        int _domaine;
        int _type;
        int _protocole;
};
}


#endif //PROJECT_SOCKET_H
