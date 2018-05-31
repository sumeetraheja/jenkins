/**
 * Class responsible for creating Pipeline
 */

package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

/**
 * Class for creating a Maven build
 */
@groovy.transform.InheritConstructors
class CiPipeLineBuilderCommonAllEnv extends AbstractMiddlewareJobBuilder {

    String consulHostAddress = 'consul.masters.parkfarm.staging.deveng.systems'

    String gitBranch = "master"

    String marathonEnv = "PF_STAGING"
    /**
     * Build method to create all jobs.
     */
    def build(DslFactory dslFactory) {
        createBuildJob(dslFactory)
        createDeployJob(dslFactory)
    }

    protected void deployToMarathon (def deployContext)  {
        String marathonUrl = (marathonEnv.contains("AWS")) ? marathonAWSUrl  : marathonPFUrl
        String imageURL = marathonUrl + "middleware/$projectName"
        String groupName = "middleware/$projectName"
        // Allows direct manipulation of the generated config.XML.
        // Adds values to Argos MicroserviceDeployPlugin
        deployContext.configure {
            it / builders /
                    'uk.co.argo.plugins.deployment.MicroserviceDeployPlugin'(plugin: 'microservice-consul-deployment-plugin@1.0.27') {
                        teamName('middleware')
                        dnsHackRequired(isDnsHackEnabled)
                        if (marathonEnv.contains("DEV")) {
                            marathonJobName("$projectName" + "-" + 'dev')
                        }else   if (marathonEnv.contains("STAGING")) {
                            marathonJobName("$projectName" + "-" + 'staging')
                        }else   if (marathonEnv.contains("PROD")) {
                            marathonJobName("$projectName" + "-" + 'prod')
                        }
                        if (marathonEnv.contains("PF_DEV")){
                            marathonEnvName("PF_STAGING")
                        }else {
                            marathonEnvName(marathonEnv)
                        }
                        marathonGroupName(groupName)
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