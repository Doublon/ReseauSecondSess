//
// Created by Doublon on 27/07/2018.
//
#include <fstream>
#include "SocketServeur.h"
#include "Config.h"

using namespace std;
using namespace libConfig;
using namespace ServeurCheckIn;

int main(int argc, char* argv[])
{
    string ipServer, port;
    SocketServeur socket;
    Config config("D:\\GitHub\\ReseauSecondSess\\Socket\\ServeurCheckIn.txt");

    ipServer = config.GetValue("SERVER_IP");
    port = config.GetValue("CHECKIN_PORT");

    socket.InitSocket();
    socket.GetInfoHost(ipServer);
    socket.PrepareSockAddrIn(stoi(port));
    socket.Bind();
    socket.Listen();
    socket.Accept(socket.SocketAddress());

    string messageRecieved = socket.ReceiveMessage();
    cout << "message recu : " << messageRecieved << endl;
}