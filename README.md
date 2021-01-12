## Process-Flow domain-process-engine

# create docker image
`docker build -t microtema/process-flow-domain-process-engine:1.0.0 .`

# run docker file
`docker run --name process-flow-domain-process-engine -p 8081:8080 microtema/process-flow-domain-process-engine:1.0.0`

# push docker file
`docker push microtema/process-flow-domain-process-engine:1.0.0`
