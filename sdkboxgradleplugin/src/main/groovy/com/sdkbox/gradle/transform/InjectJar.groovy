package com.sdkbox.gradle.transform

import com.sdkbox.gradle.asm.BrowseClassVisitor
import com.sdkbox.gradle.utils.Log
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

public class InjectJar {

    private File jarFile
    private def tempDir

    public InjectJar(File jar) {
        jarFile = jar
        tempDir = './sdkbox'
    }

    public File inject() {
        if (!jarFile.exists()) {
            Log.error("$jarFile.absolutePath not exist")
            return jarFile
        }

        return injectClass(jarFile)
    }

    private File injectClass(File jar) {
        def jarInput = new JarFile(inputJar)
        def jarOutput = null

        def hexName = DigestUtils.md5Hex(inputJar.absolutePath).substring(0, 8);
        jarOutput = new File(tempDir, hexName + jar.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(jarOutput));

        Enumeration enumeration = jarInput.entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName);
            jarOutputStream.putNextEntry(zipEntry);

            //check class file and modify
            byte[] modifiedClassBytes = null;
            byte[] sourceClassBytes = IOUtils.toByteArray(jarInput.getInputStream(jarEntry));
            if (entryName.endsWith(".class")) {
                modifiedClassBytes = BrowseClassVisitor.inject(sourceClassBytes)
            }
            if (modifiedClassBytes == null) {
                Log.error('inject class failed, use origin replace')
                modifiedClassBytes = sourceClassBytes
            }

            jarOutputStream.write(modifiedClassBytes);
            jarOutputStream.closeEntry();
        }
        jarOutputStream.close();
        jarInput.close();

        return jarOutput
    }

}
