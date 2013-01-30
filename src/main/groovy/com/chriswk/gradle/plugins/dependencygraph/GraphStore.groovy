package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.tasks.TaskAction
import org.neo4j.rest.graphdb.index.RestNodeIndex


class GraphStore extends AbstractDependencyGraphTask {


    @TaskAction
    protected void store() {
        logger.info("Storing dependencies for [groupId: ${project.getGroup()}] and [artifactId: ${project.getName()}] with node name ${nodeName()}")
        graphRestAPI.beginTx()
        graphRestAPI.getOrCreateNode()
    }


    String nodeName() {
        "${project.getGroup()}_${project.getName()}"
    }
}
