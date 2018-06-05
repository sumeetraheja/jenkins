package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

/**
 * Class for creating a Jenkins Job for common projects using Jenkins DSL
 */
class CommonJobBuilderOriginal {

    String gitBranch = "master"

    /** Build method to create build and release jobs*/
    def build(DslFactory dslFactory) {
        createBuildJob(dslFactory)
        createReleaseJob(dslFactory)

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
        dslFactory.job("${jobName}-build") {
            it.description this.description
            label(slaveLabel)
            properties {
                zenTimestamp("yyyyMMddHHmmss")
            }
            // Manages how long to keep records of the builds.
            logRotator {
                numToKeep 10
            }


            // Allows a job to check out sources from an SCM provider.
            scm {
                git {
                    branch gitBranch
                    remote {
                        url(gitProjectUrl)
                        credentials('d98fc444-5826-49f9-b1ac-1bca75f8f8fa')
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

            configure {

                it / builders << 'hudson.plugins.sonar.SonarRunnerBuilder' {
                    properties(sonarProperties)

                }
                /** This will abort Build Job when we do maven Release */

                it / 'buildWrappers' / 'ruby-proxy-object' {
                    'ruby-object' ('ruby-class': 'Jenkins::Tasks::BuildWrapperProxy', pluginid: 'ci-skip') {

                        object('ruby-class': 'CiSkipWrapper', pluginid: 'ci-skip') {
                            ci__skip(pluginid: 'ci-skip', 'ruby-class': 'NilClass')
                        }

                        pluginid(pluginid: 'ci-skip', 'ruby-class': 'String','ci-skip' )

                    }
                }
            }

            steps {
                // Invokes a Maven build.
                maven {
                    goals tasks + " checkstyle:checkstyle"
                    mavenInstallation('mvn-3.3.9')
                    // Specifies the managed Maven settings to be used.
                    providedSettings('userSettings')
                }
            }

            // Adds build triggers to the job. This configuration triggers job automatically whenever git commit happens. But Webhook must be added to Git project.
            triggers {
                gitlabPush {
                    buildOnPushEvents(true)
                    includeBranches(gitBranch)
                    setBuildDescription(true)
                }
            }
            // Adds post-build actions to the job.
            publishers {

                // Publishes Checkstyle analysis results.
                checkstyle('**/checkstyle-result.xml') {
                    healthLimits(3, 20)
                    thresholdLimit('high')
                    defaultEncoding('UTF-8')
                }

                // Triggers parameterized builds on other projects.
                downstreamParameterized {
                    trigger("${jobName}-release") {
                        parameters {
                            booleanParam("Release", true)
                        }

                    }
                }
            }
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