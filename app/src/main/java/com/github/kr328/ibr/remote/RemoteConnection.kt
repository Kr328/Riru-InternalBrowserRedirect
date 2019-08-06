package com.github.kr328.ibr.remote

import android.os.Parcel
import android.os.RemoteException
import com.github.kr328.ibr.BuildConfig
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.compat.connectSystemService

fun openRemoteConnection(): IRemoteService {
    val activity = connectSystemService("activity")
    val data = Parcel.obtain()
    val reply = Parcel.obtain()

    try {
        data.writeInterfaceToken(BuildConfig.APPLICATION_ID)

        activity.transact(Constants.ACTIVITY_CONNECT_TRANSACT_CODE, data, reply, 0)

        return IRemoteService.Stub.asInterface(reply.readStrongBinder())
                ?: throw RemoteException("Unable to connect RemoteService")
    } finally {
        data.recycle()
        reply.recycle()
    }
}
