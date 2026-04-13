pipeline {
    agent any
    booleanParam('DEPLOY', true, 'Set to true to execute the Deploy stage (placeholder)')
    environment {
        // Jenkins server where this pipeline will run (informational)
        JENKINS_SERVER = 'http://172.18.158.193:8080/'
        // Maven flags: batch mode, do not fail interactive prompts
        MVN_COMMON = '-B'
    }

    tools {
        maven 'M3'
    }
    stages {
        stage('Echo Version') {
                steps {
                    sh 'echo Print Maven Version'
                    sh 'mvn -version'
                }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/abhishk0037/submit_assignment.git'
            }
        }

        stage('Build') {
            steps {
                script {
                        sh "mvn -f test/pom.xml ${env.MVN_COMMON} -DskipTests clean package"
                    }
                }
            }

        stage('Test') {
            steps {
                script {
                        sh "mvn -f test/pom.xml ${env.MVN_COMMON} test"
                }
            }
        }

        stage('ArchiveArtifacts') {
            steps {
                // Archive everything produced under test/target
                archiveArtifacts artifacts: 'test/target/**/*', fingerprint: true, allowEmptyArchive: true
            }
        }

        stage('PublishReports') {
            steps {
                // Publish JUnit/Surefire XML results and TestNG XML if present.
                // allowEmptyResults prevents the step from failing the build if no XMLs are found.
                junit testResults: 'test/target/surefire-reports/*.xml, test/test-output/testng-results.xml', allowEmptyResults: true

                script {
                    if (fileExists('test/test-output/index.html')) {
                        echo 'Found TestNG HTML report at test/test-output/index.html (optional: publish with HTML Publisher plugin)'
                    } else {
                        echo 'No TestNG HTML report foundd'
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                expression { return params.DEPLOY }
            }
            steps {
                script {
                    echo "DEPLOY parameter is true. (This stage is a placeholder — implement your deployment steps.)"
                    echo "Target Jenkins server: ${env.JENKINS_SERVER}"
                    if (isUnix()) {
                        sh 'echo "Deployment placeholder - implement actual deployment here"'
                    } else {
                        bat 'echo Deployment placeholder - implement actual deployment here'
                    }
                }
            }
        }
    }

    post {
        always {
            // Attempt to publish results again (won't fail pipeline if none)
            junit testResults: 'test/target/surefire-reports/*.xml, test/test-output/testng-results.xml', allowEmptyResults: true
            // Ensure artifacts are archived at least once
            archiveArtifacts artifacts: 'test/target/**/*', allowEmptyArchive: true
            // Clean workspace if Workspace Cleaner plugin is available
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the console output and test reports.'
        }
    }
}
