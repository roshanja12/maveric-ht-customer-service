pipeline {
    agent any  // Use any available agent for the pipeline

    environment {
        DOCKERFILE_PATH = 'src/main/docker/Dockerfile.jvm'  // Specify the path to your Dockerfile
        IMAGESTREAM_NAME = 'customer-service'  // Set the name of your ImageStream
    }

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    // Use docker.inside to run Docker commands
                    docker.image('docker:latest').inside {
                        // Build the Docker image
                        sh "docker build -f ${DOCKERFILE_PATH} -t ${IMAGESTREAM_NAME} ."
                    }
                }
            }
        }
        stage('Push to ImageStream') {
            steps {
                script {
                    // Use oc command to tag and push the Docker image to the ImageStream
                    sh "oc tag ${IMAGESTREAM_NAME}:latest ${IMAGESTREAM_NAME}:latest"
                    sh "oc import-image ${IMAGESTREAM_NAME}:latest --from=${IMAGESTREAM_NAME}:latest --confirm"
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker resources if needed
            sh 'docker rmi ${IMAGESTREAM_NAME}:latest'
        }
    }
}
