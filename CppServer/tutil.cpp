#include "tutil.h"
#include "errno.h"
#include "stdio.h"
#include "string.h"

void print_err_code()
{
    printf("Err code:[%d] Info:[%s]\n", errno, strerror(errno));
}
