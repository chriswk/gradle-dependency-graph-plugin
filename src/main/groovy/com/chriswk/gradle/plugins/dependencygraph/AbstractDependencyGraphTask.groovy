package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.AbstractTask
import org.neo4j.rest.graphdb.RestAPI
import org.neo4j.rest.graphdb.RestAPIFacade

class AbstractDependencyGraphTask extends AbstractTask {
	String GROUP_ID_AND_ARTIFACT_ID = "groupIdAndArtifactId"
	String PRETTY_PRINT = "prettyPrint"
	String COMPLETE_ID = "completeId"
	String ARTIFACT = "artifact"
    RestAPI graphRestAPI
    Integer graphServerPort
    String graphServerUrl
    String graphServerPath
    String graphServerUsername
    String graphServerPassword


    Integer getGraphServerPort() {
        return graphServerPort
    }

    void setGraphServerPort(Integer graphServerPort) {
        this.graphServerPort = graphServerPort
    }

    String getGraphServerUrl() {
        return graphServerUrl
    }

    void setGraphServerUrl(String graphServerUrl) {
        this.graphServerUrl = graphServerUrl
    }

    String getGraphServerPath() {
        return graphServerPath
    }

    void setGraphServerPath(String graphServerPath) {
        this.graphServerPath = graphServerPath
    }

    String getGraphServerUsername() {
        return graphServerUsername
    }

    void setGraphServerUsername(String graphServerUserName) {
        this.graphServerUsername = graphServerUserName
    }

    String getGraphServerPassword() {
        return graphServerPassword
    }

    void setGraphServerPassword(String graphServerPassword) {
        this.graphServerPassword = graphServerPassword
    }

    RestAPI getGraphRestAPI() {
        return graphRestAPI
    }

    void setGraphRestAPI(RestAPI graphRestAPI) {
        this.graphRestAPI = graphRestAPI
    }

    String findServerUrl(AbstractDependencyGraphTask task) {
        "${task.getGraphServerUrl()}:${task.getGraphServerPort()}${task.getGraphServerPath()}"
    }

    def configureAbstractGraphTask(final Project project, AbstractDependencyGraphTask task) {
        task.setGraphServerUrl(project.dependencyGraph.graphServerUrl)
        task.setGraphServerPath(project.dependencyGraph.graphServerPath)
        task.setGraphServerPort(project.dependencyGraph.graphServerPort)
        task.setGraphServerUsername(project.dependencyGraph.graphServerUsername)
        task.setGraphServerPassword(project.dependencyGraph.graphServerPassword)
    }

    def configureGraphDatabase(AbstractDependencyGraphTask task) {
        def url = findServerUrl(task)
        logger.info("Creating with url [${url}] with username ${getGraphServerUsername()} and password ${getGraphServerPassword()}")
        if (getGraphServerUsername() == null || getGraphServerUsername().empty) {
            graphRestAPI = new RestAPIFacade(url)
        } else {
            graphRestAPI = new RestAPIFacade(url, task.getGraphServerUsername(), task.getGraphServerPassword())
        }
    }

    Map<String, Object> getPropertyMap(Project p) {
        [
            "name": getFullName(p, ":"),
            "groupId": p.getGroup(),
            "artifactId": p.getName(),
            "version": p.getVersion(),
            "groupIdAndArtifactId": getGroupAndArtifact(p, "#"),
            "prettyPrint": getFullName(p, ":")
        ]
    }

    static String getFullName(Project p, String seperator) {
        [p.getGroup(), p.getName(), p.getVersion()].join(seperator)
    }

    static String getFullName(Dependency d, String separator) {
        [d.getGroup(), d.getName(), d.getVersion()].join(separator)
    }

    static String getGroupAndArtifact(Project p, String seperator) {
        [p.getGroup(), p.getName()].join(seperator)
    }

    static String getGroupAndArtifact(Dependency d, String seperator) {
        [d.getGroup(), d.getName()].join(seperator)
    }


    Map<String, Object> getPropertyMap(Dependency d) {
        [
                "name": getFullName(d, ":"),
                "groupId": d.getGroup(),
                "artifactId": d.getName(),
                "version": d.getVersion(),
                "groupIdAndArtifactId": getGroupAndArtifact(d, "#"),
                "prettyPrint": getFullName(d, ":")
        ]
    }
}
