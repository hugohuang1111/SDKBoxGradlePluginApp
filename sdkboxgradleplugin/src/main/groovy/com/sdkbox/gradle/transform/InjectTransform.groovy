package com.sdkbox.gradle.transform

import com.android.annotations.NonNull
import com.android.annotations.Nullable
import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.sdkbox.gradle.asm.ActivityVisitor
import com.sdkbox.gradle.utils.Log
import com.sdkbox.gradle.utils.Utils
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.nio.file.Paths

public class InjectTransform extends Transform {
    static AppExtension android
    private static Project project;
    private List<String> classList;

    public InjectTransform(Project project) {
        InjectTransform.project = project
    }

    @Override
    String getName() {
        return "SDKBox"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    public void transform(
            @NonNull Context context,
            @NonNull Collection<TransformInput> inputs,
            @NonNull Collection<TransformInput> referencedInputs,
            @Nullable TransformOutputProvider outputProvider,
            boolean isIncremental) throws IOException, TransformException, InterruptedException {
        android = project.extensions.getByType(AppExtension)

        configTargetClass()
        appendActivityTargetClass()

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                String destName = jarInput.file.name;
                /**
                 * Rename, maybe exist same name file
                 * */
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8);
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4);
                }

                /**
                 * jar output file
                 * */
                File dest = outputProvider.getContentLocation(
                        destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR);
                def jarOutput = modifyJarFileIf(jarInput.file, context.temporaryDir)
                if (null == jarOutput) {
                    Log.error('modify Jar file fail, use jarInput file replace')
                    jarOutput = jarInput.file
                }
                FileUtils.copyFile(jarOutput, dest);
            }

            /**
             * Directory Inputs
             * just copy
             */
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY);
                File src = modifyDirectoryInputIf(directoryInput.file, context.temporaryDir)
                FileUtils.copyDirectory(src, dest);
            }
        }
    }

    private File modifyJarFileIf(File inputJar, File tempDir) {
        if (null == inputJar) {
            return inputJar
        }

        if (!inputJar.absolutePath.contains("inmobi")) {
            return inputJar
        }

        InjectJar inj = new InjectJar(inputJar, tempDir)
        return inj.inject()

        /*
        def outputJar = null
        def willModify = false
        def jarInput = new JarFile(inputJar)
        Enumeration enumeration = jarInput.entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            String className = null
            if (entryName.endsWith(".class")) {
                className = entryName.replace("/", ".").replace(".class", "")
            }
            if (willInject(className)) {
                willModify = true
            }
        }
        if (willModify) {
            def hexName = DigestUtils.md5Hex(inputJar.absolutePath).substring(0, 8);
            outputJar = new File(tempDir, hexName + jarFile.name)
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar));

            enumeration = jarInput.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                String entryName = jarEntry.getName();
                InputStream inputStream = jarInput.getInputStream(jarEntry);
                ZipEntry zipEntry = new ZipEntry(entryName);

                jarOutputStream.putNextEntry(zipEntry);

                byte[] modifiedClassBytes = null;
                byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
                if (entryName.endsWith(".class")) {
                    modifiedClassBytes = injectClass(sourceClassBytes,
                            entryName.replace("/", ".").replace(".class", ""))
                }
                if (modifiedClassBytes == null) {
                    Log.error('inject class failed, use origin replace')
                    modifiedClassBytes = sourceClassBytes
                }
                jarOutputStream.write(modifiedClassBytes);
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
            return inputJar
        } else {
            outputJar = inputJar
        }
        jarInput.close();

        return outputJar;
        */
    }

    private File modifyDirectoryInputIf(File inputDirectory, File tempDir) {
        if (null == inputDirectory) {
            return inputDirectory
        }
        classList.each {String className ->
            String classPath = className.replace('.', '/') + '.class'
            File classFile = new File(inputDirectory, classPath);
            if (classFile.exists()) {
                injectClassFile(classFile.absolutePath)
            } else {
                // maybe the activity class include in third part lib file
            }
        }
        return inputDirectory
    }

    private void injectClassFile(String path) {
        try {
            byte[] b;
            InputStream is = new FileInputStream(path);

            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(0);
            ClassVisitor cv = new ActivityVisitor(cw);
            cr.accept(cv, 0);
            b = cw.toByteArray();

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(b);
            fos.close();
        } catch (Exception e) {
            Log.error(e)
        }
    }

    private boolean willInject(String className) {
        if (null == className) {
            return false
        }
        return classList.contains(className)
    }

    private byte[] injectClass(byte[] sourceBytes, String className) {
        if (!classList.contains(className)) {
            return sourceBytes
        }

        return sourceBytes
    }

    private void configTargetClass() {
        classList = new ArrayList<String>()
    }

    private void appendActivityTargetClass() {
        def buildType = 'debug'
        def activities = Utils.findActivitys(
                Paths.get(project.buildDir.path, "intermediates", 'manifests', 'full', buildType, 'AndroidManifest.xml')
                        .toString())
        classList.addAll(activities)
    }
}
