/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.github.kr328.ibr.remote.server;
public interface IClientService extends android.os.IInterface
{
  /** Default implementation for IClientService. */
  public static class Default implements IClientService
  {
    @Override public com.github.kr328.ibr.remote.model.RuleSet queryRuleSet(String packageName) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements IClientService
  {
    private static final String DESCRIPTOR = "com.github.kr328.ibr.remote.server.IClientService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.github.kr328.ibr.remote.server.IClientService interface,
     * generating a proxy if needed.
     */
    public static IClientService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof IClientService))) {
        return ((IClientService)iin);
      }
      return new Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_queryRuleSet:
        {
          data.enforceInterface(descriptor);
          String _arg0;
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
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements IClientService
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
      public String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public com.github.kr328.ibr.remote.model.RuleSet queryRuleSet(String packageName) throws android.os.RemoteException
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
      public static IClientService sDefaultImpl;
    }
    static final int TRANSACTION_queryRuleSet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(IClientService impl) {
      if (Proxy.sDefaultImpl == null && impl != null) {
        Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static IClientService getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
  }
  public com.github.kr328.ibr.remote.model.RuleSet queryRuleSet(String packageName) throws android.os.RemoteException;
}
