#!/usr/bin/env groovy
pipeline {
  agent none

  stages {
  // Validation has been disable see DEVENV-3691
  //   stage('Jenkins jobs definition validation') {
  //     agent {
  //       label 'ec2'
  //     }
  //     steps{
  //       // Checkout the branch.
  //       checkout scm
  //       sh 'make validate'
  //     }
  //   }

    stage('Build  Jenkins job item') {
      agent {
        label 'ec2'
      }
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