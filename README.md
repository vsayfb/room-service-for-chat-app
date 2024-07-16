# Room Service For Chat Application

This microservice is part of the _[real-time chat application](https://github.com/vsayfb/real-time-chat-application)._

## Description

This microservice manages active rooms and members in the application, using Redis to store them in a fast-response, in-memory database.

## Running the application

#### Development

`docker compose up -d && docker compose logs -f`

#### Testing

`BUILD_TARGET=test docker compose up -d && docker compose logs -f`

#### Production

`docker build -t room-ms . && kubectl apply -f deployment.yml`
