# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.10

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
CMAKE_COMMAND = /cygdrive/c/Users/Doublon/.CLion2018.1/system/cygwin_cmake/bin/cmake.exe

# The command to remove a file.
RM = /cygdrive/c/Users/Doublon/.CLion2018.1/system/cygwin_cmake/bin/cmake.exe -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /cygdrive/d/GitHub/ReseauSecondSess

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug

# Include any dependencies generated for this target.
include Socket/CMakeFiles/ServeurCheckIn.dir/depend.make

# Include the progress variables for this target.
include Socket/CMakeFiles/ServeurCheckIn.dir/progress.make

# Include the compile flags for this target's objects.
include Socket/CMakeFiles/ServeurCheckIn.dir/flags.make

Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o: Socket/CMakeFiles/ServeurCheckIn.dir/flags.make
Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o: ../Socket/ServeurCheckIn.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && /usr/bin/c++.exe  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o -c /cygdrive/d/GitHub/ReseauSecondSess/Socket/ServeurCheckIn.cpp

Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.i"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && /usr/bin/c++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /cygdrive/d/GitHub/ReseauSecondSess/Socket/ServeurCheckIn.cpp > CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.i

Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.s"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && /usr/bin/c++.exe $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /cygdrive/d/GitHub/ReseauSecondSess/Socket/ServeurCheckIn.cpp -o CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.s

Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.requires:

.PHONY : Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.requires

Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.provides: Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.requires
	$(MAKE) -f Socket/CMakeFiles/ServeurCheckIn.dir/build.make Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.provides.build
.PHONY : Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.provides

Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.provides.build: Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o


# Object files for target ServeurCheckIn
ServeurCheckIn_OBJECTS = \
"CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o"

# External object files for target ServeurCheckIn
ServeurCheckIn_EXTERNAL_OBJECTS =

Socket/ServeurCheckIn.exe: Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o
Socket/ServeurCheckIn.exe: Socket/CMakeFiles/ServeurCheckIn.dir/build.make
Socket/ServeurCheckIn.exe: Socket/libSocketUtilities.a
Socket/ServeurCheckIn.exe: Socket/CMakeFiles/ServeurCheckIn.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable ServeurCheckIn.exe"
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/ServeurCheckIn.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
Socket/CMakeFiles/ServeurCheckIn.dir/build: Socket/ServeurCheckIn.exe

.PHONY : Socket/CMakeFiles/ServeurCheckIn.dir/build

Socket/CMakeFiles/ServeurCheckIn.dir/requires: Socket/CMakeFiles/ServeurCheckIn.dir/ServeurCheckIn.cpp.o.requires

.PHONY : Socket/CMakeFiles/ServeurCheckIn.dir/requires

Socket/CMakeFiles/ServeurCheckIn.dir/clean:
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket && $(CMAKE_COMMAND) -P CMakeFiles/ServeurCheckIn.dir/cmake_clean.cmake
.PHONY : Socket/CMakeFiles/ServeurCheckIn.dir/clean

Socket/CMakeFiles/ServeurCheckIn.dir/depend:
	cd /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /cygdrive/d/GitHub/ReseauSecondSess /cygdrive/d/GitHub/ReseauSecondSess/Socket /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket /cygdrive/d/GitHub/ReseauSecondSess/cmake-build-debug/Socket/CMakeFiles/ServeurCheckIn.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : Socket/CMakeFiles/ServeurCheckIn.dir/depend

