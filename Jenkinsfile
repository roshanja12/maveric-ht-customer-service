pipeline {
    agent any  // Use any available agent for the pipeline

    stages {
        stage('Build Container Image') {
            steps {
                script {
                    // Build the container image using buildah
                    sh 'buildah bud -t my-container-image -f src/main/docker/Dockerfile.jvm .'
                }
            }
        }

        // Add more stages for your pipeline
    }

    post {
        always {
            // Clean up Container image if needed (remove it)
            sh 'podman rmi my-container-image'
        }
    }
}
