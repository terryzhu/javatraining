CXXFLAG= -Wall -g



all:CppServer_out 
CppServer_out:tserversocket.o tutil.o CppServer.o
	g++ $(CXXFLAG) -o CppServer_out tserversocket.o tutil.o CppServer.o

CppServer.o:cppserver.cpp
	g++ $(CXXFLAG) -o CppServer.o -c cppserver.cpp

tserversocket.o:tserversocket.cpp
	g++ $(CXXFLAG) -o tserversocket.o -c tserversocket.cpp 

tutil.o:tutil.cpp
	g++ $(CXXFLAG) -o tutil.o -c tutil.cpp

