//
// Created by Doublon on 28/07/2018.
//
#include <gtest/gtest.h>
#include "SocketUtilities.h"

using namespace ServeurCheckIn;

class SocketUtilitiesTests : public testing::Test
{
    public:
        SocketUtilitiesTests() : _socket(50000)
        {
        }
    protected:
        SocketUtilities _socket;

};

TEST_F(SocketUtilitiesTests, InitSocket_SocketIsInitialize)
{
    _socket . Init();

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
    _socket.PrepareSockAddrIn();

    ASSERT_EQ(htons(50000), _socket.SocketAddress().sin_port);
}