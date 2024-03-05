## Order Service

Be sure docker is running before executing the following commands.

To create a bootable jar to be deployed to Lambda run:
`./gradlew awsJar`

To deploy via SAM
`sam deploy --stack-name order-service-test --capabilities CAPABILITY_IAM --resolve-s3`

To build run
`./gradlew build`

To build run
`sam build`

To deploy SAM for test:
`sam deploy --config-env test`

To deploy SAM for prod:
`sam deploy --config-env prod`