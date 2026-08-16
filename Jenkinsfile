pipeline {
    agent any

    environment {
        PATH = "C:\\Program Files\\Maven\\apache-maven-3.9.16\\bin;${env.PATH}"
    }

    stages {
        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }
    }
}