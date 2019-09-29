#include "remote.h"

#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <stdint.h>

static int remote_socket = -1;

static int open_connection() {
    if ( remote_socket >= 0 )
        return remote_socket;
    
    int fd = socket(AF_LOCAL, SOCK_STREAM, 0);

    struct sockaddr_un address;

    address.sun_family = AF_LOCAL;
    address.sun_path[0] = 0;
    strcpy(address.sun_path + 1, REMOTE_SERVER_NAME);

    if ( connect(fd, (struct sockaddr *) &address, offsetof(struct sockaddr_un, sun_path) + sizeof(REMOTE_SERVER_NAME)) < 0 ) {
        close(fd);
        return -1;
    }

    return (remote_socket = fd);
}

int query_is_package_enabled(const char *package) {
    int fd = open_connection();

    if ( fd < 0 )
        return 0;
    
    LOGD("Remote query");

    int32_t length = strlen(package);
    int32_t send_length = htonl(length);

    write(fd, &send_length, sizeof(send_length));
    write(fd, package, length);

    int32_t result = 0;
    if ( read(fd, &result, sizeof(result)) < 0 )
        close_connection();
    
    return result;
}

void close_connection() {
    if ( remote_socket >= 0 )
        close(remote_socket);
    remote_socket = -1;
}