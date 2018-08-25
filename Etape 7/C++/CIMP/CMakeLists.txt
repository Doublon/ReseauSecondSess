cmake_minimum_required(VERSION 3.10)
project(CIMP)

include_directories(../libCSV)

add_library(CIMP RequestCIMP.cpp RequestCIMP.h ../libCSV/CSV.cpp ../libCSV/CSV.h)