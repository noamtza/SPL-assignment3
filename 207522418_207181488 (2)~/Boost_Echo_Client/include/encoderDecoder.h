//
// Created by 97252 on 12/29/2021.
//

#ifndef UNTITLED1_ENCODERDECODER_H
#define UNTITLED1_ENCODERDECODER_H


#include "connectionHandler.h"
using namespace std;
class encoderDecoder {
public:
    encoderDecoder(ConnectionHandler &handler);
    void encoder () ;
    void decoder();
    string getTime();
    short getOpcode(std::string);
    void shortToBytes(short num, char* bytesArr);
    short bytesToShort(char* bytesArr);
    std::string getMessage(short op);
    bool getTerminate();
    ConnectionHandler& handler;
    void impDecoder();
     std::string decode(string &answer);
    std::vector<string> split(const string &str,char delimiter);
private:
    bool terminate;
};


#endif //UNTITLED1_ENCODERDECODER_H