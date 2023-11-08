pipeline {
    agent any  // Use any available agent for the pipeline

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    // Use docker.inside to run Docker commands
                    docker.withRegistry('https://hub.docker.com/' , ) {
                        // Build the Docker image
                        docker.image('docker:latest').inside {
                            sh 'docker build -f src/main/docker/Dockerfile.jvm -t quarkus/customer-jvm .'
                        }
                    }
                }
            }
        }   
        // Add more stages for your pipeline
    }

    post {
        always {
            // Clean up Docker resources if needed
            sh 'docker rmi my-docker-image:latest'
        }
    }
}
