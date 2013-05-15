#ifndef TSERVERSOCKET
#define TSERVERSOCKET
#include "header.h"
#include "tsession.h"
class tserversocket
{
public:
	tserversocket();
	int bind(int _port);
	int listen();
	auto_ptr<tsession> accept();
	int close();
private:
	int sd;
	tserversocket(const  tserversocket &);
 	tserversocket & operator = (const  tserversocket & );
};


#endif

