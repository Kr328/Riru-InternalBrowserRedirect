package com.github.kr328.ibr.remote;

import com.github.kr328.ibr.remote.model.RuleSet;

public interface IRemoteService extends android.os.IInterface {
    int getVersion() throws android.os.RemoteException;

    java.util.Map queryAllRuleSet() throws android.os.RemoteException;

    RuleSet queryRuleSet(java.lang.String packageName) throws android.os.RemoteException;

    void updateRuleSet(java.lang.String packageName, RuleSet ruleSet) throws android.os.RemoteException;
    //Map<String, RuleSet>

    void remoteRuleSet(java.lang.String packageName) throws android.os.RemoteException;

    /**
     * Default implementation for IRemoteService.
     */
    class Default implements IRemoteService {
        @Override
        public int getVersion() throws android.os.RemoteException {
            return 0;
        }

        @Override
        public java.util.Map queryAllRuleSet() throws android.os.RemoteException {
            return null;
        }
        //Map<String, RuleSet>

        @Override
        public RuleSet queryRuleSet(java.lang.String packageName) throws android.os.RemoteException {
            return null;
        }

        @Override
        public void updateRuleSet(java.lang.String packageName, RuleSet ruleSet) throws android.os.RemoteException {
        }

        @Override
        public void remoteRuleSet(java.lang.String packageName) throws android.os.RemoteException {
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    abstract class Stub extends android.os.Binder implements IRemoteService {
        static final int TRANSACTION_getVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_queryAllRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_queryRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_updateRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_remoteRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        private static final java.lang.String DESCRIPTOR = "com.github.kr328.ibr.remote.IRemoteService";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.github.kr328.ibr.remote.IRemoteService interface,
         * generating a proxy if needed.
         */
        public static IRemoteService asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof IRemoteService))) {
                return ((IRemoteService) iin);
            }
            return new IRemoteService.Stub.Proxy(obj);
        }

        public static boolean setDefaultImpl(IRemoteService impl) {
            if (Stub.Proxy.sDefaultImpl == null && impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IRemoteService getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_getVersion: {
                    data.enforceInterface(descriptor);
                    int _result = this.getVersion();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case TRANSACTION_queryAllRuleSet: {
                    data.enforceInterface(descriptor);
                    java.util.Map _result = this.queryAllRuleSet();
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                }
                case TRANSACTION_queryRuleSet: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    RuleSet _result = this.queryRuleSet(_arg0);
                    reply.writeNoException();
                    if ((_result != null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case TRANSACTION_updateRuleSet: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    RuleSet _arg1;
                    if ((0 != data.readInt())) {
                        _arg1 = RuleSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    this.updateRuleSet(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_remoteRuleSet: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.remoteRuleSet(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements IRemoteService {
            public static IRemoteService sDefaultImpl;
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public int getVersion() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getVersion, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().getVersion();
                    }
                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            //Map<String, RuleSet>

            @Override
            public java.util.Map queryAllRuleSet() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.Map _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_queryAllRuleSet, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().queryAllRuleSet();
                    }
                    _reply.readException();
                    java.lang.ClassLoader cl = this.getClass().getClassLoader();
                    _result = _reply.readHashMap(cl);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public RuleSet queryRuleSet(java.lang.String packageName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                RuleSet _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_queryRuleSet, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().queryRuleSet(packageName);
                    }
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        _result = RuleSet.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void updateRuleSet(java.lang.String packageName, RuleSet ruleSet) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    if ((ruleSet != null)) {
                        _data.writeInt(1);
                        ruleSet.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_updateRuleSet, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().updateRuleSet(packageName, ruleSet);
                        return;
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void remoteRuleSet(java.lang.String packageName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_remoteRuleSet, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().remoteRuleSet(packageName);
                        return;
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
