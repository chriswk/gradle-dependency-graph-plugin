package com.chriswk.gradle.plugins.dependencygraph


class DependencyGraphExtension {
    def Integer graphServerPort = 7474
    def String graphServerUrl = "http://localhost"
    def String graphServerPath = "/db/data"
    def String graphServerUsername
    def String graphServerPassword
}
