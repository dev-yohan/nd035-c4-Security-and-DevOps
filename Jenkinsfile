pipeline {
  agent {
    docker {
      image 'maven:3.3.3'
    }

  }
  stages {
    stage('build') {
      agent {
        docker {
          image 'maven:3.3.3'
        }

      }
      steps {
        sh 'mvn --version'
      }
    }

  }
}