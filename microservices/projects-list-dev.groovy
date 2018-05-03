import uk.co.argos.middleware.*

ciJobs = [
        //A 
	    new CiPipeLineBuilderDev([projectName: "address-ms", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', runSoapUiTests: 'true', description: 'Address API', isDockerFileProvided: 'TRUE']),
        new CiPipeLineBuilderDev([projectName: "aura-nectar-points-redemption-ms",  marathonEnv: 'PF_STAGING',  memory: '2048',  runSoapUiTests: 'true', description: 'Nectarpoints request and response transactions']),
        new CiPipeLineBuilderDev([projectName: "aura-compass-address-service-ms",  marathonEnv: 'PF_STAGING',  memory: '2048',  runSoapUiTests: 'true', description: 'CMO6 Aura to Comapss delivery update request and response']),
        new CiPipeLineBuilderDev([projectName: "aura-compass-deliverydate-update-ms",  marathonEnv: 'PF_STAGING',  memory: '2048',  runSoapUiTests: 'true', description: 'CMO6 Aura to Comapss delivery update request and response']),
        new CiPipeLineBuilderDev([projectName: "aura-compass-order-update-ms",  marathonEnv: 'PF_STAGING',  memory: '2048',  runSoapUiTests: 'true', description: 'CM07 Compass Order Update Request/Response']),
        new CiPipeLineBuilderDev([projectName: "aura-compass-product-item-update-ms", description: 'Compass Product Item Update Request/Response']),
		new CiPipeLineBuilderDev([projectName: "afs-tsys-estatements-ms", marathonEnv: 'AWS_DEV',  memory: '2048', isDnsHackEnabled: 'Y', description: 'AFS to TSYS E-Statements MicroService', isDockerFileProvided: 'TRUE']),
		new CiPipeLineBuilderDev([projectName: "afs-tsys-estatements-stub-ms", marathonEnv: 'AWS_DEV',  memory: '2048',  description: 'AFS to TSYS E-Statements stub mocking TSYS']),
		new CiPipeLineBuilderDev([projectName: "afs-smtp-email-service-ms", marathonEnv: 'AWS_DEV',  memory: '2048',  description: 'AFS to SMTP email service']),

        //C
        new CiPipeLineBuilderDev([projectName: "compass-stockenquiry-ms", description: 'Compass Stock Enquiry Request', isDnsHackEnabled: 'Y']),
		new CiPipeLineBuilderDev([projectName: "camel-kafka-consume", marathonEnv: 'PF_STAGING', description: 'Sample Camel App to Consume from Kafka', isDockerFileProvided: 'TRUE']),


        //E
        new CiPipeLineBuilderDev([projectName: "ebay-vendor-notification-ms",  description: 'Ebay notifications Request']),

        //G 
        new CiPipeLineBuilderDev([projectName: "gambit-omi-transactions-ms", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'N', runSoapUiTests: 'true', description: 'Gambit to OMI transactions']),
          
		// K
        new CiPipeLineBuilderDev([projectName: "kafka-consumer-monitor", marathonEnv: 'PF_STAGING',  memory: '2048',  description: 'To monitor Kafka Topic Lag, and restart Camel Routes if required', isDockerFileProvided: 'TRUE']),

        new CiPipeLineBuilderDev([projectName: "kafka-ssl-producer-consumer-test", marathonEnv: 'AWS_DEV',  description: 'To test kafka ssl producer and consumer connection']),

        new CiPipeLineBuilderDev([projectName: "kafka-topic-watch-ui", marathonEnv: 'AWS_DEV',  description: 'To search and view kafka messages']),
		
        new CiPipeLineBuilderDev([projectName: "kafka-jsrabbitmq-daventry-annexe-ms", marathonEnv: 'AWS_DEV',  memory: '2048', isDnsHackEnabled: 'Y', description: 'JS RabbitMQ <-> Kafka Link for Daventy Annexe', isDockerFileProvided: 'TRUE']),

		new CiPipeLineBuilderDev([projectName: "kafka-jms-adapter-ms", marathonEnv: 'PF_STAGING',  memory: '2048', description: 'Kafka-JMS Adapter', isDockerFileProvided: 'TRUE']),
		new CiPipeLineBuilderDev([projectName: "kafka-omi-warehousedproduct-ack-ms", marathonEnv: 'PF_STAGING',  memory: '2048', description: 'kafka-omi-warehousedproduct-ack-ms', isDockerFileProvided: 'TRUE']),

		// L
	    new CiPipeLineBuilderDev([projectName: "location-ms", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', runSoapUiTests: 'true', description: 'Location API', isDockerFileProvided: 'TRUE']),
	    new CiPipeLineBuilderDev([projectName: "location-ms-stub", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', description: 'PANLS and Google Places API Stub', isDockerFileProvided: 'TRUE']),
	    new CiPipeLineBuilderDev([projectName: "location-ms-wcs-adapter", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', runSoapUiTests: 'true', description: 'WCS Adapter to Location API', isDockerFileProvided: 'TRUE']),

		//o
		new CiPipeLineBuilderDev([projectName: "omi-kafka-delivery-order-selection-list-ms", isDnsHackEnabled: 'Y', description: 'Omi Delivery Order Selection List', isDockerFileProvided: 'TRUE']),
		new CiPipeLineBuilderDev([projectName: "omi-kafka-warehousedproduct-ms", isDnsHackEnabled: 'Y', description: 'OMITriceps message to Warehoused Product', isDockerFileProvided: 'TRUE']),
		new CiPipeLineBuilderDev([projectName: "omi-kafka-advanced-shipping-notification-ms", isDnsHackEnabled: 'Y', description: 'OMI Advanced Shipping Notification', isDockerFileProvided: 'TRUE']),
        new CiPipeLineBuilderDev([projectName: "omi-kafka-tsu-announcement-ms", isDnsHackEnabled: 'Y', description: 'OMITriceps message to TSU Announcement message', isDockerFileProvided: 'TRUE']),
        new CiPipeLineBuilderDev([projectName: "omi-kafka-tsu-transport-instruction-ack-ms", isDnsHackEnabled: 'Y', description: 'OMITriceps ack to TSU TransportInstruction', isDockerFileProvided: 'TRUE']),
        new CiPipeLineBuilderDev([projectName: "omi-kafka-gambit-transaction-ack-ms", isDnsHackEnabled: 'Y', description: 'OMITriceps message to Gambit Transaction Ack message', isDockerFileProvided: 'TRUE']),

        //P
        new CiPipeLineBuilderDev([projectName: "prm-kafka-productsupplier-warranties-ms", description: 'Product warranties data']),

        new CiPipeLineBuilderDev([projectName: "pim-kafka-product-primary-attributes-ms", description: 'Product Primary Attributes']),

        new CiPipeLineBuilderDev([projectName: "pim-kafka-product-data-publisher-ms", isDnsHackEnabled: 'Y', description: 'PIM Product Data Publisher', isDockerFileProvided: 'TRUE']),

        //S
        new CiPipeLineBuilderDev([projectName: "soapui-engine", marathonEnv: 'PF_STAGING',  memory: '2048', isDnsHackEnabled: 'Y', description: 'Soapui engine', isDockerFileProvided: 'TRUE']),

        //t

        new CiPipeLineBuilderDev([projectName: "triceps-kafka-daventry-annexe-ms", marathonEnv: 'PF_STAGING',  memory: '2048',  description: 'Kafka<->MQ (Triceps) Link for Daventry Annexe', isDockerFileProvided: 'TRUE'])


]