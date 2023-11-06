pipeline {
    
    stages {
        stage('Build Docker Image') {
            agent {
                docker {
                    image 'alpine:latest'
                    // Run the container on the node specified at the
                    // top-level of the Pipeline, in the same workspace,
                    // rather than on a new node entirely:
                    reuseNode true
                }
            }


            steps {
                script {
                  
                    sh 'docker build -f src/main/docker/Dockerfile.jvm -t quarkus/customer-jvm .'
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
