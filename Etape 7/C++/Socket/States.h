//
// Created by Tusse on 25/08/2018.
//

#ifndef CHECKIN_STATES_H
#define CHECKIN_STATES_H

enum States
{
    CONNECTION_CLOSE = -1,
    DISCONNECTED = 0,
    CONNECTED = 1,
    TICKET_CHECKED = 2,
    LUGGAGES_CHECKED = 3,
    LUGGAGES_DATA_SAVING = 4
};

#endif //CHECKIN_STATES_H
