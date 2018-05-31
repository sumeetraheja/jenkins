#!/usr/bin/env groovy
pipeline {
  agent none

  stages {


    stage('Build  Jenkins job item') {

      steps {
        script {
          if (( env.BRANCH_NAME == 'master' ) || ( env.BRANCH_NAME == 'devops-private')) {
            // Checkout the branch.
            checkout scm
            jobDsl targets: ['jobs/*/*.groovy', 'views/*/*.groovy'].join('\n')
          }
          else {
            println "Not on master or devops-private branch doing nothing."
          }
        }
      }
    }
  }
}