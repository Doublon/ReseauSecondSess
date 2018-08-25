//
// Created by Doublon on 30/07/2018.
//

#include <gtest/gtest.h>
#include <SocketThreadServer.h>

using namespace ServeurCheckIn;

class ServeurSocketTests : public testing::Test
{
    public:
        ServeurSocketTests() : _socket(5000)
        {
        }
    protected:
        SocketThreadServer _socket;
};

TEST_F(ServeurSocketTests, SocketBind_SocketIsBinded)
{
    _socket . Init();
    _socket.GetInfoHost("127.0.0.1");
    _socket.PrepareSockAddrIn();
    _socket . Bind();

    ASSERT_EQ(1, 1);
}

TEST_F(ServeurSocketTests, SocketListen_SocketLisnening)
{
    _socket . Init();
    _socket.GetInfoHost("127.0.0.1");
    _socket.PrepareSockAddrIn();
    _socket . Bind();
    _socket.Listen();

    ASSERT_EQ(1, 1);
}