cmake_minimum_required(VERSION 3.10)
project(ServeurCheckIn)

include_directories(../libCSV)
add_library(ServeurCheckIn SocketUtilities.cpp SocketUtilities.h)
add_executable(Serveur main.cpp)
target_link_libraries(Serveur ServeurCheckIn)