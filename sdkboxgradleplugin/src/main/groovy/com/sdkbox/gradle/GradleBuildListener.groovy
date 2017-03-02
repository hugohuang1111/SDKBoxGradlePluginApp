package com.sdkbox.gradle

import com.sdkbox.gradle.utils.Log
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState
import org.gradle.util.Clock

class GradleBuildListener implements TaskExecutionListener, BuildListener {
    private Clock clock
    private times = []

    @Override
    void beforeExecute(Task task) {
        clock = new Clock()
        // println "GradleBuildListener task before execute " + task.getName()

        /*
        if ('transformClassesWithDexForDebug' == task.getName()) {
            def taskName = 'sdkboxConfigClass'
            Set<Task> tasks = task.project.getTasksByName(taskName, false)
            if (1 != tasks.size()) {
                Log.debug("can't find task >>> $taskName or more than one")
                return
            }
            for (Task t in tasks) {
                t.execute()
            }
        }
        */
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        // println "GradleBuildListener task after execute " + task.getName()

        def ms = clock.timeInMs
        times.add([ms, task.path])
        // task.project.logger.warn "${task.path} spend ${ms}ms"
    }

    @Override
    void buildFinished(BuildResult result) {
        println "GradleBuildListener total time:"
        for (time in times) {
            if (time[0] >= 50) {
                printf "%7sms  %s\n", time
            }
        }
    }

    @Override
    void buildStarted(Gradle gradle) {
        println('GradleBuildListener build start')
    }

    @Override
    void projectsEvaluated(Gradle gradle) {
        println('GradleBuildListener project evaluated')
    }

    @Override
    void projectsLoaded(Gradle gradle) {
        println('GradleBuildListener project loaded')
    }

    @Override
    void settingsEvaluated(Settings settings) {
        println('GradleBuildListener settings evaluated')
    }
}
