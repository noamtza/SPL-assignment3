CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: BGSclient
	g++ -o bin/BGSclient bin/connectionHandler.o bin/echoClient.o bin/encoderDecoder.o $(LDFLAGS)

BGSclient: bin/connectionHandler.o bin/echoClient.o bin/encoderDecoder.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/encoderDecoder.o: src/encoderDecoder.cpp
	g++ $(CFLAGS) -o bin/encoderDecoder.o src/encoderDecoder.cpp

.PHONY: clean
clean:
	rm -f bin/*