#include "header.h"
#include "tserversocket.h"
using namespace std;
int main(){
	tserversocket server;
	server.bind(8888);
	server.listen();
	while(1){
		int cd = server.accept();
		cout<<"new connection "<<cd<<endl;
	}
	return 0;
}
