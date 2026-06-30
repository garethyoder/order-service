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

## Native build on Windows (Amazon Linux target)

Build the native executable in Docker so the binary is Linux-compatible.

Run from repo root in PowerShell:

```powershell
docker run --rm --entrypoint /bin/sh -v "${PWD}:/workspace" -w /workspace ghcr.io/graalvm/native-image-community:25 -c "sed -i 's/\r$//' ./gradlew; chmod +x ./gradlew; ./gradlew nativeCompile"
```

Native binary output:

`build/native/nativeCompile/order-service`

If `/bin/sh` is unavailable in the image:

```powershell
docker run --rm --entrypoint /bin/bash -v "${PWD}:/workspace" -w /workspace ghcr.io/graalvm/native-image-community:25 -lc "sed -i 's/\r$//' ./gradlew; chmod +x ./gradlew; ./gradlew nativeCompile"
```

## Run native binary locally on Windows

Because the native executable is Linux, run it in Docker on Windows.

1) Start local DynamoDB services:

```powershell
docker compose up -d
```

2) Run native app in a container:

```powershell
docker run --rm -p 8080:8080 --entrypoint /bin/sh -v "${PWD}:/workspace" -w /workspace/build/native/nativeCompile -e SPRING_PROFILES_ACTIVE=local -e RECAPTCHA_SECRET=test-secret -e DYNAMODB_ENDPOINT=http://host.docker.internal:4566/ -e AMAZON_AWS_ACCESS_KEY=1 -e AMAZON_AWS_SECRET_KEY=2 -e DYNAMODB_TABLENAME=OrderTable-local ghcr.io/graalvm/native-image-community:25 -c "./order-service"
```

3) Verify app is running:

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```

4) Stop services when done:

```powershell
docker compose down
```

## Native build troubleshooting

- `Task 'nativeCompile' not found`: ensure `org.graalvm.buildtools.native` plugin is present in `build.gradle.kts`.
- `./gradlew: cannot execute: required file not found`: wrapper has CRLF line endings; run the Docker command above that includes `sed -i 's/\r$//' ./gradlew`.
- `Image name ('-lc') start with a dash`: use `--entrypoint /bin/sh` (or `--entrypoint /bin/bash`) so shell flags are not interpreted as image args.
