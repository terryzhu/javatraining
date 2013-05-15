#ifndef TSERVERSOCKET
#define TSERVERSOCKET
#include "header.h"

class tserversocket
{
public:
	tserversocket();
	int bind(int _port);
	int listen();
	int accept();
private:
	int sd;
};


#endif

