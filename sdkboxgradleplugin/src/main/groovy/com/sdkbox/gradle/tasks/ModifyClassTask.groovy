package com.sdkbox.gradle.tasks

import com.sdkbox.gradle.asm.ActivityVisitor
import com.sdkbox.gradle.utils.Log
import com.sdkbox.gradle.utils.Utils
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

public class ModifyClassTask extends DefaultTask {

    def buildType = 'debug'

    @TaskAction
    void run() {
        Log.debug('ModifyClassTask Begin')
        modifyClass(project.buildDir.path)
        Log.debug('ModifyClassTask End')
    }

    private void modifyClass(String buildDir) {
        def classesDir = Paths.get(buildDir, "intermediates", 'classes').toString()
        List<File> activityList = Utils.findActivitys(
                Paths.get(buildDir, "intermediates", 'manifests', 'full', buildType, 'AndroidManifest.xml')
                    .toString())
        for (int i = 0; i < activityList.size(); i++) {
            def activityPath = activityList[i].replace('.', '/')
            activityPath = Paths.get(classesDir, buildType, activityPath).toString()
            hookClassFile(activityPath + '.class')
        }
    }
    private List<File> findClass(Path dir) {
        final List<File> files = new ArrayList<File>();
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().toLowerCase().endsWith('.class')) {
                    files.add(file.toFile());
                }
//                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.class");
//                if (matcher.matches(file)) {
//                    files.add(file.toFile());
//                }
                return super.visitFile(file, attrs);
            }
        };

        Files.walkFileTree(dir, finder);
        return files
    }

    private static void hookClassFile(String path) {
        // adapts the class on the fly
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
            println(e)
        }
    }

}
