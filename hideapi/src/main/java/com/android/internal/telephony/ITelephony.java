package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface ITelephony extends IInterface {
    String getDeviceSoftwareVersionForSlot(int slotIndex, String callingPackage) throws RemoteException;

    String getDeviceId(String callingPackage) throws RemoteException;

    String getImeiForSlot(int slotIndex, String callingPackage) throws RemoteException;

    String getMeidForSlot(int slotIndex, String callingPackage) throws RemoteException;

    int getDataNetworkType(String callingPackage);

    int getDataNetworkTypeForSubscriber(int subId, String callingPackage);

    int getActivePhoneTypeForSlot(int slotIndex);

    int getActivePhoneType();

    public abstract class Stub extends Binder implements ITelephony {
        public static ITelephony asInterface(IBinder binder) {
            throw new IllegalArgumentException("Stub!");
        }

        @Override
        public IBinder asBinder() {
            throw new IllegalArgumentException("Stub!");
        }
    }
}
