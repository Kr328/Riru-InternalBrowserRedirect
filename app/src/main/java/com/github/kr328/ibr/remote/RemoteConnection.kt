package com.github.kr328.ibr.remote

import android.os.Parcel
import android.os.RemoteException
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.compat.ServiceManager
import com.github.kr328.ibr.remote.server.IRemoteService
import java.lang.IllegalStateException

object RemoteConnection {
    val connection: IRemoteService by lazy {
        openRemoteConnection()
    }

    private fun openRemoteConnection(): IRemoteService {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()

        try {
            val activity = ServiceManager.getService("activity")

            data.writeInterfaceToken(IRemoteService::class.java.name)

            activity.transact(Constants.ACTIVITY_CONNECT_TRANSACT_CODE, data, reply, 0)

            val connection = IRemoteService.Stub.asInterface(reply.readStrongBinder())
                    ?: throw RemoteException("Unable to connect RemoteService")

            if ( connection.version != Constants.REMOTE_SERVICE_VERSION )
                throw IllegalStateException("Invalid service version")

            return connection
        } finally {
            data.recycle()
            reply.recycle()
        }
    }

}


