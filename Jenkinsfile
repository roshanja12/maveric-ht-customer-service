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
spec:
  containers:
    - name: docker
      image: alpine:latest // Use a Docker image with the desired version
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
                    node('docker-agent') { // Use the label of your custom agent pod
                        sh 'docker build -f src/main/docker/Dockerfile.jvm -t quarkus/customer-jvm .'
                    }
                }
            }
        }
        // Add more stages for your pipeline
    }
    post {
        always {
            node('docker-agent') { // Use the label of your custom agent pod
                sh 'docker rmi my-docker-image:latest'
            }
        }
    }
}
