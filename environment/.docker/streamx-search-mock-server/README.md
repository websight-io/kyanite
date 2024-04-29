# Security
**WARNING** this is **NOT** using secure transfer - it's just `HTTP` without encryption.

**DO NOT** mock up sensitive data, always use dummy data.

# How it works?
- data - https://github.com/typicode/json-server/#getting-started
- routes - https://github.com/typicode/json-server/#add-custom-routes

# How it builds?
`docker-compose.yml` supports `build` parameter that is using `Dockerfile` to build the image.

```
    build:
      context: ../.docker/streamx-search-mock-server
```

# How to rebuild the container?
When there are some changes and need to rebuild the container, run:

`docker compose --build` 

The build flag forces rebuild of all `Dockerfile` files 
that `build` parameters points to.
