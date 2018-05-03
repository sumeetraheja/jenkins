import uk.co.argos.middleware.*

ciJobs = [
		// A
		new CiPipeLineBuilderStage([projectName: "afs-tsys-estatements-ms", marathonEnv: 'AWS_DEV',  memory: '2048', isDnsHackEnabled: 'Y', description: 'AFS to TSYS E-Statements MicroService', isDockerFileProvided: 'TRUE']),

		// C
        new CiPipeLineBuilderStage([projectName: "compass-stockenquiry-ms", description: 'Compass Stock Enquiry Request']),
		
		// K
        new CiPipeLineBuilderStage([projectName: "kafka-jsrabbitmq-daventry-annexe-ms", marathonEnv: 'AWS_DEV',  memory: '2048', isDnsHackEnabled: 'Y', description: 'JS RabbitMQ <-> Kafka Link for Daventy Annexe', isDockerFileProvided: 'TRUE']),
		new CiPipeLineBuilderStage([projectName: "kafka-jms-adapter-ms", marathonEnv: 'PF_STAGING',  memory: '2048', description: 'Kafka-JMS Adapter', isDockerFileProvided: 'TRUE']),

		// L
	    new CiPipeLineBuilderStage([projectName: "location-ms", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', description: 'Location API', isDockerFileProvided: 'TRUE']),
	    new CiPipeLineBuilderStage([projectName: "location-ms-stub", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', description: 'PANLS and Google Places API Stub', isDockerFileProvided: 'TRUE']),
	    new CiPipeLineBuilderStage([projectName: "location-ms-wcs-adapter", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', description: 'WCS Adapter to Location API', isDockerFileProvided: 'TRUE']),

		// O
		new CiPipeLineBuilderStage([projectName: "omi-kafka-delivery-order-selection-list-ms", isDnsHackEnabled: 'Y', description: 'Omi Delivery Order Selection List', isDockerFileProvided: 'TRUE']),

		// P
        new CiPipeLineBuilderStage([projectName: "prm-kafka-productsupplier-warranties-ms",  description: 'prm-kafka-productsupplier-warranties-ms']),
        new CiPipeLineBuilderStage([projectName: "pim-kafka-product-data-publisher-ms",  description: 'PIM - Kafka Product data publisher', isDockerFileProvided: 'TRUE' , tasks: "clean install -DskipTests"]),

		// T
        new CiPipeLineBuilderStage([projectName: "triceps-kafka-daventry-annexe-ms", marathonEnv: 'PF_STAGING',  memory: '2048',  description: 'Kafka<->MQ (Triceps) Link for Daventry Annexe', isDockerFileProvided: 'TRUE'])

]