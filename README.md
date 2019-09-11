## Example front-end and back-end for the course Web and Cloud Computing

The following commands can be used to build the docker images and run the corresponding containers:

```
sudo docker build -t wacc-frontend .
sudo sbt docker:publishLocal
sudo docker run --name wacc-frontend -p 8000:8000 –-network=wacc-network –d wacc-frontend
sudo docker run --name wacc-backend –-network=wacc-network -p 8080:8080 –d wacc-backend:0.1
sudo docker run --name wacc-mongo –-network=wacc-network –d mongo
```

This command can be used to query the GraphQl endpoint: `curl -X POST -H "Content-Type: application/json" -d '{ "query": "{ messages { name } }" } ' localhost:8081/graphql`.
