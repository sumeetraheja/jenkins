import uk.co.argos.middleware.*

ciJobs = [
        new CiCommonBuilder([projectName: "common-exception-handler", description: 'Common Code to handle Exceptions']),
        new CiCommonBuilder([projectName: "common-ibm-fat-mqjars", description: 'Combines all mq jars into single jar']),
        new CiCommonBuilder([projectName: "common-test-utils", description: 'Common Code to handle test classes']),
        new CiCommonBuilder([projectName: "middleware-camel-common", description: "Common Library to provide useful util functions for Middleware Camel Microservices"]),
        new CiCommonBuilder([projectName: "middleware-kafka-principal", description: "Customized Principal Builder used in Kafka ACL's"]),
		new CiCommonBuilder([projectName: "camel-admin-lib", description: "Common Library to provide basic admin GUI for a Camel App"]),
		new CiCommonBuilder([projectName: "common-ms-metrics", description: "Common Library which will report metrics of Microservices"])
]