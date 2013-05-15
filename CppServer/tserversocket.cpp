#include "tserversocket.h"
tserversocket::tserversocket(){
	sd = socket(PF_INET,SOCK_STREAM,0);
	if (sd == -1 ){
		print_err_code();
	}
	int sock_opt = 1;
	if (setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, (void*)&sock_opt, sizeof(sock_opt) ) == -1){
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
	sin.sin_port = htons(_port);

	int ret = ::bind(sd, (struct sockaddr *)&sin,sizeof(struct sockaddr_in));
	if (ret == -1 ){
		print_err_code();
	}

	return ret;
}

auto_ptr<tsession> tserversocket::accept(){
	int ret = ::accept(sd,NULL,NULL);
	if (ret == -1 ){
		print_err_code();
	}
	auto_ptr<tsession> pt(new tsession(ret));
	return pt;
}

int tserversocket::close(){
	int ret = ::close(sd);
	// is it OK? like script?
	ret == -1 && print_err_code();
	return ret;
}
