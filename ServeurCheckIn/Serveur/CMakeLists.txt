cmake_minimum_required(VERSION 3.10)
project(ServeurCheckIn)

set(CMAKE_CXX_STANDARD 14)

add_library(ServeurCheckIn SHARED Serveur.cpp Serveur.h Socket.cpp Socket.h)