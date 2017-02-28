package com.sdkbox.gradle.asm

import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

public class ActivityVisitor extends ClassVisitor implements Opcodes {

    public ActivityVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv)
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if(name.equals("onCreate") && mv!=null){
            mv = new ActivityMethodVisitor(mv);
        }
        return mv;
    }
}
