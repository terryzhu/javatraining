CXXFLAG= -Wall -g
OBJTEMP=tserversocket.o tutil.o cppserver.o tsession.o

all:CppServer_out
CppServer_out:$(OBJTEMP)
	g++ $(CXXFLAG) -o CppServer_out $(OBJTEMP)

%.o: %.cpp
	g++ $(CXXFLAG) -c -o $@ $<

clean:
	rm $(OBJTEMP)
	rm CppServer_out
