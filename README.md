## Example front-end and back-end for the course Web and Cloud Computing

The following commands can be used to build the docker images and run the corresponding containers:

```
docker build -t michelm/wacc-frontend .
sbt docker:publishLocal
docker run --name front-end -p 80:80 -d michelm/wacc-frontend
docker run --name back-end --network wacc-network -p 8080:8080 -d michelm/wacc-backend
docker run --name mongodb --network wacc-network -d mongo
```

This command can be used to query the GraphQl endpoint: `curl -X POST -H "Content-Type: application/json" -d '{ "query": "{ messages { name, content } }" } ' localhost:8081/graphql`.
