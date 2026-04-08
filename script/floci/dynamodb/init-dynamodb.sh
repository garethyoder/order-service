#!/bin/bash
set -euo pipefail

AWS_ENDPOINT_URL="${AWS_ENDPOINT_URL:-http://localhost:4566}"
AWS_REGION="${AWS_DEFAULT_REGION:-us-east-1}"

echo 'Creating Default Table'
aws dynamodb create-table \
   --endpoint-url "$AWS_ENDPOINT_URL" \
   --region "$AWS_REGION" \
   --table-name OrderTable-local \
   --attribute-definitions AttributeName=email,AttributeType=S AttributeName=lastUpdatedDate,AttributeType=S \
   --key-schema AttributeName=email,KeyType=HASH AttributeName=lastUpdatedDate,KeyType=RANGE \
   --billing-mode PAY_PER_REQUEST

echo 'Finished DynamoDB configuration'

