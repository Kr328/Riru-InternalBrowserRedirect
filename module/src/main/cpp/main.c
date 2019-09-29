#include <stdio.h>
#include <jni.h>
#include <dlfcn.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>
#include <sys/system_properties.h>

#include "hook.h"
#include "log.h"
#include "utils.h"
#include "inject.h"

#define EXPORT __attribute__((visibility("default")))

#define DEX_PATH           "/system/framework/boot-internal-browser-redirect.jar"
#define SERVICE_STATUE_KEY "sys.ibr.status"
#define CONFIG_PATH_FORMAT "/data/misc/riru/modules/internal_browser_redirect/userdata/rules.%s.json"

static char *app_fork_argument;

static void on_app_fork(JNIEnv *env, jstring jAppDataDir, jstring jPackageName) {
    static char config_path_buffer[1024];
    char package_name[256];

    if (jPackageName) {
        const char *packageName = (*env)->GetStringUTFChars(env, jPackageName, NULL);
        sprintf(package_name, "%s", packageName);
        (*env)->ReleaseStringUTFChars(env, jPackageName, packageName);
    }
    else if (jAppDataDir) {
        const char *appDataDir = (*env)->GetStringUTFChars(env, jAppDataDir, NULL);
        int user = 0;
        while (appDataDir)
        {
            // /data/user/<user_id>/<package>
            if (sscanf(appDataDir, "/data/%*[^/]/%d/%s", &user, package_name) == 2)
                break;

            // /mnt/expand/<id>/user/<user_id>/<package>
            if (sscanf(appDataDir, "/mnt/expand/%*[^/]/%*[^/]/%d/%s", &user, package_name) == 2)
                break;

            // /data/data/<package>
            if (sscanf(appDataDir, "/data/%*[^/]/%s", package_name) == 1)
                break;

            package_name[0] = '\0';
        }
        (*env)->ReleaseStringUTFChars(env, jAppDataDir, appDataDir);
    }

    if ( app_fork_argument != NULL )
        free(app_fork_argument);
    sprintf(config_path_buffer, CONFIG_PATH_FORMAT, package_name);
    app_fork_argument = malloc_and_load_file("app_forked|", config_path_buffer);

    //LOGD("file = %s, data = %s", config_path_buffer, app_fork_argument);
}

EXPORT
void nativeForkAndSpecializePre(
    JNIEnv *env, jclass clazz, jint *_uid, jint *gid, jintArray *gids, jint *runtime_flags,
    jobjectArray *rlimits, jint *_mount_external, jstring *se_info, jstring *se_name,
    jintArray *fdsToClose, jintArray *fdsToIgnore, jboolean *is_child_zygote,
    jstring *instructionSet, jstring *appDataDir, jstring *packageName,
    jobjectArray *packagesForUID, jstring *sandboxId) {
    on_app_fork(env, *appDataDir, *packageName);
}

EXPORT
int nativeForkAndSpecializePost(JNIEnv *env, jclass clazz, jint res) {
    if ( res == 0 && app_fork_argument != NULL )
        invoke_inject_method(env, app_fork_argument);
}

EXPORT
int nativeForkSystemServerPost(JNIEnv *env, jclass clazz, jint res) {
    if ( res == 0 ) {
        __system_property_set(SERVICE_STATUE_KEY, "system_server_forked");
        invoke_inject_method(env, "system_server_forked");
    }
    return 0;
}

EXPORT
void onModuleLoaded() {
    char buffer[4096];
    char *p = NULL;

    strcpy(buffer,(p = getenv("CLASSPATH")) ? p : "");
    strcat(buffer,":" DEX_PATH);
    setenv("CLASSPATH",buffer,1);

    hook_install(&init_inject_class_method);

    __system_property_set(SERVICE_STATUE_KEY, "riru_loaded");
}

