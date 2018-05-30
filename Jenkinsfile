#!/usr/bin/env groovy
pipeline {
  agent none

  stages {


    stage('Build  Jenkins job item') {

      steps {

            // Checkout the branch.
            checkout scm
            jobDsl targets: ['jobs/*/*.groovy', 'views/*/*.groovy'].join('\n')
        }
      }
    }
}