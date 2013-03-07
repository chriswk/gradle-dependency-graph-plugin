package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction
import org.gradle.api.artifacts.Dependency
import org.neo4j.graphdb.DynamicRelationshipType
import org.neo4j.rest.graphdb.index.RestIndex
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Direction

class GraphStore extends AbstractDependencyGraphTask {
    RestIndex<Node> index;

    @TaskAction
    protected void store() {
        configureAbstractGraphTask(project, this)
        configureGraphDatabase(this)
        logger.info("Storing dependencies for ${getGroupAndArtifact(project, "#")} - [groupId: ${project.getGroup()}] and [artifactId: ${project.getName()}]")
    	Map<String, String> config = new HashMap<>()
		config.put("type", "exact")
		config.put("provider", "lucene")
		config.put("to_lower_case", "false")
		
		index = graphRestAPI.createIndex(Node.class, ARTIFACT, config)
		
		getGraphDependencies()
	}
	
	def getGraphDependencies() {
        logger.info("Getting dependencies........")
		Node projectNode = makeProjectNode()

        projectNode.getRelationships(Direction.OUTGOING).each { rel ->
			rel.delete()
		}
		//Register parent dependency
		if(project.getParent()) {
			registerDependency(projectNode, project.getParent(), "parent")
		}
		project.getConfigurations().each{ Configuration config ->
            logger.info("Registering for ${config.name}")
            config.getDependencies().each{ Dependency dep ->
			    logger.info("Registering ${dep.name}")
                registerDependency(projectNode, dep, config.getName())
		    }
        }
		
	}

    def registerDependency(Node projectNode, Dependency dependency, String type) {
        Node dep = makeNode(dependency)
        projectNode.createRelationshipTo(dep, DynamicRelationshipType.withName(type))
        logger.info("Registered dependency to ${getFullName(dependency, ":")}")
    }

    def registerDependency(Node projectNode, Project project, String type) {
        Node dep = makeNode(project)
        projectNode.createRelationshipTo(dep, DynamicRelationshipType.withName(type))
        logger.info("Registered dependency to ${getFullName(project, ":")}")
    }

    Node makeNode(Dependency d) {
        Node depNode = graphRestAPI.getOrCreateNode(index, COMPLETE_ID, getFullName(d, "#"), getPropertyMap(d))
        index.add(depNode, GROUP_ID_AND_ARTIFACT_ID, getGroupAndArtifact(d, "#"))
        return depNode
    }

    Node makeNode(Project p) {
        Node projNode = graphRestAPI.getOrCreateNode(index, COMPLETE_ID, getFullName(p, "#"), getPropertyMap(p))
        index.add(projNode, GROUP_ID_AND_ARTIFACT_ID, getGroupAndArtifact(p, "#"))
        return projNode
    }

	Node makeProjectNode() {
        Node projectNode = graphRestAPI.getOrCreateNode(index, COMPLETE_ID, getFullName(project, "#"), getPropertyMap(project))
        graphRestAPI.getIndex(ARTIFACT).add(projectNode, GROUP_ID_AND_ARTIFACT_ID, getGroupAndArtifact(project, "#"))
        graphRestAPI.getIndex(ARTIFACT).add(projectNode, COMPLETE_ID, getFullName(project, "#"))
        return projectNode
	}

}
