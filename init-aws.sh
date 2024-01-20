#!/bin/bash
echo 'Creating Default Table'
awslocal dynamodb create-table \
   --table-name order-table-local \
   --attribute-definitions AttributeName=email,AttributeType=S AttributeName=lastUpdatedDate,AttributeType=S \
   --key-schema AttributeName=email,KeyType=HASH AttributeName=lastUpdatedDate,KeyType=RANGE \
   --billing-mode PAY_PER_REQUEST

echo 'Loading Default Table Data'