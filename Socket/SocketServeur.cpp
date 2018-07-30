//
// Created by Doublon on 30/07/2018.
//

#include "SocketServeur.h"

using namespace std;
using namespace ServeurCheckIn;


void SocketServeur::Bind()
{
    if( bind(_hListeningSocket, (struct sockaddr *)&_socketAddress, sizeof(struct sockaddr_in)) == -1 )
    {
        cerr << "Erreur de bind : " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
        cout << "bind() ok !" << endl;
}

void SocketServeur::Listen()
{
    if(listen(_hListeningSocket,SOMAXCONN) == -1)
    {
        cerr << "Erreur d'écoute : " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
        cout << "listen() ok !" << endl;
}

void SocketServeur::Accept(struct sockaddr_in clientAddress)
{
    int structSize = sizeof(struct sockaddr_in);

    cout << "En attende de connexion" << endl;

    if(_hSocketServeur = accept(_hListeningSocket,(struct sockaddr*)&clientAddress, &structSize) == -1)
    {
        cerr << "Erreur d'accept : " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
        cout << "accept() ok !" << endl;
}

void SocketServeur::CloseConnexion()
{
    close(_hListeningSocket);
    cout << "Socket d'écoute fermée" << endl;

    close (_hSocketServeur);
    cout << "Socket de service fermée" << endl;
}

int SocketServeur::hSocketServeur() const
{
    return _hSocketServeur;
}
