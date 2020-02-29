#!/system/bin/sh
# Please don't hardcode /magisk/modname/... ; instead, please use $MODDIR/...
# This will make your scripts compatible even if Magisk change its mount point in the future
MODDIR=${0%/*}

# This script will be executed in late_start service mode
# More info in the main Magisk thread

BOOT_LOGCAT_PATH="/data/local/tmp/boot_logcat.txt"
TEMP_APK_PATH="/data/local/tmp/ibr.apk"

logcat > ${BOOT_LOGCAT_PATH} &
LOGCAT_PID=$!

while sleep 5
do
    if [[ -d "/sdcard/Android" ]] && [[ "$(getprop sys.boot_completed)" = "1" ]];then
        break
    fi
done

kill ${LOGCAT_PID}

if [[ ! -f "$MODDIR/apk_installed" ]];then
  cp "$MODDIR/app.apk" "$TEMP_APK_PATH"
  chown shell:shell "$TEMP_APK_PATH"
  chmod 0644 "$TEMP_APK_PATH"

  /system/bin/pm install -r "$TEMP_APK_PATH"
  /system/bin/am start -n "com.github.kr328.ibr/com.github.kr328.ibr.FirstInstallActivity"

  touch "$MODDIR/apk_installed"
  rm "$TEMP_APK_PATH"
fi

