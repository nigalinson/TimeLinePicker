# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class key.open.cn.androidfuturekey.po.** { *; }
-keep class key.open.cn.androidfuturekey.api.ApiResponse { *; }
-keep class key.open.cn.androidfuturekey.orm.** { *; }
-keep class key.open.cn.androidfuturekey.orm.entity.** { *; }

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

-keep class com.umeng.commonsdk.** {*;}
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#RxJava2 RxAndroid2
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *; }

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod

#audio
-keep class com.github.piasy.audioprocessor.** {*;}

#JPUSH
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#blockcanary
-dontwarn com.github.moduth.blockcanary.**
-keep class com.github.moduth.blockcanary.** { *; }
-keep class com.github.moduth.blockcanary.internal.** { *; }

#eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#动态loading
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

#动态loading
-keep class com.github.moduth.blockcanary.** { *; }
-keep class com.github.moduth.blockcanary.** { *; }

#greenDao
-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**

#take_photo
-dontwarn android.support.**
-keep class android.support.** { *; }

#热更新
#基线包使用，生成mapping.txt
#-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}

-dontwarn com.alibaba.sdk.android.beacon.**
-keep class com.alibaba.sdk.android.beacon.** { *; }

-dontwarn key.open.cn.androidfuturekey.base.**
-keep class key.open.cn.androidfuturekey.base.** { *; }

#snaphelper
-dontwarn com.github.rubensousa.gravitysnaphelper.**
-keep class com.github.rubensousa.gravitysnaphelper.**{*;}

#lifecycle
-dontwarn android.arch.lifecycle.**
-keep class android.arch.lifecycle.** {*;}

#glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** {*;}

#rxPermission
-dontwarn com.tbruyelle.rxpermissions2.**
-keep class com.tbruyelle.rxpermissions2.** {*;}

##widget
#-dontwarn key.open.cn.androidfuturekey.widget.**
#-keep class key.open.cn.androidfuturekey.widget.** {*;}

#nineOldAndroids
-dontwarn com.nineoldandroids.animation.**
-keep class com.nineoldandroids.animation.** {*;}

#防止inline
-dontoptimize

-keepclassmembers class key.open.cn.androidfuturekey.base.MyApplication {
    public <init>();
}
# 如果不使用android.support.annotation.Keep则需加上此行
# -keep class key.open.cn.androidfuturekey.hotfix.SophixStubApplication$MyApplicationStub

-keepattributes InnerClasses
-dontoptimize

-keep class org.devio.takephoto.** { *; }
-dontwarn org.devio.takephoto.**

-keep class com.jph.takephoto.** { *; }
-dontwarn com.jph.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**

#qrcode
-keep class key.open.cn.qrcode.** { *; }
-dontwarn key.open.cn.qrcode.**
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

#permissionHelper
-keep class key.open.cn.permissionhelper.** { *; }
-dontwarn key.open.cn.permissionhelper.**

#qrcode
-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.journeyapps.barcodescanner.**
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

#友盟

#XXpermission
-dontwarn com.hjq.permissions.**

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------



#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.DialogFragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.database.sqlite.SQLiteOpenHelper
-keep class android.support.** {*;}
-keep class android.database.** {*;}
-keep class java.text.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---
