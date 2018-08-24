//
// Created by Doublon on 31/07/2018.
//

#ifndef CHECKIN_SOCKETTHREADSERVER_H
#define CHECKIN_SOCKETTHREADSERVER_H
#define MAX_CLIENT 3

#include <w32api/minwindef.h>
#include "SocketUtilities.h"

static int _currentIndex = -1;
static int _hSocketConnected[MAX_CLIENT];

namespace ServeurCheckIn
{

class SocketThreadServer : public SocketUtilities
{
    public:
        SocketThreadServer(int port);

        int Start(struct sockaddr_in clientAddress);

        void Init() override;

        void Bind();
        void Listen();
        void Accept(struct sockaddr_in clientAddress);
        void FindFreeSocket();
        std::string ReceiveMessage(int socketTraited);
        void CloseConnexion() override ;

        void LaunchThread();
        void * Thread(int *param);

    private:
        int _hSocketDuplicated;

        pthread_t _threadHandle[MAX_CLIENT];
        pthread_mutex_t _mutexCurrentIndex;
        pthread_cond_t _condCurrentIndex;
};
}

#endif //CHECKIN_SOCKETTHREADSERVER_H
