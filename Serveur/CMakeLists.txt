add_library(ServeurCheckIn Socket.cpp Socket.h)
add_executable(Serveur main.cpp)
target_link_libraries(Serveur ServeurCheckIn)