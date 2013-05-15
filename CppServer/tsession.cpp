#include "tsession.h"
tsession::tsession(int _sd){
	sd = _sd;
}

int tsession::read(void *buf, size_t count)
{
	return ::read(sd,buf,count);
}

int tsession::write(const void *buf, size_t count)
{
	return ::write(sd,buf,count);
}
