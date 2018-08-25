//
// Created by Doublon on 31/07/2018.
//

#include <w32api/ntdef.h>
#include "SocketThreadServer.h"
#include "RequestCIMP.h"
#include "Config.h"
#include "States.h"

using namespace std;
using namespace ServeurCheckIn;
using namespace libConfig;
using namespace CIMP;


SocketThreadServer::SocketThreadServer(int port) : SocketUtilities(port)
{
}

void SocketThreadServer::Init()
{
    pthread_mutex_init(&_mutexCurrentIndex, nullptr);
    pthread_cond_init(&_condCurrentIndex, nullptr);

    for(int i = 0; i < MAX_CLIENT; i++)
        _hSocketConnected[i] = -1;

    if( (_hListeningSocket = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        cerr << "Erreur lors de la création de la socket : " << strerror(errno) << endl;
        exit(1);
    }
    else
        cout << "Socket créée" << endl;
}

int SocketThreadServer::Start(std::string ipAddress)
{
    Init();
    GetInfoHost(ipAddress);
    PrepareSockAddrIn();

    Bind();
    LaunchThread();
    do
    {
        Listen();
        Accept(_socketAddress);
        FindFreeSocket();
    }while(1);
}


void SocketThreadServer::Bind()
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

void SocketThreadServer::Listen()
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

void SocketThreadServer::Accept(struct sockaddr_in clientAddress)
{
    int structSize = sizeof(struct sockaddr_in);

    cout << "En attende de connexion" << endl;

    _hSocketDuplicated = accept(_hListeningSocket,(struct sockaddr*)&clientAddress, &structSize);
    if(_hSocketDuplicated == -1)
    {
        cerr << "Erreur d'accept : " << strerror(errno) << endl;
        CloseConnexion();
        exit(1);
    }
    else
    {
        cout << "accept() ok ! " << endl;
    }
}

void SocketThreadServer::FindFreeSocket()
{
    int i;
    for (i = 0; i < MAX_CLIENT && _hSocketConnected[i] != -1; i++);

    if(i == MAX_CLIENT)
    {
        //TODO : ENVOYER UN MESSAGE AU CLIENT ?
        cout << "Toutes les connexions sont occupées" << endl;
        CloseConnexion();
    }
    else
    {
        cout << "Connexion : " << i << " libre !" << endl;
        pthread_mutex_lock(&_mutexCurrentIndex);
        _hSocketConnected[i] = _hSocketDuplicated;
        _currentIndex = i;
        pthread_mutex_unlock(&_mutexCurrentIndex);
        pthread_cond_signal(&_condCurrentIndex);
    }
}

void SocketThreadServer::CloseConnexion()
{
    close(_hListeningSocket);
    cout << "Socket d'écoute fermée" << endl;

//TODO : BOUCLE
//    close (_hSocketService);
//    cout << "Socket de service fermée" << endl;
}

void SocketThreadServer::LaunchThread()
{
    for(int i = 0; i < MAX_CLIENT; i++)
    {
        pthread_create(&_threadHandle[i], nullptr, (void*(*)(void*))&Thread, &i);
        cout << "Lancement du thread : " << i << endl;
        pthread_detach(_threadHandle[i]);
    }
}

void *SocketThreadServer::Thread(int * param)
{
    int indexTreadedClient;
    int hSocketServer;

    do
    {
        pthread_mutex_lock(&_mutexCurrentIndex);
        while(_currentIndex == -1)
            pthread_cond_wait(&_condCurrentIndex, &_mutexCurrentIndex);

        indexTreadedClient = _currentIndex;
        _currentIndex = -1;
        hSocketServer = _hSocketConnected[indexTreadedClient];
        pthread_mutex_unlock(&_mutexCurrentIndex);

        string message = ReceiveMessage(hSocketServer);

        pthread_mutex_lock(&_mutexCurrentIndex);
        _hSocketConnected[indexTreadedClient] = -1;
        pthread_mutex_unlock(&_mutexCurrentIndex);

        int state = AnalyzeRequest(message);
        SendResponse(state);

        if(!message.compare(""))
            break;
    }while(1);

    pthread_exit(param);
}

int SocketThreadServer::AnalyzeRequest(std::string requestString)
{
    Config config("D:\\GitHub\\ReseauSecondSess\\Etape 7\\C++\\Socket\\ServeurCheckIn.txt");
    string separator = config.GetValue("TRAME_SEPERATOR");
    string trameEnd = config.GetValue("TRAME_END");

    RequestCIMP request(separator, trameEnd);
    int ret = request.AnalyseRequest((char*)requestString.c_str());
    return ret;
}

void SocketThreadServer::SendResponse(int state)
{
    switch(state)
    {
        case States::CONNECTED :
            cout << "Envoie du CONNECTED " <<  endl;
            SendMessage(_hSocketDuplicated, "CONNECTED");
            break;
    }
}
