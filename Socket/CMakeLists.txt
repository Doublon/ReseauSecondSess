cmake_minimum_required(VERSION 3.10)
project(ServeurCheckIn)

include_directories(../libCSV)
add_library(SocketUtilities SocketUtilities.cpp SocketUtilities.h)
add_executable(Socket main.cpp)
target_link_libraries(Socket SocketUtilities)