buildscript {
	repositories {
		mavenCentral()
		mavenLocal()
		maven {
			name "Neo4j Release"
		    url "http://m2.neo4j.org/"
		}
		maven {
			name "neo4j Snapshots"
		    url "http://m2.neo4j.org/snapshots"
		}
	}
	dependencies {
		classpath "com.chriswk.gradle:gradle-dependency-graph-plugin:0.1"
	}
}

group = "com.chriswk.gradle"
version = "0.1"
apply plugin: 'dependencyGraphPlugin'
apply plugin: 'java'

dependencies {
	testCompile "junit:junit:4.11"
}

task wrapper(type: Wrapper) {
	gradleVersion = 1.4
}
