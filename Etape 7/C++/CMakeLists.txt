cmake_minimum_required (VERSION 3.10)
project (CheckIn)

add_subdirectory(libConfig)
add_subdirectory(libCSV)
add_subdirectory(CIMP)
add_subdirectory (Socket)
add_subdirectory (Tests)

enable_testing ()