# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.12

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /cygdrive/c/Users/Tusse/.CLion2018.2/system/cygwin_cmake/bin/cmake.exe

# The command to remove a file.
RM = /cygdrive/c/Users/Tusse/.CLion2018.2/system/cygwin_cmake/bin/cmake.exe -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /cygdrive/d/GitHub/ReseauSecondSess

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug

# Include any dependencies generated for this target.
include Socket/CMakeFiles/ClientCheckIn.dir/depend.make

# Include the progress variables for this target.
include Socket/CMakeFiles/ClientCheckIn.dir/progress.make

# Include the compile flags for this target's objects.
include Socket/CMakeFiles/ClientCheckIn.dir/flags.make

Socket/CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.o: Socket/CMakeFiles/ClientCheckIn.dir/flags.make
Socket/CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.o: ../Socket/ClientCheckIn.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object Socket/CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.o"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && /usr/bin/c++.exe  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.o -c /cygdrive/d/GitHub/ReseauSecondSess/Socket/ClientCheckIn.cpp

Socket/CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.i"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && /usr/bin/c++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /cygdrive/d/GitHub/ReseauSecondSess/Socket/ClientCheckIn.cpp > CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.i

Socket/CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.s"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && /usr/bin/c++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /cygdrive/d/GitHub/ReseauSecondSess/Socket/ClientCheckIn.cpp -o CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.s

# Object files for target ClientCheckIn
ClientCheckIn_OBJECTS = \
"CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.o"

# External object files for target ClientCheckIn
ClientCheckIn_EXTERNAL_OBJECTS =

Socket/ClientCheckIn.exe: Socket/CMakeFiles/ClientCheckIn.dir/ClientCheckIn.cpp.o
Socket/ClientCheckIn.exe: Socket/CMakeFiles/ClientCheckIn.dir/build.make
Socket/ClientCheckIn.exe: Socket/libSocketUtilities.a
Socket/ClientCheckIn.exe: libConfig/liblibConfig.a
Socket/ClientCheckIn.exe: Socket/CMakeFiles/ClientCheckIn.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable ClientCheckIn.exe"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/ClientCheckIn.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Socket/CMakeFiles/ClientCheckIn.dir/build: Socket/ClientCheckIn.exe

.PHONY : Socket/CMakeFiles/ClientCheckIn.dir/build

Socket/CMakeFiles/ClientCheckIn.dir/clean:
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && $(CMAKE_COMMAND) -P CMakeFiles/ClientCheckIn.dir/cmake_clean.cmake
.PHONY : Socket/CMakeFiles/ClientCheckIn.dir/clean

Socket/CMakeFiles/ClientCheckIn.dir/depend:
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /cygdrive/d/GitHub/ReseauSecondSess /cygdrive/d/GitHub/ReseauSecondSess/Socket /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket/CMakeFiles/ClientCheckIn.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : Socket/CMakeFiles/ClientCheckIn.dir/depend

