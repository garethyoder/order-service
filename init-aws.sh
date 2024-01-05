#!/bin/bash
echo 'Creating Default Table'
awslocal dynamodb create-table \
   --table-name table-global \
   --attribute-definitions AttributeName=id,AttributeType=S \
   --key-schema AttributeName=id,KeyType=HASH \
   --billing-mode PAY_PER_REQUEST

echo 'Loading Default Table Data'