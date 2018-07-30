//
// Created by Doublon on 30/07/2018.
//

#include "SocketClient.h"

using namespace std;
using namespace ServeurCheckIn;

int main(int argc, char* argv[])
{
    SocketClient socket;

    socket.InitSocket();
    socket.GetInfoHost("127.0.0.1");
    socket.PrepareSockAddrIn(50000);
    socket.Connect(socket.SocketAddress());
}