//
// Created by Doublon on 30/07/2018.
//

#include "SocketClient.h"

using namespace std;
using namespace ServeurCheckIn;

SocketClient::SocketClient(int port) : SocketUtilities(port)
{
}

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

void SocketClient::SendMessage(std::string message)
{
    if(send(_hListeningSocket, message.c_str(), 1024, 0) == -1)
    {
        cerr << "Erreur lors de l'envoie du message " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
        cout << "Envoie du message : " << message << endl;
}
