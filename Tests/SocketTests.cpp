//
// Created by Doublon on 27/07/2018.
//

#include <gtest/gtest.h>
#include "Serveur/Socket.h"


using namespace ServeurCheckIn;

class SocketTests : public testing::Test
{
    public:
        SocketTests() : _socket(AF_INET, SOCK_RAW, IPPROTO_IP)
        {
        }
    protected:
        Socket _socket;
};

TEST_F(SocketTests, CreateSocket_SocketIsCreated)
{
    ASSERT_EQ(AF_INET, _socket.Domain());
    ASSERT_EQ(SOCK_RAW, _socket.Type());
    ASSERT_EQ(IPPROTO_IP, _socket.Protocol());
}

TEST_F(SocketTests, InitSocket_SocketIsInitialize)
{
    int hsocket = _socket.InitSocket();

    ASSERT_NE(-1, hsocket);
}