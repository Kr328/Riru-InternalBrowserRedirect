package android.os;

public final class ServiceManager {
    private static IServiceManager sServiceManager;

    private static IServiceManager getIServiceManager() {
        throw new IllegalArgumentException("Stub!");
    }

    public static IBinder getService(String name) {
        throw new IllegalArgumentException("Unsupported");
    }


    public static void addService(String name, IBinder binder) {
        throw new IllegalArgumentException("Stub!");
    }
}
