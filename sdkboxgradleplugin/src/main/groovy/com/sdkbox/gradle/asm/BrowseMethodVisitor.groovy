package com.sdkbox.gradle.asm

import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

public class BrowseMethodVisitor extends MethodVisitor {

    public BrowseMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitCode() {
        mv.visitLdcInsn("SDKBox")
        mv.visitLdcInsn("this is from asm")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                "android/util/Log", "d",
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false)
        mv.visitInsn(Opcodes.POP)

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

}

