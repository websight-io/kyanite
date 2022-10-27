# Local Environment

Build the distribution & Docker images running the following commands from this directory:

```bash
mvn -f ../../pom.xml clean install
```

```bash
docker compose up
```
> Note: hitting `ctrl+c` just stops the environment. It will not delete the content.

```bash
sh delete.sh
```

Services are available with URLs:

- [localhost:8080](http://localhost:8080/) - ICE admin panel (`wsadmin`/`wsadmin`)
- [localhost](http://localhost/) - the published pages
