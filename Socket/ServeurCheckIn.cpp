//
// Created by Doublon on 27/07/2018.
//
#include "SocketUtilities.h"

using namespace std;
using namespace ServeurCheckIn;

int main(int argc, char* argv[])
{
    SocketUtilities socket;

    socket.InitSocket();
    socket.GetInfoHost("127.0.0.1");
    socket.PrepareSockAddrIn(50000);
    socket.Bind();
    socket.Listen();
    socket.Accept(socket.SocketAddress());
}