//
// Created by Doublon on 30/07/2018.
//

#include "SocketClient.h"
#include "Config.h"
#include "RequestCIMP.h"

using namespace std;
using namespace libConfig;
using namespace ServeurCheckIn;

int main(int argc, char* argv[])
{
    Config config("D:\\GitHub\\ReseauSecondSess\\Etape 7\\C++\\Socket\\ServeurCheckIn.txt");

    string ipServer = config.GetValue("SERVER_IP");
    string port = config.GetValue("CHECKIN_PORT");
    string separator = config.GetValue("TRAME_SEPERATOR");
    string trameEnd = config.GetValue("TRAME_END");
    SocketClient socket(stoi(port));

    socket.Init();
    socket.GetInfoHost(ipServer);
    socket.PrepareSockAddrIn();
    socket.Connect(socket.SocketAddress());

    RequestCIMP request(separator, trameEnd);
    socket.SendMessage(request.CreateLoginRequest("tusset", "123"));
}