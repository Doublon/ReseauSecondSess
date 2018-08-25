//
// Created by Tusse on 25/08/2018.
//

#ifndef CHECKIN_CSV_H
#define CHECKIN_CSV_H

#include <string>

namespace libCSV
{
class CSV
{
public:
    CSV(std::string pathCSV);

    char* ReadCSV();

private:
    std::string _pathCSV;
};
}

#endif //CHECKIN_CSV_H
