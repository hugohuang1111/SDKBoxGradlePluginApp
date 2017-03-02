package com.sdkbox.gradle.asm

import com.sdkbox.gradle.utils.Log
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes

public class BrowseClassVisitor extends ClassVisitor implements Opcodes {

    public BrowseClassVisitor(ClassVisitor cv) {
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

    public static byte[] inject(byte[] source) {
        try {
            ClassReader cr = new ClassReader(source);
            ClassWriter cw = new ClassWriter(0);
            ClassVisitor cv = new BrowseClassVisitor(cw);
            cr.accept(cv, 0);

            return cw.toByteArray();
        } catch (Exception e) {
            Log.error(e)
        }
    }
}
