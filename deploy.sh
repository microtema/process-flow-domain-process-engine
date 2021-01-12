#!/bin/sh

mvn test
mvn package

docker build -t microtema/process-flow-domain-process-engine:1.0.0 .

docker push microtema/process-flow-domain-process-engine:1.0.0

kubectl apply -f deployment.yaml
