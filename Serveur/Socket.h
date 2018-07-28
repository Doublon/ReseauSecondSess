//
// Created by Doublon on 27/07/2018.
//

#ifndef PROJECT_SOCKET_H
#define PROJECT_SOCKET_H


#include <sys/socket.h>
#include <netinet/in.h>

namespace ServeurCheckIn
{
class Socket
{
    public:
        Socket(int domain, int type, int protocol);

        int InitSocket();
        int Domain();
        int Type();
        int Protocol();

    private:
        int _domain;
        int _type;
        int _protocol;
};
}


#endif //PROJECT_SOCKET_H
