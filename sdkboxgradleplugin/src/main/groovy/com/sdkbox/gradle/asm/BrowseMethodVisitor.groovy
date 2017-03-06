package com.sdkbox.gradle.asm

import com.sdkbox.gradle.utils.Log
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

public class BrowseMethodVisitor extends MethodVisitor {

    public class methInfo {
        def owner;
        def name;
        def signature;
    }

    public class TransMethInfo {
        def methInfoOrigin;
        def methInfoNew;

        public TransMethInfo() {
            methInfoOrigin = new methInfo()
            methInfoNew = new methInfo()
        }
    }

    def transMap = null;

    public class Callback {
        public void onModify() {
        }
    }

    private Callback cb;

    public BrowseMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv)
        transMap = new ArrayList<TransMethInfo>()
    }

    public void registerCallback(Callback cb) {
        this.cb = cb;
    }

    public void addTransMethInfo(TransMethInfo m) {
        transMap.add(m)
    }

    private List<TransMethInfo> findTransMethInfo(String owner, String name, String signature) {
        List<TransMethInfo> l = new ArrayList<TransMethInfo>();

        for (TransMethInfo m : transMap) {
            if (m.methInfoOrigin.owner == owner
                    && m.methInfoOrigin.name == name
                    && m.methInfoOrigin.signature == signature) {
                l.add(m)
            }
        }

        return l
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        def replace = false
        def l = findTransMethInfo(owner, name, desc)
        for (def m : l) {
            //Log.debug("visitMethodInsn: $opcode, $owner, $name, $desc, $itf")
            super.visitMethodInsn(Opcodes.INVOKESTATIC,
                    m.methInfoNew.owner,
                    m.methInfoNew.name,
                    m.methInfoNew.signature,
                    itf)
            replace = true
        }

        /*
        if ('java/net/URL' == owner) {
            if ('openConnection' == name) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                        'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge',
                        'URL_openConnection',
                        '(Ljava/net/URL;)Ljava/net/URLConnection;', itf)
                replace = true
            }
        } else if (('java/net/HttpURLConnection' == owner || "javax/net/ssl/HttpsURLConnection" == owner)) {
            if ('connect' == name) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                        'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge',
                        'HttpURLConnect_connect',
                        '(Ljava/net/URLConnection;)V', itf)
                replace = true
            } else if ('getResponseCode' == name) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                        'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge',
                        'HttpURLConnect_getResponseCode',
                        '(Ljava/net/HttpURLConnection;)I', itf)
                replace = true
            }
        } else if ('android/webkit/WebView' == owner) {
            if ('loadUrl' == name) {
                def targetDesc = '(Landroid/webkit/WebView;Ljava/lang/String;)V'
                if ('(Ljava/lang/String;Ljava/util/Map;)V' == desc) {
                    targetDesc = '(Landroid/webkit/WebView;Ljava/lang/String;Ljava/util/Map;)V'
                }
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                        'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge',
                        'WebView_loadUrl',
                        targetDesc, itf)
                replace = true
            }
        }
        */

        if (!replace) {
            super.visitMethodInsn(opcode, owner, name, desc, itf)
        }

    }

}

