package com.chriswk.gradle.plugins.dependencygraph


class DependencyGraphPluginConvention {
    Integer graphServerPort = 7474
    String graphServerUrl = "http://localhost"
    String graphServerPath = "/db/data"
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

    void setGraphServerUsername(String graphServerUsername) {
        this.graphServerUsername = graphServerUsername
    }

    String getGraphServerPassword() {
        return graphServerPassword
    }

    void setGraphServerPassword(String graphServerPassword) {
        this.graphServerPassword = graphServerPassword
    }

    @Override
    public String toString() {
        return "DependencyGraphPluginConvention{" +
                "graphServerPort=" + graphServerPort +
                ", graphServerUrl='" + graphServerUrl + '\'' +
                ", graphServerPath='" + graphServerPath + '\'' +
                ", graphServerUsername='" + graphServerUsername + '\'' +
                ", graphServerPassword='" + graphServerPassword + '\'' +
                '}';
    }
}
