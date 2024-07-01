pipeline {
    environment {
        FULL_PATH_BRANCH = "${env.BRANCH_NAME}"
        RELEASE_BRANCH = FULL_PATH_BRANCH.substring(FULL_PATH_BRANCH.lastIndexOf('/') + 1, FULL_PATH_BRANCH.length()).trim()
        NSS_API_KEY = credentials('tocl-nss-api-key')
    }

    tools {
        maven 'maven3'
        jdk 'OpenJDK17'
    }

    agent any

    stages {

        stage('initialization') {
            steps {
                sh 'java -version'
                sh 'mvn --version'
                sh 'echo HOME=${HOME}'
                sh 'echo PATH=${PATH}'
                sh 'echo M2_HOME=${M2_HOME}'
                echo "build=${env.BUILD_NUMBER}"
                echo "FULL_PATH_BRANCH=${FULL_PATH_BRANCH}"
                echo "RELEASE_BRANCH=\"${RELEASE_BRANCH}\""
            }
        }

        stage('build and deploy QA artifacts') {
            when {
                branch 'qa'
            }
            steps {
                sh 'mvn -B clean compile package -Drevision=${RELEASE_BRANCH}-SNAPSHOT'
            }
        }

        stage('build RELEASE') {
            when {
                expression { BRANCH_NAME ==~ /release\/[0-9]+\.[0-9]+/ }
            }
            steps {
                sh 'mvn -B clean package deploy -Drevision=${RELEASE_BRANCH}.${BUILD_NUMBER}'
                // просмотр артефактов
                sh 'ls -la'
                sh 'ls -la target/'
            }
        }

    }

    post {
        always {
            nssSendJobResult(recipients: "AndewilEventsChannel")
        }
    }
}
