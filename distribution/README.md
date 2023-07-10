# Distribution
It builds a distribution containing all dependencies, configurations and running scripts. 

The release artifacts are:
- `slingosgifeature` file (descriptor for all projects that extend our distribution)
- CMS Docker image.

- Java 17 & Maven
- Docker Desktop


Run the command

```bash
../mvnw clean install
```

to assemble the distribution, build a Docker image and run integration tests.

```bash
../mvnw clean install
docker run -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=mongoadmin -e MONGO_INITDB_ROOT_PASSWORD=mongoadmin mongo:4.4.6
java -jar target/dependency/org.apache.sling.feature.launcher.jar -f target/slingfeature-tmp/feature-websight-cms-kyanite-project.json
```

and open [localhost:8080](http://localhost:8080/) to see the ICE admin panel (use default `wsadmin/wsadmin` password).

Press `CTRL + C` to stop the application.
