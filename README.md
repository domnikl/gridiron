# gridiron

Gridiron is an American Football bet system that lets you bet on final scores of games with your friends.

## project structure

The project is built with a Kotlin + Ktor powered [backend](https://github.com/domnikl/gridiron/tree/master/backend) using MySQL as persistent storage. On top sits a Single Page Application built with VueJS residing in [frontend](https://github.com/domnikl/gridiron/tree/master/frontend).

## building

Gridiron uses a `docker-compose` config to build and run everything. Build and start it like this:

```
docker-compose up --build
```
