//
// Created by Doublon on 31/07/2018.
//

#ifndef CHECKIN_SOCKETTHREADSERVER_H
#define CHECKIN_SOCKETTHREADSERVER_H
#define MAX_CLIENT 5

#include <w32api/minwindef.h>
#include "SocketUtilities.h"

static int _currentIndex = -1;
static int _hSocketConnected[MAX_CLIENT];
static int _hSocketDuplicated;

namespace ServeurCheckIn
{

class SocketThreadServer : public SocketUtilities
{
    public:
        SocketThreadServer(int port);

        int Start(std::string ipAddress);

        void Init() override;

        void Bind();
        void Listen();
        void Accept(struct sockaddr_in clientAddress);
        void FindFreeSocket();
        void CloseConnexion() override ;

        void LaunchThread();
        void * Thread(int *param);

    private:
        pthread_t _threadHandle[MAX_CLIENT];
        pthread_mutex_t _mutexCurrentIndex;
        pthread_cond_t _condCurrentIndex;

        int AnalyzeRequest(std::string requestString);
        void SendResponse(int state);
};
}

#endif //CHECKIN_SOCKETTHREADSERVER_H
