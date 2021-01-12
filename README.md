## Process-Flow Platform Backend

# create docker image
`docker build -t microtema/process-flow-platform-backend:1.0.0 .`

# run docker file
`docker run --name process-flow-platform-backend -p 8081:8080 microtema/process-flow-platform-backend:1.0.0`

# push docker file
`docker push microtema/process-flow-platform-backend:1.0.0`
