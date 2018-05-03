/**
 * Class responsible for creating Pipeline
 */

package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

/**
 * Class for creating a Maven build
 */
@groovy.transform.InheritConstructors
class CiPipeLineBuilderProd extends AbstractMiddlewareJobBuilder {

    String consulHostAddress = 'consul.masters.parkfarm.staging.deveng.systems'

    String gitBranch = "master"

    String marathonEnv = "PF_PROD"
    /**
     * Build method to create all jobs.
     */
    def build(DslFactory dslFactory) {
        createBuildJob(dslFactory)
        createDeployJob(dslFactory)
        createReleaseJob(dslFactory)
    }

    // Triggers manual builds on downstream projects.
    public void triggerDownstreamBuild (def publisherContext)  {

        publisherContext.buildPipelineTrigger("${jobName}-deploy") {
            parameters {
                // Reads parameters from a properties file.
                propertiesFile("build.properties")
            }
        }

    }

    protected void deployToMarathon (def deployContext)  {
        String marathonUrl = (marathonEnv.contains("AWS")) ? marathonAWSUrl  : marathonPFUrl
        String imageURL = marathonUrl + "middleware/$projectName"

        // Allows direct manipulation of the generated config.XML.
        // Adds values to Argos MicroserviceDeployPlugin
        deployContext.configure {
            it / builders /
                    'uk.co.argo.plugins.deployment.MicroserviceDeployPlugin'(plugin: 'microservice-consul-deployment-plugin@1.0.27') {
                        teamName('middleware')
                        dnsHackRequired(isDnsHackEnabled)
                        marathonJobName("$projectName" )
                        marathonEnvName(marathonEnv)
                        marathonGroupName('middleware')
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

}