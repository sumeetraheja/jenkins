import groovy.json.JsonSlurper

URL apiUrl = args[0].toURL()

def keepGoing = true
int count = 0

while (keepGoing) {
    def status
    try {
        Map convertedJSONMap = new JsonSlurper().parseText(apiUrl.text)
        status = convertedJSONMap."status"
    } catch (Exception ex) {
        status = "Not started"
    }
    println "Microservice Health Status : " + status

    if (status == "UP") {
        keepGoing = false
    } else if (count == 10) {
        keepGoing = false
        println("Retry count exceeded , count " + count)
        throw new Exception("Microservice is not healthy")
    } else {
        count++
        println("Will retry after 20sec , count " + count)
        Thread.currentThread().sleep(20000)

    }
}
