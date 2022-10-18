# springboot-microservices

The repository contains the design of microservices using spring boot and other frameworks

Keycloak is integrated and needs to be running for api gateway to allow requests.
Also run the docker command "docker run -d -p 9411:9411 openzipkin/zipkin" to run zipkin for distributed tracing.

kafka docker compose:
docker-compose up -d

to view logs:
docker logs -f broker

to start keycloak:
run standalone.bat -Djboss.http.port=8181