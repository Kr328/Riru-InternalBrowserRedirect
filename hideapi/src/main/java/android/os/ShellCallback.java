package android.os;

public class ShellCallback implements Parcelable {
    private ShellCallback(Parcel in) {
    }

    public static final Creator<ShellCallback> CREATOR = new Creator<ShellCallback>() {
        @Override
        public ShellCallback createFromParcel(Parcel in) {
            return new ShellCallback(in);
        }

        @Override
        public ShellCallback[] newArray(int size) {
            return new ShellCallback[size];
        }
    };

    @Override
    public int describeContents() {
        throw new IllegalArgumentException("Stub!");
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        throw new IllegalArgumentException("Stub!");
    }
}
