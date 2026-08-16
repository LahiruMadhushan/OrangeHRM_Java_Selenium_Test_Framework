pipeline {
    agent any

    stages {
        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }
    }
}