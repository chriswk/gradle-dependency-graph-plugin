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
        def String dep
        if (project.getProperties().get("graphDep") != null && !(((String) project.getProperties().get("graphDep")).isEmpty())) {
            dep = project.getProperties().get("graphDep")
        } else {
            dep = getGroupAndArtifact(project, "#")
        }
        logger.info("Finding dependencies for ${dep}")
    	listDependants(findNodes(dep))
	}

    def findNodes(String artifactId) {
        RestIndex<RestNode> index = graphRestAPI.getIndex("artifact")
        IndexHits<RestNode> nodes = index.query(GROUP_ID_AND_ARTIFACT_ID, artifactId)
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
			println(rel.getStartNode().getProperty(PRETTY_PRINT) + " -> " +rel.getEndNode().getProperty(PRETTY_PRINT))
		}
	}
}
