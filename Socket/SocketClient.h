//
// Created by Doublon on 30/07/2018.
//

#ifndef CHECKIN_SOCKETCLIENT_H
#define CHECKIN_SOCKETCLIENT_H

#include "SocketUtilities.h"

namespace ServeurCheckIn
{

class SocketClient : public SocketUtilities
{
    public:
        void Connect(struct sockaddr_in serveurAddress);
};
}

#endif //CHECKIN_SOCKETCLIENT_H
