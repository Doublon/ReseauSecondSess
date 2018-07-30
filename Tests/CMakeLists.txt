cmake_minimum_required(VERSION 3.10)
project(Tests)

find_package(Threads REQUIRED)
include_directories(${CTEST_SOURCE_DIRECTORY}/Socket)

add_subdirectory(lib/googletest-master)
include_directories(${CMAKE_SOURCE_DIR})
include_directories(lib/googletest-master/googletest/include)
include_directories(lib/googletest-master/googlemock/include)

add_executable(Tests main.cpp SocketUtilitiesTests.cpp)
target_link_libraries(Tests gtest gtest_main SocketUtilities)