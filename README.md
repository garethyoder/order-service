## Order Service

Be sure docker is running before executing the following commands.

## Local DynamoDB (floci)

This project now uses floci for local DynamoDB emulation (no LocalStack dependency).

Start local services:
```bash
docker compose up -d
```

Stop local services:
```bash
docker compose down
```

Run the full test suite:
```bash
./gradlew clean test
```

Run the full test suite (Windows PowerShell):
```powershell
.\gradlew.bat clean test
```

Run a focused form test suite:
```bash
./gradlew test --tests "com.cedarmeadowmeats.orderservice.OrderSubmissionFormTest"
```

Run a focused form test suite (Windows PowerShell):
```powershell
.\gradlew.bat test --tests "com.cedarmeadowmeats.orderservice.OrderSubmissionFormTest"
```

To build the Lambda deployment artifact zip run:
`./gradlew packageJar`

To deploy via SAM
`sam deploy --stack-name order-service-test --capabilities CAPABILITY_IAM --resolve-s3`

To build run
`./gradlew build`

To build with SAM run
`sam build`

To deploy SAM for test:
`sam deploy --config-env test`

To deploy SAM for prod:
`sam deploy --config-env prod`

To delete a stack run:
`sam delete --stack-name order-service-test`

To update the gradle version:
Change the version in the following file: gradle/wrapper/gradle-wrapper.properties