cmake_minimum_required(VERSION 3.10)
project(SocketUtilities)

find_package(Threads REQUIRED)

include_directories(../libConfig)
include_directories(../libCSV)
include_directories(../CIMP)


add_library(SocketUtilities SocketUtilities.cpp SocketUtilities.h
        SocketClient.h SocketClient.cpp SocketThreadServer.h SocketThreadServer.cpp)

add_executable(ServeurCheckIn ServeurCheckIn.cpp)
target_link_libraries(ServeurCheckIn SocketUtilities libConfig libCSV CIMP)

add_executable(ClientCheckIn ClientCheckIn.cpp)
target_link_libraries(ClientCheckIn SocketUtilities libConfig libCSV CIMP)