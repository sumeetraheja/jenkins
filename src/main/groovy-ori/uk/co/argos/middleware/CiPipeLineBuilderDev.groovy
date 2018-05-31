/**
 * Class responsible for creating Pipeline
 */

package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Class for creating a Maven build,deploy jobs for Dev environment
 */
@groovy.transform.InheritConstructors
class CiPipeLineBuilderDev extends AbstractMiddlewareJobBuilder {

    boolean runSoapUiTests = false
    String healthCheckScript

    /** Build method to create build and deploy jobs*/
    def build(DslFactory dslFactory) {
        createBuildJob(dslFactory)
        createDeployJob(dslFactory)
        if (runSoapUiTests) {
            createTestJob(dslFactory)
        }
    }

    def buildReleaseJobTrigger(def buildPipelineContext) {
        // Blank implementation
    }
    // Triggers builds( manual or automated) on downstream projects.
    def triggerDownstreamTestBuild(def publisherContext) {
        if (runSoapUiTests) {
            publisherContext.downstreamParameterized {
                trigger("${jobName}-test") {
                    parameters {
                        currentBuild()
                    }
                }
            }
        }
    }

    protected void createTestJob(DslFactory dslFactory) {
        //Get sonar properties

        String gitProjectUrl = gitNamespace + projectName + ".git"
        String healthCheckUrl = (marathonEnv.contains("AWS")) ? "http://" + projectName + "-dev.service.eu-west-1.dev.deveng.systems/hrg-private/health" : "http://" + projectName + "-dev.privateagents.parkfarm.staging.deveng.systems/hrg-private/health"

        dslFactory.job("${jobName}-test") {
            it.description this.description
            label(slaveLabel)
            jobParameters(delegate)

            // Adds custom properties to the job.
            properties {
                zenTimestamp("yyyyMMddHHmmss")
            }

            // Manages how long to keep records of the builds.
            logRotator {
                numToKeep 10
            }
            // Allows a job to check out sources from multiple SCM providers.
            scm {
                // Adds a Git SCM source.
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
            publishers {
                // Archives artifacts with each build.
                archiveArtifacts {
                    pattern('**/*-results.json')
                }
            }
            // Adds build steps to the jobs.
            steps {
                groovyCommand(healthCheckScript) {
                    groovyInstallation('groovy-2.4.7')
                    scriptParam(healthCheckUrl)
                }
                // Invokes a Maven build.
                maven {
                    goals " clean -Psoapui test"
                    mavenInstallation('mvn-3.3.9')
                    // Specifies the managed Maven settings to be used.
                    providedSettings('userSettings')
                }


            }
        }
    }
}