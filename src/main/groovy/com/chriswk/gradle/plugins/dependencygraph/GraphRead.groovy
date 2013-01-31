package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.tasks.TaskAction

import org.neo4j.rest.graphdb.entity.RestNode
import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.index.IndexHits
import org.neo4j.rest.graphdb.index.RestIndex

class GraphRead extends AbstractDependencyGraphTask {
    @TaskAction
    protected void read() {
        logger.info("Finding dependencies for ${project.getGroup()} ${project.getName()}")
    	listDependants()
	}

	def listDependants() {
		RestIndex<RestNode> index = restAPI.getIndex("artifact")
		IndexHits<RestNode> nodes = index.query(GROUP_ID_AND_ARTIFACT_INDEX, getGroupAndArtifact())
		nodes.each { node ->
			listNodeDependants(node)
		} 
	}
	
	def listNodeDependants(RestNode dependency) {
		dependency.getRelationShips(Direction.INCOMING).each {
			logger.info(r.getStartNode().getProperty(PRETTY_PRINT) + " -> " +r.getEndNode().getProperty(PRETTY_PRINT))
		}
	}
}
