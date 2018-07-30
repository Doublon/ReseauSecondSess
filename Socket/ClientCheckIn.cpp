//
// Created by Doublon on 30/07/2018.
//

#include "SocketClient.h"
#include "Config.h"

using namespace std;
using namespace libConfig;
using namespace ServeurCheckIn;

int main(int argc, char* argv[])
{
    SocketClient socket;
    string ipServer, port;
    Config config("D:\\GitHub\\ReseauSecondSess\\Socket\\ServeurCheckIn.txt");

    ipServer = config.GetValue("SERVER_IP");
    port = config.GetValue("CHECKIN_PORT");

    socket.InitSocket();
    socket.GetInfoHost(ipServer);
    socket.PrepareSockAddrIn(stoi(port));
    socket.Connect(socket.SocketAddress());
}