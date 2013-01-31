package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.tasks.TaskAction
import org.neo4j.graphdb.Relationship
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
		RestIndex<RestNode> index = graphRestAPI.getIndex("artifact")
		IndexHits<RestNode> nodes = index.query(GROUP_ID_AND_ARTIFACT_ID, getGroupAndArtifact(project, "#"))
		nodes.each { RestNode node ->
			listNodeDependants(node)
		} 
	}
	
	def listNodeDependants(RestNode dependency) {
		dependency.getRelationships(Direction.INCOMING).each { Relationship rel ->
			logger.info(rel.getStartNode().getProperty(PRETTY_PRINT) + " -> " +rel.getEndNode().getProperty(PRETTY_PRINT))
		}
	}
}
