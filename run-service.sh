#!/bin/sh

runSpring(){
	command=$(grep -v '^$' .env | awk -F= '{printf "%s=%s ", $1, $2}')
	eval "$command ./gradlew bootRun &"
}

docker compose up -d

cd user

runSpring

cd ../order

runSpring

cd ../catalogue

runSpring
