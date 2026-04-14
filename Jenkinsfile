pipeline {
    agent any

    parameters {
        booleanParam(name: 'DEPLOY', defaultValue: true, description: 'Set to true to execute the Deploy stage (placeholder)')
    }

    tools {
        maven 'maven'
    }

    stages {
        stage('Echo Version') {
            steps {
                sh 'echo Print Maven Version'
                sh 'mvn -version'
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/abhishk0037/submit_assignment.git'
            }
        }

        stage('Build') {
            steps {
                sh "mvn -f test/pom.xml test -Dsurefire.suiteXmlFiles=testng.xml"
            }
        }

        stage('Test') {
            steps {
                sh "mvn -f test/pom.xml test"
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
                junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true

                script {
                    if (fileExists('test/test-output/index.html')) {
                        echo 'Found TestNG HTML report at test/test-output/index.html (optional: publish with HTML Publisher plugin)'
                    } else {
                        echo 'No TestNG HTML report found'
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
                    echo "DEPLOY parameter is true. (This stage is a placeholder — implement your deployment logic here.)"
                    sh 'echo "Deployment placeholder - implement actual deployment here"'
                }
            }
        }
    }

    post {
        always {
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the console output and test reports.'
        }
    }
}
