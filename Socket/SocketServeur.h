//
// Created by Doublon on 30/07/2018.
//

#ifndef CHECKIN_SOCKETSERVEUR_H
#define CHECKIN_SOCKETSERVEUR_H

#include "SocketUtilities.h"

namespace ServeurCheckIn
{
    class SocketServeur : public SocketUtilities
    {
        public:
            void Bind();
            void Listen();
            void Accept(struct sockaddr_in clientAddress);
            void CloseConnexion() override ;

            int hSocketServeur() const;

        private:
            int _hSocketServeur;
    };
}

#endif //CHECKIN_SOCKETSERVEUR_H
