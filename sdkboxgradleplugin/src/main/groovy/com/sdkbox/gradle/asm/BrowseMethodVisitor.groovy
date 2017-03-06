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

        if (!replace) {
            super.visitMethodInsn(opcode, owner, name, desc, itf)
        }

    }

}

