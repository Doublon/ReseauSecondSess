//
// Created by Doublon on 28/07/2018.
//
#include <gtest/gtest.h>
#include "Serveur/Serveur.h"


using namespace ServeurCheckIn;

class ServeurTests : public testing::Test
{
    public:
        ServeurTests() : _socket(AF_INET, SOCK_RAW, IPPROTO_IP),
                         _serveur(_socket)
        {
        }
    protected:
        Serveur _serveur;
        Socket _socket;
};

TEST_F(ServeurTests, InitSocket_SocketIsInitialize)
{
    _serveur.InitSocket();

    ASSERT_NE(-1, _serveur.hSocket());
}

TEST_F(ServeurTests, GetInfoHost_InfoIsTaked)
{
    _serveur.GetInfoHost("127.0.0.1");

    ASSERT_NE(nullptr, _serveur.InfoHost());
}

