pipeline {
    agent {
        kubernetes {
            label 'docker-agent-1' // Label for the custom agent pod template
            customWorkspace '/var/jenkins/workspace' // Set a custom workspace location if needed

            defaultContainer 'jnlp'
            containers {
                customContainer('docker-agent') {
                    image 'docker:20.10.7' // Specify the Docker image version
                    args '-v /var/run/docker.sock:/var/run/docker.sock' // Mount the Docker socket for access

                    // You can add more environment variables, resource limits, etc. as needed
                }
            }
        }
    }
    
    stages {
        stage('Build Docker Image') {
            steps {
                container('docker-agent-1') {
                    // Inside this container, Docker is available
                    sh 'docker build -f src/main/docker/Dockerfile.jvm -t quarkus/customer-jvm .'
                }
            }
        }
        // Add more stages for your pipeline
    }

    post {
        always {
            sh "echo ls"
        }
    }
}
