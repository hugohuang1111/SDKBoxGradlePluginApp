package com.sdkbox.gradle.asm

import com.sdkbox.gradle.utils.Log
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

public class BrowseClassVisitor extends ClassVisitor implements Opcodes {

    private boolean modify;

    public BrowseClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv)
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        BrowseMethodVisitor bmv = new BrowseMethodVisitor(mv)

        BrowseMethodVisitor.TransMethInfo tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'java/net/URL'
        tmi.methInfoOrigin.name = 'openConnectionL'
        tmi.methInfoOrigin.signature = '()Ljava/net/URLConnection;'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'URL_openConnection'
        tmi.methInfoNew.signature = '(Ljava/net/URL;)Ljava/net/URLConnection;'
        bmv.addTransMethInfo(tmi)


        tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'java/net/HttpURLConnection'
        tmi.methInfoOrigin.name = 'connect'
        tmi.methInfoOrigin.signature = '()V'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'HttpURLConnect_connect'
        tmi.methInfoNew.signature = '(Ljava/net/URLConnection;)V'
        bmv.addTransMethInfo(tmi)

        tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'java/net/HttpURLConnection'
        tmi.methInfoOrigin.name = 'getResponseCode'
        tmi.methInfoOrigin.signature = '()I'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'HttpURLConnect_getResponseCode'
        tmi.methInfoNew.signature = '(Ljava/net/HttpURLConnection;)I'
        bmv.addTransMethInfo(tmi)

        tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'javax/net/ssl/HttpsURLConnection'
        tmi.methInfoOrigin.name = 'connect'
        tmi.methInfoOrigin.signature = '()V'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'HttpURLConnect_connect'
        tmi.methInfoNew.signature = '(Ljava/net/URLConnection;)V'
        bmv.addTransMethInfo(tmi)

        tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'javax/net/ssl/HttpsURLConnection'
        tmi.methInfoOrigin.name = 'getResponseCode'
        tmi.methInfoOrigin.signature = '()I'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'HttpURLConnect_getResponseCode'
        tmi.methInfoNew.signature = '(Ljavax/net/ssl/HttpsURLConnection;)I'
        bmv.addTransMethInfo(tmi)

        tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'android/webkit/WebView'
        tmi.methInfoOrigin.name = 'loadUrl'
        tmi.methInfoOrigin.signature = '(Ljava/lang/String;)V'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'WebView_loadUrl'
        tmi.methInfoNew.signature = '(Landroid/webkit/WebView;Ljava/lang/String;)V'
        bmv.addTransMethInfo(tmi)

        tmi = new BrowseMethodVisitor.TransMethInfo()
        tmi.methInfoOrigin.owner = 'android/webkit/WebView'
        tmi.methInfoOrigin.name = 'loadUrl'
        tmi.methInfoOrigin.signature = '(Ljava/lang/String;Ljava/util/Map;)V'
        tmi.methInfoNew.owner = 'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge'
        tmi.methInfoNew.name = 'WebView_loadUrl'
        tmi.methInfoNew.signature = '(Landroid/webkit/WebView;Ljava/lang/String;Ljava/util/Map;)V'
        bmv.addTransMethInfo(tmi)

        /*
        bmv.registerCallback(new BrowseMethodVisitor.Callback() {
            @Override
            void onModify() {
                modify = true;
            }
        })
        */
        return bmv;
    }

    public static byte[] inject(byte[] source) {
        byte[] modifyByte = source;
        try {
            ClassReader cr = new ClassReader(source)
            ClassWriter cw = new ClassWriter(0)
            ClassVisitor cv = new BrowseClassVisitor(cw)
            cr.accept(cv, 0)

//            if (cv.modify) {
//                modifyByte = cw.toByteArray()
//            }
            modifyByte = cw.toByteArray()
        } catch (Exception e) {
            Log.error(e)
        }

        return modifyByte
    }
}
