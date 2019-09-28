#pragma once

#include <jni.h>

#define INJECT_CLASS_PATH "com/github/kr328/ibr/remote/Injector"
#define INJECT_METHOD_NAME "inject"

void init_inject_class_method(JNIEnv *env);
void invoke_inject_method(JNIEnv* env, const char *argument);