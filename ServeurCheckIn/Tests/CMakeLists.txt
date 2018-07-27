cmake_minimum_required(VERSION 3.10)
project(Tests)

set(CMAKE_CXX_STANDARD 14)

add_subdirectory(lib/googletest-master)
include_directories(lib/googletest-master/googletest/include)
include_directories(lib/googletest-master/googlemock/include)

add_executable(Tests main.cpp ServeurTests/SocketTests.cpp )
target_link_libraries(Tests gtest gtest_main ServeurCheckIn)
enable_testing()