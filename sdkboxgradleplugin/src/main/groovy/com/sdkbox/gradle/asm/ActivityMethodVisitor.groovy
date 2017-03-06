package com.sdkbox.gradle.asm

import jdk.internal.org.objectweb.asm.Label
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

public class ActivityMethodVisitor extends MethodVisitor {

    public ActivityMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitCode() {

        /*
         * source code:
         * test()
         *
         */
        /*
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "com/sdkbox/gradle/sdkboxgradlepluginapp/MainActivity", "test",
                "()V",
                false);
        */

        /*
         * source code:
         * testA("TestAParam")
         *
         */
        /*
        mv.visitLdcInsn("TestAParam");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "com/sdkbox/gradle/sdkboxgradlepluginapp/MainActivity", "testA",
                "(Ljava/lang/String;)V",
                false);
        */
        super.visitCode()
    }

    @Override
    void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN) {
            /*
             * source code:
             * android/util/Log("HHH", "this is from asm")
             *
             */
            mv.visitLdcInsn("SDKBox")
            mv.visitLdcInsn("this is from asm")
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "android/util/Log", "d",
                    "(Ljava/lang/String;Ljava/lang/String;)I",
                    false)
            mv.visitInsn(Opcodes.POP)
        }
        super.visitInsn(opcode)
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if ('java.net.HttpURLConnection' == owner && 'connect' == name) {
            super.visitMethodInsn(Opcodes.INVOKESTATIC,
                    'com/sdkbox/gradle/sdkboxgradlepluginapp/NetBridge',
                    'HttpURLConnect_connect',
                    '(Ljava.net.HttpURLConnection;)V', itf)
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf)
        }
//        Log.debug("visitMethodInsn:$opcode,$owner,$name,$desc,$itf")
    }

    @Override
    void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc)
//        Log.debug("visitFieldInsn:$opcode,$owner,$name,$desc")
    }

    @Override
    void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index)
//        Log.debug("visitLocalVariable:$name,$desc,$signature,$start,$end,$index")
    }
}
