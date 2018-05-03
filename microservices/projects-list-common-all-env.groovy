import uk.co.argos.middleware.*

ciJobs = [
        new CiPipeLineBuilderCommonAllEnv([projectName: "kafka-topic-watch-ui", marathonEnv: 'AWS_DEV',  description: 'To search and view kafka messages']),
        new CiPipeLineBuilderCommonAllEnv([projectName: "kafka-topic-watch-ui", marathonEnv: 'AWS_STAGING',  description: 'To search and view kafka messages']),
        new CiPipeLineBuilderCommonAllEnv([projectName: "kafka-topic-watch-ui", marathonEnv: 'AWS_PROD',  description: 'To search and view kafka messages']),
        new CiPipeLineBuilderCommonAllEnv([projectName: "kafka-topic-watch-ui", marathonEnv: 'PF_DEV',  description: 'To search and view kafka messages']),
        new CiPipeLineBuilderCommonAllEnv([projectName: "kafka-topic-watch-ui", marathonEnv: 'PF_STAGING',  description: 'To search and view kafka messages']),
        new CiPipeLineBuilderCommonAllEnv([projectName: "kafka-topic-watch-ui", marathonEnv: 'PF_PROD',  description: 'To search and view kafka messages'])
]