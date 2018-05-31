#!/usr/bin/env groovy
pipeline {
  agent any

  stages {


    stage('Build  Jenkins job item') {

      steps {

        // Checkout the branch.
        checkout scm
        jobDsl targets: ['jobs/*.groovy'].join('\n') , additionalClasspath: 'target/jenkins2-job-1.0.jar'
      }
    }
  }
}