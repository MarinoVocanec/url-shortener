# Url shortener

### Requirements:
1. **Java** version >= 11
2. Apache **Maven**

### Compile and run application:
1. Download project.
2. Open terminal.
3. Navigate to project directory.
4. For fresh compile and run, execute following commands:
    - `mvn package`
    - `cd target`
    - `java -jar urlshortener-0.0.1-SNAPSHOT.jar`   
    If on **Windows** make sure **PATH** variable is configured for **Java** and **Maven**!
5. In browser open frontend page: http://localhost:8080 or http://127.0.0.1:8080.

**NOTE**: API call tests and unit tests can be found in *standard* directory: `src/test/java`.
However, tests are NOT run when executing `mvn package`.
