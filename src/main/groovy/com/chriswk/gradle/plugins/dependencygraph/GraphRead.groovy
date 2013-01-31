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
        configureAbstractGraphTask(project, this)
        configureGraphDatabase(this)
        logger.info("Finding dependencies for ${project.getGroup()} ${project.getName()}")
    	listDependants(findNodes())
	}

    def findNodes() {
        RestIndex<RestNode> index = graphRestAPI.getIndex("artifact")
        IndexHits<RestNode> nodes = index.query(GROUP_ID_AND_ARTIFACT_ID, getGroupAndArtifact(project, "#"))
        nodes
    }
	def listDependants(IndexHits<RestNode> nodes) {
        println "${nodes.size()} nodes found with ${getGroupAndArtifact(project, "#")}"
		nodes.each { RestNode node ->
			listNodeDependants(node)
		} 
	}
	
	def listNodeDependants(RestNode node) {
        println("Finding relationships for ${node.getId()} - ${node.getProperty(GROUP_ID_AND_ARTIFACT_ID)}}")
		node.getRelationships(Direction.INCOMING).each { Relationship rel ->
			logger.info(rel.getStartNode().getProperty(PRETTY_PRINT) + " -> " +rel.getEndNode().getProperty(PRETTY_PRINT))
		}
	}
}
