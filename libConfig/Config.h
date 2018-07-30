//
// Created by Doublon on 30/07/2018.
//

#ifndef CHECKIN_CONFIG_H
#define CHECKIN_CONFIG_H

#include <string>
#include <iostream>

namespace libConfig
{
class Config
{
    public:
        Config(std::string nameFile);
        std::string GetValue(std::string key);

    private:
        std::string _nameFile;
};
}

#endif //CHECKIN_CONFIG_H
