//
// Created by Doublon on 27/07/2018.
//

#include <gtest/gtest.h>
#include "../Serveur/Socket.h"


using namespace Serveur;

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
ASSERT_EQ(1, 1);
}