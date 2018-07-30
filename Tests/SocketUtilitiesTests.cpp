//
// Created by Doublon on 28/07/2018.
//
#include <gtest/gtest.h>
#include "Socket/SocketUtilities.h"


using namespace ServeurCheckIn;

class SocketUtilitiesTests : public testing::Test
{
    public:
        SocketUtilitiesTests()
        {
        }
    protected:
        SocketUtilities _socket;

};

void SendMessage(int hSocket, char *Msg)
{
    char MsgSocket[100] = "";
    send(hSocket, MsgSocket, strlen(MsgSocket), 0);
}

void OpenConnexion(int hSocket, struct sockaddr_in adresseSocket)
{
    unsigned int tailleSockaddr_in = sizeof(struct sockaddr_in);
    connect(hSocket, (struct sockaddr *)&adresseSocket, tailleSockaddr_in);
}

TEST_F(SocketUtilitiesTests, GetterStaticVariable_RightValue)
{
    ASSERT_EQ("127.0.0.1", _socket.IpAddressHost());
    ASSERT_EQ(8081, _socket.Port());
}

TEST_F(SocketUtilitiesTests, InitSocket_SocketIsInitialize)
{
    _socket.InitSocket();

    ASSERT_NE(-1, _socket . ListenningSocket());
}

TEST_F(SocketUtilitiesTests, GetInfoHost_InfoIsTaked)
{
    _socket.GetInfoHost("127.0.0.1");

    ASSERT_NE(nullptr, _socket.InfoHost());
}

TEST_F(SocketUtilitiesTests, PrepareSocket_SocketIsReady)
{
    _socket.GetInfoHost("127.0.0.1");
    _socket.PrepareSockAddrIn(8080);

    ASSERT_EQ(htons(8080), _socket.SocketAddress().sin_port);
}

TEST_F(SocketUtilitiesTests, SocketBind_SocketIsBinded)
{
    _socket.InitSocket();
    _socket.GetInfoHost("127.0.0.1");
    _socket.PrepareSockAddrIn(50000);
    _socket . Bind();

    ASSERT_EQ(1, 1);
}

TEST_F(SocketUtilitiesTests, SocketListen_SocketLisnening)
{
    _socket.InitSocket();
    _socket.GetInfoHost("127.0.0.1");
    _socket.PrepareSockAddrIn(50000);
    _socket . Bind();
    _socket.Listen();

    ASSERT_EQ(1, 1);
}

//TEST_F(SocketUtilitiesTests, SocketConnect_SocketConnected)
//{
//    _socket.InitSocket();
//    _socket.GetInfoHost("127.0.0.1");
//    _socket.PrepareSockAddrIn(50000);
//    _socket.Bind();
//    _socket.Listen();
//
//    ASSERT_EQ(1, 1);
//}
//
//TEST_F(SocketUtilitiesTests, SocketAccept_ConnexionAccepted)
//{
//    _socket.InitSocket();
//    _socket.GetInfoHost("127.0.0.1");
//    _socket.PrepareSockAddrIn(50000);
//    _socket.Bind();
//    _socket.Listen();
//
//    OpenConnexion(_socket . ListenningSocket(), _socket.SocketAddress());
//    _socket.Accept();
//
//    ASSERT_NE(-1, _socket.ServiceSocket());
//}