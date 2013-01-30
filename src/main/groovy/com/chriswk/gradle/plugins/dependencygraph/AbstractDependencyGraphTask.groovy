package com.chriswk.gradle.plugins.dependencygraph

import org.gradle.api.Project
import org.gradle.api.internal.ConventionTask
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.rest.graphdb.RestAPI
import org.neo4j.rest.graphdb.RestAPIFacade
import org.neo4j.rest.graphdb.RestGraphDatabase


class AbstractDependencyGraphTask extends ConventionTask {
    GraphDatabaseService graphDatabaseService
    RestAPI graphRestAPI
    Integer graphServerPort
    String graphServerUrl
    String graphServerPath
    String graphServerUserName
    String graphServerPassword

    GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService
    }

    void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService
    }

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

    String getGraphServerUserName() {
        return graphServerUserName
    }

    void setGraphServerUserName(String graphServerUserName) {
        this.graphServerUserName = graphServerUserName
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

    def configureAbstractGraphTask(final Project project, final DependencyGraphPluginConvention convention, AbstractDependencyGraphTask task) {
        logger.info(convention.toString())
        task.conventionMapping.map("graphServerPort", {
            convention.getGraphServerPort()
        })
        task.conventionMapping.map("graphServerUrl", {
            convention.getGraphServerUrl()
        })
        task.conventionMapping.map("graphServerPath", {
            convention.getGraphServerPath()
        })
        task.conventionMapping.map("graphServerUserName", {
            convention.getGraphServerUsername()
        })
        task.conventionMapping.map("graphServerPassword", {
            convention.getGraphServerPassword()
        })
    }

    def configureGraphDatabase(AbstractDependencyGraphTask task) {
        def url = findServerUrl(task)
        logger.info("Creating with url [${url}] with username ${getGraphServerUserName()} and password ${getGraphServerPassword()}")
        if (getGraphServerUserName() == null || getGraphServerUserName().empty) {
            task.conventionMapping.map("graphDatabaseService", {
                new RestGraphDatabase(url)
            })
            task.conventionMapping.map("graphRestAPI", {
                new RestAPIFacade(url)
            })
        } else {
            task.conventionMapping.map("graphDatabaseService", {
                new RestGraphDatabase(url, task.getGraphServerUserName(), task.getGraphServerPassword())
            })
            task.conventionMapping.map("graphRestAPI", {
                new RestAPIFacade(url, task.getGraphServerUserName(), task.getGraphServerPassword())
            })
        }
    }
}
