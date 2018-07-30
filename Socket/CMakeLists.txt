cmake_minimum_required(VERSION 3.10)
project(SocketUtilities)

include_directories(../libConfig)

add_library(SocketUtilities SocketUtilities.cpp SocketUtilities.h SocketServeur.cpp SocketServeur.h SocketClient.h SocketClient.cpp)

add_executable(ServeurCheckIn ServeurCheckIn.cpp)
target_link_libraries(ServeurCheckIn SocketUtilities libConfig)

add_executable(ClientCheckIn ClientCheckIn.cpp)
target_link_libraries(ClientCheckIn SocketUtilities libConfig)