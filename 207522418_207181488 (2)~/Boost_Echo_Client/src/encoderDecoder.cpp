#include <string>
#include <vector>
#include <zconf.h>
#include <iostream>
#include "../include/encoderDecoder.h"



void encoderDecoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}
void encoderDecoder::encoder() {
    while(!terminate){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        std::string opStr = "";
        for (unsigned int i = 0; i < line.length() && line[i] != ' '; i++) {
            opStr = opStr + line[i];
        }
        short Opcode = getOpcode(opStr);
       if(line.length()>opStr.length()+1){
            line=line.substr(opStr.length()+1);}//}
        line=std::to_string(Opcode)+" "+line;//replaced "REGISTER, ..." to the opcode
        if(Opcode==6) {
            std::string dateAndTime = getTime();
            line = line + " " + dateAndTime;
        }
        if(!handler.sendLine(line)){
            terminate=true;
            std::cout << "Disconnecting...\n" << std::endl;
            break;
        }
    }
}

std::string encoderDecoder::getTime() {
    time_t now=time(0);
    tm* dt= localtime(&now);
    int year=1900+dt->tm_year;
    int month=1+dt->tm_mon;
    int day=dt->tm_mday;
    std::string sendTime=std::to_string(day)+"-"+std::to_string(month)+"-"+std::to_string(year);
    return sendTime;
}
void encoderDecoder::impDecoder() {
    while (!terminate) {
        string input;
        if(!handler.getLine(input)){
            terminate = true;
        }
        if(!input.empty()){
            input = decode(input);
        }
    }
}
string encoderDecoder::decode(string &input) {
    std::string toPrint = "";
    vector<string> serverRead;
    std::stringstream sst(input);
    string str;
    while (getline(sst, str, ' ')) {
        serverRead.push_back(str);
    }
    if(serverRead.size()==2){
        serverRead[1]=serverRead[1].substr(0,serverRead[1].length()-1);
    }
    short result= stoi(serverRead[0]);
    toPrint = toPrint + getMessage(result);
    short opCode=stoi(serverRead[1]);
    // short opCode = stoi(serverRead[1]);

    if (result != 9) {
        toPrint = toPrint + " " + serverRead[1];
    }
    if (result==10&&opCode == 4) {
        toPrint = toPrint + " " + serverRead[2];//userName
        //complete 0 1
    }
    if ((opCode == 7 )| (opCode == 8)) {
        for (unsigned int i = 2; i < serverRead.size(); i++) {
            toPrint = toPrint + " " + serverRead[i];
        }
    }

    if (result ==9) {
        if (opCode ==0) {
            toPrint = toPrint + " PM";
        } else
            toPrint = toPrint + " PUBLIC";

        for (unsigned int i = 2; i < serverRead.size(); i++) {
            toPrint = toPrint + " " + serverRead[i];

        }
    }
    std::cout <<toPrint << std::endl;
    return toPrint;
}


short bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}


short encoderDecoder::getOpcode(std::string str){
    if(str=="REGISTER")
        return 1;
    if(str=="LOGIN")
        return 2;
    if(str=="LOGOUT")
        return 3;
    if(str=="FOLLOW")
        return 4;
    if(str=="POST")
        return 5;
    if(str=="PM")
        return 6;
    if(str=="LOGSTAT")
        return 7;
    if(str=="STAT")
        return 8;
    if(str=="NOTIFICATION")
        return 9;
    if(str=="BLOCK")
        return 12;
    else {
        return -1;
    }
}
std::string encoderDecoder:: getMessage(short op){
    if(op==10)
        return "ACK";
    if(op==11)
        return "ERROR";
    if (op==9)
        return "NOTIFICATION";
    else
        return "nothing";
}

encoderDecoder::encoderDecoder(ConnectionHandler &handler):handler(handler), terminate(false){}

bool encoderDecoder::getTerminate() {
    return terminate;
}

short encoderDecoder::bytesToShort(char *bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}


