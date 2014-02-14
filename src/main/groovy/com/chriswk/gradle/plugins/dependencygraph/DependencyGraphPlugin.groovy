package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.ExtensionContainer

class DependencyGraphPlugin implements Plugin<Project> {

    @Override
    void apply(Project p) {

        p.extensions.create("dependencyGraph", DependencyGraphExtension)
        configureReadDepTask(p)
        configureStoreDepTask(p)
    }

    def configureReadDepTask(Project project) {
        GraphRead graphRead = project.getTasks().create("graphRead", GraphRead.class)
        graphRead.setDescription("Finds artifacts that uses me as a dependency")
        graphRead.setGroup("Dependency Management")
    }

    def configureStoreDepTask(Project project) {
        GraphStore graphStore = project.getTasks().create("graphStore", GraphStore.class)
        graphStore.setDescription("Saves me and all artifacts I use")
        graphStore.setGroup("Dependency Management")

    }

}
