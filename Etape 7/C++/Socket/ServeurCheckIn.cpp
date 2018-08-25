//
// Created by Doublon on 27/07/2018.
//
#include <fstream>
#include "SocketThreadServer.h"
#include "Config.h"

using namespace std;
using namespace libConfig;
using namespace ServeurCheckIn;

int main(int argc, char* argv[])
{
    string ipServer, port;
    Config config("D:\\GitHub\\ReseauSecondSess\\Etape 7\\C++\\Socket\\ServeurCheckIn.txt");

    ipServer = config.GetValue("SERVER_IP");
    port = config.GetValue("CHECKIN_PORT");

    SocketThreadServer socket(stoi(port));

    socket.Start(ipServer);
}