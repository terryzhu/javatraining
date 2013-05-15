#ifndef TSESSION
#define TSESSION
#include "header.h"

class tsession
{
public:
	tsession(int _sd);
	int read(void *buf, size_t count);
	int write(const void *buf, size_t count);
private:
	int sd;
};


#endif
