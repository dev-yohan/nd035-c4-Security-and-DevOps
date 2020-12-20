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
    stage ("Package"){
                 steps { sh 'mvn package -DskipTests'}
            }
    stage ("copy artifact"){
         steps { sh 'cp target/auth-course-0.0.1-SNAPSHOT.jar /tmp/ecom_app.jar && chmod 755 /tmp/ecom_app.jar'}
    }
    stage ("Deploy"){
         steps { sh 'cd /tmp  && java -jar ecom_app.jar &'}
    }
  }
}