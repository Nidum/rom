# Room occupation manager

Room occupation manager is a Spring-Boot service which provides endpoints for managing room occupation.

## Prerequisits

To run current project, you need to have Java 11 or above installed.
Clone project from git repository. For this, open folder, where you wish to keep sources. Open any command line tool in project root folder and run:

```bash
git clone https://github.com/Nidum/rom
```

Also, ensure that your gradle version is above 6.3. Run this command to verify this:
```bash
./gradlew --version
```

To install newer version of gradle run:
```bash
./gradlew wrapper --gradle-version=6.5.1 --distribution-type=bin
```


## Running
Execute following command in any command line tool in the root folder of the project:
```bash
 ./gradlew bootJar bootRun
```

Now, as the app is up and running you can access it via Postman. 
Exmple request could be

<http://localhost:8080/api/room/occupancy?economy=1&premium=1>

Both economy and premium parameters are optional. They are substituted with a zero if missing.

Also, you can use swagger UI which is available [here](http://localhost:8080/swagger-ui.html). 
## Executing tests
To run all the unit and integration tests enter following command in any command line tool in the root folder of the project:
```bash
./gradlew clean test
```

## Additional configurations
App allows specifiing boundaries for room types. Open *price.properties* file to update existing values. App would not start, if some boundaries have intersections.