import uk.co.argos.middleware.*

String pipeBasePath = 'build-jobs-staging'
String namespace = 'middleware'

String viewName = 'STAGE-JOBS-VIEW'

folder(pipeBasePath) {
    primaryView(viewName)
}

/** fix classloader problem using ConfigSlurper in job dsl. */
def slurper = new ConfigSlurper()
slurper.classLoader = this.class.classLoader

//Read projects list file
def jobList = slurper.parse(readFileFromWorkspace('microservices/projects-list-stage.groovy'))

/** Read build properties file. build.properties file contains all pre-defined parameter values. */
def buildProperties = readFileFromWorkspace('scripts/buildPropertiesStage.sh')

/** Read docker file. Docker can build images automatically by reading the instructions from a Dockerfile.
 * A Dockerfile is a text document that contains all the commands a user could call on the command line to assemble an image. */
def dockerFile = readFileFromWorkspace('scripts/dockerFileStage.sh')

/**
 Read project list from projects-list-stage.groovy file and create jenkins jobs for each project
 */
jobList.ciJobs.each { job ->
    job.jobName = pipeBasePath + "/" + job.projectName
    job.buildProperties = buildProperties
    job.dockerFile = dockerFile
    job.build(this)
}
/**
 Create nested view of the Jobs created above.
 */
nestedView("$pipeBasePath/$viewName") {
    description('Shows the service build pipelines')
    columns {
        status()
        weather()
    }
    views {
        def innerNestedView = delegate
        jobList.ciJobs.each { job ->
            innerNestedView.buildPipelineView("${job.projectName}") {
                title("${job.projectName}")
                consoleOutputLinkStyle(OutputStyle.NewWindow)
                alwaysAllowManualTrigger(true)
                displayedBuilds(5)
                filterBuildQueue(false)
                filterExecutors(false)
                refreshFrequency(3)
                selectedJob("$pipeBasePath/${job.projectName}-build")
                triggerOnlyLatestJob(false)
                alwaysAllowManualTrigger(true)
                showPipelineParameters(true)
                showPipelineParametersInHeaders(false)
                showPipelineDefinitionHeader(false)
                startsWithParameters(true)
            }

        }
    }
}