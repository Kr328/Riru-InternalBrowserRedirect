#pragma once

#include "log.h"

#define REMOTE_SERVER_NAME "internal_browser_redirect_service"

int query_is_package_enabled(const char *package);
void close_connection();