pipeline {
    agent {
        kubernetes {
            label 'docker-agent' // Label for the custom agent pod template
            defaultContainer 'jnlp' // JNLP container for Jenkins communication
            yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: docker-agent
    namespace: maveric-ht-dev
spec:
  containers:
    - name: docker
      image: docker:19.03.13 // Use a Docker image with the desired version
      command:
        - cat
      resources:
        limits:
          memory: 512Mi
          cpu: 500m
  """
        }
    }
    stages {
        stage('Build Docker Image') {
            steps {
                script {

                    sh 'echo in stage'
                    sh 'docker build -f src/main/docker/Dockerfile.jvm -t quarkus/customer-jvm .'
                }
            }
        }
        // Add more stages for your pipeline
    }
    post {
        always {
            // Clean up Docker resources if needed
            sh 'echo test'
        }
    }
}
