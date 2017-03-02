package com.sdkbox.gradle

import com.android.build.api.transform.Transform
import com.android.build.gradle.BaseExtension
import com.sdkbox.gradle.transform.InjectTransform
import com.sdkbox.gradle.utils.Log
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.sdkbox.gradle.tasks.ModifyClassTask

class SDKBoxExtension {
    String appID
    String appSec
}

public class SDKBox implements Plugin<Project> {

    static final String PLUGIN_NAME = "SDKBox"

    Project project
    SDKBoxExtension extension

    @Override
    void apply(Project project) {
        Log.showSDKBoxLogo()
        this.project = project

        def sp = project.gradle.startParameter
        def p = sp.projectDir
        def t = sp.taskNames[0]

        project.gradle.addListener(new GradleBuildListener())
        createExtension()
        createTask()
        registerTransform()
    }

    void createExtension() {
        extension = project.extensions.create(PLUGIN_NAME, SDKBoxExtension)
    }

    void createTask() {
        project.task('sdkboxInfo', group: 'SDKBox', description: 'show SDKBox Info').doLast {
            Log.showSDKBoxLogo()
        }
        project.task('sdkboxConfigClass', group: 'SDKBox', description: 'reconfig class', type: ModifyClassTask)
    }

    void registerTransform() {
        BaseExtension android = project.extensions.getByType(BaseExtension)
        Transform transform = new InjectTransform(project)
        android.registerTransform(transform)
    }
}


