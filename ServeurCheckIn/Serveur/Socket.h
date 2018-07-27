//
// Created by Doublon on 27/07/2018.
//

#ifndef SERVEURCHECKIN_SOCKET_H
#define SERVEURCHECKIN_SOCKET_H

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
#endif //SERVEURCHECKIN_SOCKET_H
