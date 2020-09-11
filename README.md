## Example front-end and back-end for the course Web and Cloud Computing

The following commands can be used to build the docker images and run the corresponding containers:

```
sudo docker build -t michelm/wacc-frontend .
sudo sbt docker:publishLocal
sudo docker run --name front-end -p 80:80 –-network wacc-network –d michelm/wacc-frontend
sudo docker run --name back-end –-network wacc-network -p 8080:8080 –d michelm/wacc-backend:0.1
sudo docker run --name mongodb –-network wacc-network –d mongo
```

This command can be used to query the GraphQl endpoint: `curl -X POST -H "Content-Type: application/json" -d '{ "query": "{ messages { name, content } }" } ' localhost:8081/graphql`.
