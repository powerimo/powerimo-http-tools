pipeline {
    environment {
        FULL_PATH_BRANCH = "${env.BRANCH_NAME}"
        RELEASE_BRANCH = FULL_PATH_BRANCH.substring(FULL_PATH_BRANCH.lastIndexOf('/') + 1, FULL_PATH_BRANCH.length()).trim()
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
                sh 'mvn -B clean compile versions:set -DnewVersion=1.0-SNAPSHOT'
                sh 'mvn deploy'
            }
        }

        stage('build RELEASE') {
            when {
                expression { BRANCH_NAME ==~ /release\/[0-9]+\.[0-9]+/ }
            }
            steps {
                sh 'mvn -B clean compile versions:set -DnewVersion=${RELEASE_BRANCH}.${BUILD_NUMBER}'
                sh 'mvn -B package'
                // просмотр артефактов
                sh 'ls -la'
                sh 'ls -la target/'
                nexusPublisher nexusInstanceId: 'nexus3',
                                    nexusRepositoryId: 'maven-releases',
                                    packages: [
                                        [$class: 'MavenPackage', mavenAssetList:
                                            [
                                                [extension: 'jar', filePath: "target/powerimo-http-tools-${RELEASE_BRANCH}.${BUILD_NUMBER}.jar"]
                                            ],
                                            mavenCoordinate: [artifactId: 'powerimo-http-tools', groupId: 'org.powerimo', packaging: 'jar', version: "${RELEASE_BRANCH}.${BUILD_NUMBER}"]
                                        ]
                                    ]
            }
        }

    }

    post {
         success {
             sh '/var/jenkins_home/deployscripts/send-message-success.sh'
         }
         failure {
             sh '/var/jenkins_home/deployscripts/send-message-fail.sh'
         }
    }
}
