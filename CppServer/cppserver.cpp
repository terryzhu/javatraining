#include "header.h"
#include "tserversocket.h"
using namespace std;
int main(){
	tserversocket server;
	server.bind(8888);
	server.listen();
	while(1){
		auto_ptr<tsession> pt = server.accept();
		cout<<"new connection "<<pt->sd<<endl;
		sleep(3);
	}
	//tserversocket s2(server);

	return 0;
}
