# Jenkins Job DSL Maven 

It is responsible for creating CI Jobs in Jenkins , and will be used to create Jenkins Pipeline

An example [Job DSL](https://github.com/jenkinsci/job-dsl-plugin) project that uses Maven for building and testing

## File structure

    .
    ├── jobs                    # DSL script files
    ├── resources               # resources for DSL scripts
    ├── src
    │   ├── main
    │   │   ├── groovy          # support classes
    │   │   └── resources
    │   │       └── idea.gdsl   # IDE support for IDEA
    │   └── test
    │       └── groovy          # specs
    └── pom.xml                 # build file

# Script Examples   