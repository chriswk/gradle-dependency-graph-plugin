Gradle dependency mapper
========================
Stores dependency relationships in a graph database (neo4j)
-----------------------------------------------------------

Local environment:
-----------------
* install neo4j http://www.neo4j.org/develop#install
* checkout this repo
* gradlew install
* go to another gradle project and add the following to build.gradle
``` groovy
  buildscript {
    repositories {
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
  
  apply plugin: 'dependencyGraphPlugin'
  
```

* For full example of working build file see [here](build.gradle.MD)

* to find dependencies of your current project
```
  gradlew graphRead
```

* to find dependencies of for instance spring-core
```
 gradlew graphRead -PgraphDep="org.springframework#spring-core"
```

* When installing into repo make sure to run
```
 gradlew graphStore
```

* This will update your graph database with all dependencies of the current project
* They will be tagged with which configuration they come from

* Override server settings by creating a dependencyGraph closure in your build script
* Defaults are in the code example
``` groovy
  dependencyGraph {
    graphServerUrl = "http://localhost"
    graphServerPort = 7474
    graphServerPath = "/db/data"
    graphServerUsername
    graphServerPassword
  }
```

* Username and password only if needed 