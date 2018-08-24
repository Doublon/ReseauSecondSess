cmake_minimum_required(VERSION 3.10)
project(Tests)

find_package(Threads REQUIRED)

include_directories(../Socket)

add_subdirectory(lib/googletest-master)
include_directories(lib/googletest-master/googletest/include)
include_directories(lib/googletest-master/googlemock/include)

add_executable(Tests main.cpp SocketUtilitiesTests.cpp ServeurSocketTests.cpp)
target_link_libraries(Tests gtest gtest_main SocketUtilities)