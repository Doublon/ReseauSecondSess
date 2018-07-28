cmake_minimum_required(VERSION 3.10)
project(CSVUtilities)

add_library(libCSV SHARED CSVUtilities.cpp CSVUtilities.h)
target_link_libraries(CSVUtilities libCSV)