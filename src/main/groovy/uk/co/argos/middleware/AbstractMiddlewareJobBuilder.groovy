package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

/**
 * Class for creating a Jenkins Job for common projects using Jenkins DSL
 */
@groovy.transform.InheritConstructors
class AbstractMiddlewareJobBuilder {


    String slaveLabel = 'aws-label'
    String jobName
    String description
    String gitNamespace = "https://gitlab.deveng.io/middleware/"
    String gitBranch = "develop"
    String tasks = "clean install"
    String projectName
    String buildProperties
    String dockerFile
    String cpu = "0.1"
    String memory = "1024"
    String instances = "1"
    String marathonEnv = "PF_STAGING"
    String isDnsHackEnabled = "N"
    boolean isDockerFileProvided = false

    String consulHostAddress = '10.111.11.90'
    String consulPortNo = '80'
    String containerListenerPort = '8080'
    String marathonAWSUrl = "docker.deveng.systems/"
    String marathonPFUrl = "10.185.0.191:5000/"

    def getSonarProperties = { sonarProjectName ->
        """ sonar.projectKey=uk.co.argos.middleware:${sonarProjectName}""" +
                '''
                                    sonar.projectName=$JOB_NAME
                                    sonar.projectVersion=1.0
                                    # Comma-separated paths to directories with sources (required)
                                    sonar.sources=src/main/java
                                    # Language
                                    sonar.language=java
                                    # Encoding of the source files
                                    sonar.sourceEncoding=UTF-8'''
    }
    private String consulPort


    protected void createBuildJob(DslFactory dslFactory) {
        //Get sonar properties
        String sonarProperties = getSonarProperties(projectName + "-middleware-jenkins")
        String gitProjectUrl = gitNamespace + projectName + ".git"
        String gitPropertiesUrl = gitNamespace + "application-properties.git"

        dslFactory.job("${jobName}-build") {
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
            multiscm {
                // Adds a Git SCM source.
                git {
                    branch gitBranch
                    remote {

                        url(gitProjectUrl)
                        credentials('d98fc444-5826-49f9-b1ac-1bca75f8f8fa')
                    }
                }
                // Adds a Git SCM source.Get application-{env}.yml file from master branch
                git {
                    branch 'master'
                    remote {

                        url(gitPropertiesUrl)
                        credentials('d98fc444-5826-49f9-b1ac-1bca75f8f8fa')
                    }
                    extensions {
                        relativeTargetDirectory("./properties")
                    }
                    configure { git ->
                        git / 'extensions' / 'hudson.plugins.git.extensions.impl.SparseCheckoutPaths' / 'sparseCheckoutPaths' {
                            'hudson.plugins.git.extensions.impl.SparseCheckoutPath' {
                                path("/$projectName")

                            }
                        }
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

            // Adds build steps to the jobs.
            steps {
                // Invokes a Maven build.
                maven {
                    goals tasks + " checkstyle:checkstyle"
                    mavenInstallation('mvn-3.3.9')
                    // Specifies the managed Maven settings to be used.
                    providedSettings('userSettings')
                }
                // Injects environment variables into the build.
                environmentVariables {
                    env("PROJECT_NAME", projectName)
                    env("DEPLOY_ENVIRONMENT", marathonEnv.toLowerCase().replace("_", "-"))
                }
                shell(buildProperties)
                environmentVariables {
                    propertiesFile("build.properties")
                }
                if (isDockerFileProvided) {
                    // Runs a shell expected to be on root of project workspace to create dockerFile
                    shell("./createDocker.sh")
                } else {
                    // Runs a shell envProperties to create default dockerFile in workspace
                    shell(dockerFile)
                }

                // Builds and pushes a Docker based project to the Docker registry.
                dockerBuildAndPublish {
                    repositoryName("middleware/$projectName")
                    tag('$DOCKER_IMAGE_TAG')
                    dockerHostURI('tcp://buildfarm.jenkins.internal:2375')
                    dockerRegistryURL('https://docker.deveng.systems')
                }
            }

            // Adds build triggers to the job.
            triggers {
                // Trigger that runs jobs on push notifications from GitLab.
                gitlabPush {
                    buildOnMergeRequestEvents(true)
                    buildOnPushEvents(true)
                    includeBranches(gitBranch)
                    setBuildDescription(true)
                    rebuildOpenMergeRequest('never')
                }
            }

            // Allows direct manipulation of the generated config.XML.
            configure {
                it / builders << 'hudson.plugins.sonar.SonarRunnerBuilder' {
                    properties(sonarProperties)

                }
            }

            // Adds post-build actions to the job.
            publishers {
                // Archives artifacts with each build.
                archiveArtifacts {
                    pattern('build.properties')
                    pattern('Dockerfile')
                    onlyIfSuccessful()
                }
                // Publishes Checkstyle analysis results.
                checkstyle('**/checkstyle-result.xml') {
                    healthLimits(3, 20)
                    thresholdLimit('high')
                    defaultEncoding('UTF-8')
                }
                // Publishes Code Coverage analysis results.
                jacocoCodeCoverage {
                    execPattern('**/jacoco.exec')
                    classPattern('**/classes')
                    exclusionPattern('**/*Test*.class,**/Application.class*,**/config/**,**/generated/**')
                    sourcePattern('**/src/main/java')
                    inclusionPattern('**/*.class')
                }

                triggerDownstreamBuild(delegate)
            }
        }
    }

    /**
     * Creates deploy job.
     * @param dslFactory DSL factory
     * @param name Project name
     * @return null
     */
    protected void createDeployJob(DslFactory dslFactory) {

        //Creates or updates a free style job.
        dslFactory.job("${jobName}-deploy") {
            label(slaveLabel)
            // Allows to parameterize the job.
            parameters {

                stringParam("ENV_NAME")
                stringParam("GIT_TAG")
                stringParam("GIT_REVISION_SHORT")
                stringParam("DOCKER_IMAGE_TAG")
            }



            deployToMarathon(delegate)
            publishers {
                triggerDownstreamTestBuild(delegate)
            }
            buildReleaseJobTrigger(delegate)
        }
    }

    /**
     * Creates release job.
     */
    protected void createReleaseJob(DslFactory dslFactory) {
        // For some reason release needs IP address for GIT
        String gitProjectUrl = "ssh://git@10.185.0.193:2222/middleware/" + projectName + ".git"
        //Creates or updates a free style job.
        dslFactory.job("${jobName}-release") {
            label(slaveLabel)
            // Allows to parameterize the job.
            parameters {
                stringParam("GIT_TAG")
                stringParam("GIT_REVISION_SHORT")
            }

            // Allows a job to check out sources from an SCM provider.
            scm {
                git {
                    branch '$GIT_REVISION_SHORT'
                    remote {
                        url(gitProjectUrl)
                        credentials('a86e1c61-9ae8-4401-bfa6-552b9606eb9a')
                    }
                }
            }

            // Adds post-build actions to the job.
            publishers {
                //Git publisher to create tag in Git
                git {
                    tag('origin', '$GIT_TAG') {
                        create(true)
                        message("STAGING: Jenkins release job build No#" + '$BUILD_NUMBER')
                    }
                }

            }
        }
    }


    def jobParameters(def buildPipelineContext) {
        // Blank implementation
    }

    // Triggers builds( manual or automated) on downstream projects.
    protected void triggerDownstreamBuild(def publisherContext) {

        publisherContext.downstreamParameterized {
            trigger("${jobName}-deploy") {
                parameters {
                    // Reads parameters from a properties file.
                    propertiesFile("build.properties")
                }
            }
        }

    }

    // Triggers builds( manual or automated) on downstream projects.
    def triggerDownstreamTestBuild(def publisherContext) {
        // Blank implementation
    }

    protected void deployToMarathon(def deployContext) {
        String marathonUrl = (marathonEnv.contains("AWS")) ? marathonAWSUrl : marathonPFUrl
        String imageURL = marathonUrl + "middleware/$projectName"

        // Allows direct manipulation of the generated config.XML.
        // Adds values to Argos MicroserviceDeployPlugin
        deployContext.configure {
            it / builders /
                    'uk.co.argo.plugins.deployment.MicroserviceDeployPlugin'(plugin: 'microservice-consul-deployment-plugin@1.0.27') {
                        teamName('middleware')
                        dnsHackRequired(isDnsHackEnabled)
                        marathonJobName("$projectName" + "-" + '${ENV_NAME}')
                        marathonEnvName(marathonEnv)
                        marathonGroupName('middleware' + "/" + '${ENV_NAME}')
                        dockerImageURL(imageURL)
                        dockerImageTag('${DOCKER_IMAGE_TAG}')
                        containerListenPort(containerListenerPort)
                        cpus(cpu)
                        mem(memory)
                        numInstances(instances)
                        publicService('false')
                        consulHost(consulHostAddress)
                        consulPort(consulPortNo)
                        javaOpts('-Xms1228m -Xmx1228m')
                        DEPLOYMENT__WAIT__TIME('60000')

                    }
        }

    }


    def buildReleaseJobTrigger(def buildPipelineContext) {

        // Adds post-build actions to the job.
        buildPipelineContext.publishers {

            // Adds a manual triggers for jobs that require intervention prior to execution.
            buildPipelineTrigger("${jobName}-release") {
                parameters {
                    currentBuild()

                }
            }
        }
    }

}