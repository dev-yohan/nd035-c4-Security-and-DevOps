pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'mvn --version'
      }
    }
    stage('java version') {
        steps {
            sh 'javac -version'
          }
    }
    stage('Clean') {
                steps {
                    dir('starter_code'){
                        sh 'mvn clean compile'
                    }
                }
            }
    stage('Test') {
                steps {
                    dir('starter_code'){
                        sh 'mvn test'
                    }
                }
            }
     stage('Deliver') {
                steps {
                    sh './jenkins/scripts/deliver.sh'
                }
            }
  }
}