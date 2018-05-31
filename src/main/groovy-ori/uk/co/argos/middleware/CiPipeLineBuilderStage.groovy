/**
 * Class responsible for creating Pipeline
 */

package uk.co.argos.middleware

import javaposse.jobdsl.dsl.DslFactory

/**
 * Class for creating a Maven build
 */
@groovy.transform.InheritConstructors
class CiPipeLineBuilderStage extends AbstractMiddlewareJobBuilder{

    /**
     * Build method to create all jobs.
     */
    def build(DslFactory dslFactory) {
        createBuildJob(dslFactory)
        createDeployJob(dslFactory)
        createReleaseJob(dslFactory)
    }

    def jobParameters (def buildPipelineContext)  {
        buildPipelineContext.parameters {
            choiceParam('SELECT_ENVIRONMENT', ['uat2', 'uat1', 'sit2', 'sit1', 'pt', 'dt'], 'environment list')
        }
    }
}