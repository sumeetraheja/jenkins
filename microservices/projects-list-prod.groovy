import uk.co.argos.middleware.*

ciJobs = [

		// K
        new CiPipeLineBuilderProd([projectName: "kafka-jsrabbitmq-daventry-annexe-ms", marathonEnv: 'AWS_PROD',  memory: '2048', isDnsHackEnabled: 'Y', description: 'JS RabbitMQ <-> Kafka Link for Daventy Annexe', isDockerFileProvided: 'TRUE']),

        // P
		new CiPipeLineBuilderProd([projectName: "prm-kafka-productsupplier-warranties-ms",  description: 'prm-kafka-productsupplier-warranties-ms']),

        new CiPipeLineBuilderProd([projectName: "pim-kafka-product-data-publisher-ms",  isDnsHackEnabled: 'Y', description: 'pim-kafka-product-data-publisher-ms', isDockerFileProvided: 'TRUE']),
		
		// T
        new CiPipeLineBuilderProd([projectName: "triceps-kafka-daventry-annexe-ms", marathonEnv: 'PF_PROD',  memory: '2048',  description: 'Kafka<->MQ (Triceps) Link for Daventry Annexe', isDockerFileProvided: 'TRUE'])
		
]