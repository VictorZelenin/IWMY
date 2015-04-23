-keep class ** { *; }

-dontwarn **

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int d(...);
    public static int e(...);
    public static int i(...);
    public static int v(...);
    public static int w(...);
}
