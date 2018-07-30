//
// Created by Doublon on 30/07/2018.
//

#include <cstring>
#include "Config.h"

using namespace std;
using namespace libConfig;

Config::Config(std::string nameFile) : _nameFile(nameFile)
{
}

std::string Config::GetValue(string key)
{
    int find = 0;
    string token;
    char buffer[512];
    FILE *file = nullptr;

    file = fopen(_nameFile.c_str(), "rb");
    if(file == nullptr)
        cerr << "impossible d'ouvrir le fichier : " << strerror(errno) << endl;
    else
    {
        while (!find && fgets(buffer, 512, file) != nullptr)
        {
            string temp(buffer);

            if(temp.substr(0, temp.find("=")) == key)
            {
                find = 1;
                token = temp.erase(0, temp.find("=") + 1);
                token = token.substr(0, token.find("\r"));
            }
        }
        fclose(file);

        if(find)
            return token;
        else
            return nullptr;
    }
}
