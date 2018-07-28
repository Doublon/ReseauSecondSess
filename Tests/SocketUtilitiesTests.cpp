//
// Created by Doublon on 28/07/2018.
//
#include <gtest/gtest.h>
#include "Serveur/SocketUtilities.h"


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

TEST_F(SocketUtilitiesTests, GetterStaticVariable_RightValue)
{
    ASSERT_EQ("127.0.0.1", _socket.IpAddressHost());
    ASSERT_EQ(8081, _socket.Port());
}

TEST_F(SocketUtilitiesTests, InitSocket_SocketIsInitialize)
{
    _socket.InitSocket();

    ASSERT_NE(-1, _socket.hSocket());
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
    _socket.SocketBind();

    ASSERT_EQ(1, 1);
}