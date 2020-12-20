pipeline {
  agent {
    docker {
      image 'maven:3.3.3'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'mvn --version'
      }
    }
    stage('Clean') {
                steps {
                    dir('starter_code'){
                        sh 'mvn -B -DskipTests clean package'
                    }
                }
            }
    stage('Test') {
                steps {
                    dir('starter_code'){
                        sh 'mvn test'
                    }
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
  }
}