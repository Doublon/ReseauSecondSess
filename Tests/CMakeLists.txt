find_package(Threads REQUIRED)
include_directories(${CTEST_SOURCE_DIRECTORY}/Serveur)

add_subdirectory(lib/googletest-master)
include_directories(lib/googletest-master/googletest/include)
include_directories(lib/googletest-master/googlemock/include)

add_executable(Tests main.cpp SocketTests.cpp )
target_link_libraries(Tests gtest gtest_main ServeurCheckIn)