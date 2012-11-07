/*
* Copyright 2011 Kenshoo.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.kenshoo.watership

import org.gradle.api.Plugin
import org.gradle.api.Project

class PackagingPlugin implements Plugin<Project> {
    public static final String STAGE_PATH = "/package-stage"

    @Override
    void apply(Project project) {
        project.extensions.create('debian', PackagingPluginExtension)
        project.debian.sourceDirs = [project.libsDir, project.sourceSets.main.output.resourcesDir]
        def stageDir = project.buildDir.absolutePath + STAGE_PATH
        def root = project.rootProject
        project.task('stagePackageFiles', group: 'Build', type: StagePackageFilesTask, dependsOn: root.tasks.build)
        def debTask = project.task('debian', group: 'Build', type: DebianTask, dependsOn: root.tasks.stagePackageFiles)
        debTask.stageDir = new File(stageDir)

        def rpmTask = project.task('rpm', group: 'Build', type: RpmTask, dependsOn: root.tasks.stagePackageFiles)
        rpmTask.stageDir = new File(stageDir)
    }

}
