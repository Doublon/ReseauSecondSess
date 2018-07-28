cmake_minimum_required(VERSION 3.10)
project(ServeurCheckIn)

include_directories(../libCSV)
add_library(ServeurCheckIn Socket.cpp Socket.h Serveur.cpp Serveur.h)
add_executable(Serveur main.cpp)
target_link_libraries(Serveur ServeurCheckIn)