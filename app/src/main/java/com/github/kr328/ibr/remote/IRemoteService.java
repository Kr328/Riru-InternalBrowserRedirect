/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.github.kr328.ibr.remote;
public interface IRemoteService extends android.os.IInterface
{
  /** Default implementation for IRemoteService. */
  public static class Default implements com.github.kr328.ibr.remote.IRemoteService
  {
    @Override public int getVersion() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public java.util.Map queryAllRuleSet() throws android.os.RemoteException
    {
      return null;
    }
    @Override public com.github.kr328.ibr.remote.model.RuleSet queryRuleSet(java.lang.String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void updateRuleSet(java.lang.String packageName, com.github.kr328.ibr.remote.model.RuleSet ruleSet) throws android.os.RemoteException
    {
    }
    @Override public void removeRuleSet(java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void updateSetting(java.lang.String feature, boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean getBooleanSetting(java.lang.String feature) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.github.kr328.ibr.remote.IRemoteService
  {
    private static final java.lang.String DESCRIPTOR = "com.github.kr328.ibr.remote.IRemoteService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.github.kr328.ibr.remote.IRemoteService interface,
     * generating a proxy if needed.
     */
    public static com.github.kr328.ibr.remote.IRemoteService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.github.kr328.ibr.remote.IRemoteService))) {
        return ((com.github.kr328.ibr.remote.IRemoteService)iin);
      }
      return new com.github.kr328.ibr.remote.IRemoteService.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_getVersion:
        {
          data.enforceInterface(descriptor);
          int _result = this.getVersion();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_queryAllRuleSet:
        {
          data.enforceInterface(descriptor);
          java.util.Map _result = this.queryAllRuleSet();
          reply.writeNoException();
          reply.writeMap(_result);
          return true;
        }
        case TRANSACTION_queryRuleSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          com.github.kr328.ibr.remote.model.RuleSet _result = this.queryRuleSet(_arg0);
          reply.writeNoException();
          if ((_result!=null)) {
            reply.writeInt(1);
            _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          }
          else {
            reply.writeInt(0);
          }
          return true;
        }
        case TRANSACTION_updateRuleSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          com.github.kr328.ibr.remote.model.RuleSet _arg1;
          if ((0!=data.readInt())) {
            _arg1 = com.github.kr328.ibr.remote.model.RuleSet.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          this.updateRuleSet(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_removeRuleSet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.removeRuleSet(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_updateSetting:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.updateSetting(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getBooleanSetting:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.getBooleanSetting(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.github.kr328.ibr.remote.IRemoteService
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public int getVersion() throws android.os.RemoteException
      {
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
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.Map queryAllRuleSet() throws android.os.RemoteException
      {
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
          java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
          _result = _reply.readHashMap(cl);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public com.github.kr328.ibr.remote.model.RuleSet queryRuleSet(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        com.github.kr328.ibr.remote.model.RuleSet _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryRuleSet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().queryRuleSet(packageName);
          }
          _reply.readException();
          if ((0!=_reply.readInt())) {
            _result = com.github.kr328.ibr.remote.model.RuleSet.CREATOR.createFromParcel(_reply);
          }
          else {
            _result = null;
          }
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void updateRuleSet(java.lang.String packageName, com.github.kr328.ibr.remote.model.RuleSet ruleSet) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          if ((ruleSet!=null)) {
            _data.writeInt(1);
            ruleSet.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateRuleSet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().updateRuleSet(packageName, ruleSet);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeRuleSet(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeRuleSet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().removeRuleSet(packageName);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void updateSetting(java.lang.String feature, boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(feature);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().updateSetting(feature, enabled);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean getBooleanSetting(java.lang.String feature) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(feature);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getBooleanSetting, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getBooleanSetting(feature);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static com.github.kr328.ibr.remote.IRemoteService sDefaultImpl;
    }
    static final int TRANSACTION_getVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_queryAllRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_queryRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_updateRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_removeRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_updateSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getBooleanSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    public static boolean setDefaultImpl(com.github.kr328.ibr.remote.IRemoteService impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.github.kr328.ibr.remote.IRemoteService getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public int getVersion() throws android.os.RemoteException;
  public java.util.Map queryAllRuleSet() throws android.os.RemoteException;
  public com.github.kr328.ibr.remote.model.RuleSet queryRuleSet(java.lang.String packageName) throws android.os.RemoteException;
  public void updateRuleSet(java.lang.String packageName, com.github.kr328.ibr.remote.model.RuleSet ruleSet) throws android.os.RemoteException;
  public void removeRuleSet(java.lang.String packageName) throws android.os.RemoteException;
  public void updateSetting(java.lang.String feature, boolean enabled) throws android.os.RemoteException;
  public boolean getBooleanSetting(java.lang.String feature) throws android.os.RemoteException;
}
