cmake_minimum_required(VERSION 3.0)
project(riru_internal_browser_redirect)

include_directories(libriru_utils)
include_directories(libriru_utils/libriru)
include_directories(libriru_utils/libxhook)

add_subdirectory(libriru_utils)

add_library(riru_internal_browser_redirect SHARED main.c hook.c inject.c remote.c)
target_link_libraries(riru_internal_browser_redirect riru_utils log)

