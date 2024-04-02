#!/bin/bash
echo 'Creating Default Table'
awslocal dynamodb create-table \
   --table-name OrderTable-local \
   --attribute-definitions AttributeName=email,AttributeType=S AttributeName=lastUpdatedDate,AttributeType=S \
   --key-schema AttributeName=email,KeyType=HASH AttributeName=lastUpdatedDate,KeyType=RANGE \
   --billing-mode PAY_PER_REQUEST

echo 'Loading Default Table Data'

echo 'SES Setup'
awslocal ses verify-email-identity email-address client@test.com --endpoint-url=http://localhost:4566
awslocal ses verify-email-identity email-address sender@test.com --endpoint-url=http://localhost:4566

echo 'Finished configurations'
