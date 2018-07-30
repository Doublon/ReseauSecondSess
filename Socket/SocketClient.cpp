//
// Created by Doublon on 30/07/2018.
//

#include "SocketClient.h"

using namespace std;
using namespace ServeurCheckIn;

void SocketClient::Connect(struct sockaddr_in serveurAddress)
{
    unsigned int sizeSockaddr_in = sizeof(struct sockaddr_in);

    cout << "Tentative de connexion" << endl;

    if( connect(_hListeningSocket, (struct sockaddr *)&serveurAddress, sizeSockaddr_in) == -1)
    {
        cerr << "Erreur de connect : " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
        cout << "connect() ok !" << endl;
}
