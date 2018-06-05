package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

/**
 * Class for creating a Jenkins Job for common projects using Jenkins DSL
 */
class CommonJobBuilder {

    String gitBranch = "master"

    String slaveLabel = 'aws-label'
    String jobName
    String description
    String gitNamespace = "https://gitlab.deveng.io/middleware/"
    String tasks = "clean install"
    String projectName
    String buildProperties
    String dockerFile
    String cpu = "0.1"
    String memory = "1024"
    String instances = "1"
    String marathonEnv = "PF_STAGING"
    String isDnsHackEnabled = "N"



    /** Build method to create build and release jobs*/
    def build(DslFactory dslFactory) {
        createBuildJob(dslFactory)
        //createReleaseJob(dslFactory)

    }


    /**
     * Creates build job in Jenkins.
     * @param dslFactory DSL factory
     * @param name Jenkins job name with folder path. ex: build-jobs-commons/{job_name}*
     * @param gitUrl GIT project url.
     */
    void createBuildJob(DslFactory dslFactory) {

        //Get sonar properties.
        String sonarProperties = getSonarProperties(projectName + "-middleware-jenkins")
        String gitProjectUrl =  gitNamespace + projectName + ".git"

        //Create maven job
        dslFactory.pipelineJobjob("${jobName}-build") {
            it.description this.description
        }


    }



/**
 * Release job to add jar file to Nexus repository.
 * @param dslFactory DSL factory
 * @param name Jenkins job name with folder path. ex: build-jobs-commons/{job_name}*
 * @param gitUrl GIT project url.
 */
    void createReleaseJob(DslFactory dslFactory) {
        String gitProjectUrl =  gitNamespace + projectName + ".git"
        //Creates or updates a job to build a Maven project.
        dslFactory.job("${jobName}-release") {
            // Allows a job to check out sources from an SCM provider.
            scm {
                // Adds a Git SCM source.
                git {
                    branch gitBranch
                    remote {
                        // Sets the remote URL.
                        url(gitProjectUrl)
                        // Sets credentials for authentication with the remote repository.
                        credentials('d98fc444-5826-49f9-b1ac-1bca75f8f8fa')
                    }
                    // Adds additional behaviors.
                    extensions {
                        // Checkout the revision to build as HEAD on this branch.
                        localBranch gitBranch
                    }
                }
            }

            // Adds pre/post actions to the job.
            wrappers {
                // Sets the display name of a build.
                buildName('#${BUILD_NUMBER}')
                // Deletes files from the workspace before the build starts.
                preBuildCleanup {
                    deleteDirectories(true)
                }
            }

            steps {
                // Invokes a Maven build.
                maven {
                    // Do maven release . To avoid infinite loop by maven-release plugin use keyword [ci skip]
                    goals "-Darguments=-DskipTests -DscmCommentPrefix='  [ci skip] ' -B release:clean release:prepare release:perform"
                    mavenInstallation('mvn-3.3.9')
                    // Use managed Maven settings.
                    providedSettings('userSettings')
                }
            }
        }
    }

}