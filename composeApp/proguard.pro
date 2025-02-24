-keep class androidx.compose.runtime.** { *; }
-keep class androidx.collection.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.savedstate.** { *; }
-keep class androidx.core.bundle.** { *; }
#-keep class org.conscrypt.** { *; }
#-keep class android.security.** { *; }
#-keep class org.bouncycastle.jsse.** { *; }
#-keep class org.openjsse.javax.net.ssle.** { *; }
#-keep class android.net.http.** { *; }
#-keep class android.net.ssl.** { *; }
-keep class androidx.compose.ui.text.platform.ReflectionUtil { *; }
-dontnote **
-ignorewarnings

-keepclassmembers public class **$$serializer {
    private ** descriptor;
}

# We're excluding Material 2 from the project as we're using Material 3
-dontwarn androidx.compose.material.**

# Kotlinx coroutines rules seems to be outdated with the latest version of Kotlin and Proguard
-keep class kotlinx.coroutines.** { *; }
