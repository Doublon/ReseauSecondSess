cmake_minimum_required(VERSION 3.10)
project(CIMP)

include_directories(../libCSV)
include_directories(../Socket)

add_library(CIMP RequestCIMP.cpp RequestCIMP.h ../libCSV/CSV.cpp ../libCSV/CSV.h ../Socket/States.h)