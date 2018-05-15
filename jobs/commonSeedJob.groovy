import uk.co.argos.middleware.*

String pipeBasePath = 'middleware-jobs-commons'

String viewName = 'COMMONS-VIEW'

folder(pipeBasePath) {
    primaryView(viewName)
}


/** fix classloader problem using ConfigSlurper in job dsl. */
def slurper = new ConfigSlurper()
slurper.classLoader = this.class.classLoader
def jobList = slurper.parse(readFileFromWorkspace('microservices/projects-list-common.groovy'))

/**
 Read project list from projects-list-common.groovy file and create jenkins jobs for each project
 */
jobList.ciJobs.each { job ->
    job.jobName = pipeBasePath + "/" + job.projectName
    job.build(this)
}
/**
 Add all common jenkins jobs in a nested view.
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