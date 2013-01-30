package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.Convention

class DependencyGraphPlugin implements Plugin<Project> {

    @Override
    void apply(Project p) {
        DependencyGraphPluginConvention dgPlugin = new DependencyGraphPluginConvention()
        Convention c = p.getConvention()
        c.plugins.put("dependencyGraph", dgPlugin)
        configureMappingRules(p, dgPlugin)
        configureReadDepTask(p)
        configureStoreDepTask(p)
    }

    def configureMappingRules(final Project project, final DependencyGraphPluginConvention dependencyGraphPluginConvention) {
        project.getTasks().withType(AbstractDependencyGraphTask.class, { AbstractDependencyGraphTask task ->
            configureAbstractGraphTask(project, dependencyGraphPluginConvention, task)
        })
        project.getTasks().withType(AbstractDependencyGraphTask.class, { AbstractDependencyGraphTask task ->
            configureGraphDatabase(task)
        })
    }



    def configureReadDepTask(Project project) {
        GraphRead graphRead = project.getTasks().add("graphRead", GraphRead.class)
        graphRead.setDescription("Finds artifacts that uses me as a dependency")
        graphRead.setGroup("Dependency Management")
    }

    def configureStoreDepTask(Project project) {
        GraphStore graphStore = project.getTasks().add("graphStore", GraphStore.class)
        graphStore.setDescription("Saves me and all artifacts I use")
        graphStore.setGroup("Dependency Management")

    }

}
