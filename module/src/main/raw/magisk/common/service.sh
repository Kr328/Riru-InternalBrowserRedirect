#!/system/bin/sh
# Please don't hardcode /magisk/modname/... ; instead, please use $MODDIR/...
# This will make your scripts compatible even if Magisk change its mount point in the future
MODDIR=${0%/*}

# This script will be executed in late_start service mode
# More info in the main Magisk thread

MODULES_ROOT="/data/misc/riru/modules"
MODULE_NAME="internal_browser_redirect"

BOOT_LOGCAT_PATH="/data/local/tmp/boot_logcat.txt"

logcat > ${BOOT_LOGCAT_PATH} &
LOGCAT_PID=$!

while sleep 5
do
    if [[ -d "/sdcard/Android" ]];then
        break
    fi
done

kill ${LOGCAT_PID}

if [[ ! -f "$MODULES_ROOT/$MODULE_NAME/apk_installed" ]];then
    /system/bin/pm install -r "$MODULES_ROOT/$MODULE_NAME/app.apk"
    /system/bin/am start -n "com.github.kr328.ibr/com.github.kr328.ibr.FirstInstallActivity"

    touch "$MODULES_ROOT/$MODULE_NAME/apk_installed"
fi

