#include "tserversocket.h"
tserversocket::tserversocket(){
	sd = socket(PF_INET,SOCK_STREAM,0);
	if (sd == -1 ){
		print_err_code();
	}
}

int tserversocket::listen(){
	int ret = ::listen(sd,20);
	if (ret == -1 ){
		print_err_code();
	}
	return ret;
}

int tserversocket::bind(int _port){
	struct sockaddr_in sin = { 0 };
	sin.sin_family = AF_INET;
	sin.sin_addr.s_addr = INADDR_ANY;
	sin.sin_port = _port;
	int ret = ::bind(sd, (struct sockaddr *)&sin,sizeof(struct sockaddr_in));
	if (ret == -1 ){
		print_err_code();
	}

	return -1;
}

int tserversocket::accept(){
	return -1;
}
