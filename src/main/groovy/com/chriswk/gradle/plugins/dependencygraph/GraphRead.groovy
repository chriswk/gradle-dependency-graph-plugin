package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.tasks.TaskAction
import org.neo4j.graphdb.Node
import org.neo4j.rest.graphdb.RestAPI
import org.neo4j.rest.graphdb.entity.RestNode

class GraphRead extends AbstractDependencyGraphTask {
    @TaskAction
    protected void read() {
        logger.info("Finding dependencies for ${project.getGroup()} ${project.getName()}")
        graphRestAPI.beginTx()
        RestNode serverSideNode = graphRestAPI.getIndex("artifact").get("artifactId", project.getName())
        logger.info("Found node ${serverSideNode.toString()}")
    }
}
