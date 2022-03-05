cmake_minimum_required(VERSION 3.20)
project(Boost_Echo_Client)

set(CMAKE_CXX_STANDARD 14)

add_executable(Boost_Echo_Client ./src/connectionHandler.cpp ./src/encoderDecoder.cpp ./src/echoClient.cpp)
