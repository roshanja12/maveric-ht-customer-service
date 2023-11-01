pipeline

{
    agent any
	
    stages
     {

        stage('Build')
        {
            steps
            {
                script {
                    
                //sh 'chmod +x ./mvnw'
                //sh './mvnw package -DskipTests '
                //sh 'docker build -f src/main/docker/Dockerfile.jvm -t quarkus/customer-jvm .'
            }
            }
        }

// stage('Push to ECR')
// 			{


//                     steps
//                      {

//                        withCredentials([[
//                                            $class: 'AmazonWebServicesCredentialsBinding',
//                                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
//                                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
//                                            credentialsId: 'aws-ecr-creds'
//                                        ]])
//                          {
//                             sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 516244522563.dkr.ecr.us-east-1.amazonaws.com'
//                             sh 'docker push 516244522563.dkr.ecr.us-east-1.amazonaws.com/payassure-sdk-alpha:latest'
//                         }

//                     }
// 			}


// stage('Deploy')
//         {
//             steps
//             {
                
//                sh 'kubectl config use-context arn:aws:eks:us-east-1:516244522563:cluster/PAYASSURE-ALPHA-EKS-CLUSTER'
//                 sh 'kubectl delete deployments payassure-sdk -n payassure-a'
//                 sh 'kubectl apply -f sdk-deploy.yaml -n payassure-a'
// 				//sh "kubectl set image deployment/payassure-dev-ui-deploy payassure-dev-ui=516244522563.dkr.ecr.us-east-1.amazonaws.com/payassure-ui:latest"
// 				sh 'kubectl get pods -n payassure-a'
//                 sh 'docker system prune -f'
				
//             }
//         }


}


}
