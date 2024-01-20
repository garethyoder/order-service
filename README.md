## Order Service

To create a bootable jar to be deployed to Lambda run:
`./gradlew awsJar`

To deploy via SAM
`sam deploy --stack-name order-service-test --capabilities CAPABILITY_IAM --resolve-s3`