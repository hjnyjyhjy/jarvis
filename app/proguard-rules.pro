# Proguard rules for Platinum Arabic AI Assistant

# Keep all public classes and methods
-keep public class * {
    public protected *;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep Kotlin metadata
-keepclassmembers class **$WhenMappings {
    <fields>;
}

-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    <methods>;
}

# Keep Room database
-keep class * extends androidx.room.RoomDatabase

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keep @dagger.hilt.android.HiltAndroidApp class *

# Keep Retrofit and Gson
-keepclassmembers class ** {
    @retrofit2.http.* <methods>;
}
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

# Keep model classes
-keep class com.platinumassistant.data.models.** { *; }
-keep class com.platinumassistant.domain.entities.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Optimize
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

# Keep line numbers for crash reporting
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
