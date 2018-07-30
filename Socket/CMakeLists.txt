cmake_minimum_required(VERSION 3.10)
project(SocketUtilities)

include_directories(../libCSV)
add_library(SocketUtilities SocketUtilities.cpp SocketUtilities.h)

add_executable(ServeurCheckIn ServeurCheckIn.cpp)
target_link_libraries(ServeurCheckIn SocketUtilities)

add_executable(ClientCheckIn ClientCheckIn.cpp)
target_link_libraries(ClientCheckIn SocketUtilities)