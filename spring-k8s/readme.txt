repository.findAll()
  - returning the entities directly to the consumer as a response because any change
    in database schema will affect the response
  - it is better to load data into DTO (since we are only reading) than loading an Entity
    - Used DTO Projection using Flyway Migration

H2 Link: http://localhost:8080/h2-console/ 
  jdbc-url: [Check from logs]
  username: sa
  password: [blank]

SpringBoot supports 2 Proper Database Migration Tool
  1. Flyway
  2. Liquibase

Flyway migration follow a naming pattern:
  Ex: V1__create_bookmarks_table.sql

After creating the migration script, check in H2 flyway_schema_history

you can query: select * from "flyway_schema_history";

installed_rank  	version  	description  	                              type    script  	                        checksum  	  installed_by    installed_on                execution_time  success  
-1	              null	    << Flyway Schema History table created >>	  TABLE	                                    null	        SA	            2023-04-03 22:52:58.904804	0	              TRUE
1	                1	        create bookmarks table	                    SQL	    V1__create_bookmarks_table.sql	  509433377	    SA	            2023-04-03 22:52:58.924826	7	              TRUE


NOTE: if you change the V1__create_bookmarks_table.sql, it will produce a different checksum, and the compilation fails.
Checksums validate that the script is the same.
RULE: Dont touch the script once it is executed.

Check FlywayAutoConfiguration class
  from org.springframework.boot.autoconfigure.flyway
  then
Check DatabaseDriver class
  from org.springframework.boot.jdbc.DatabaseDriver;

{vendor} -> productName.toLowerCase();
H2 -> h2

-----------
5. Using Spring Data Jpa DTO Projections

in the List View, we dont need to expose all the properties and columns from database.
  - We should load only what we needed

SEE docs for sprind data jpa projections
2 Ways to create Projections:
  1. Class-based projection using DTO
  2. Interface-based

-----------
6. API Integration Testing using Testcontainers

Use @SpringBootTest 
  - allows to write Integration Test
  - allows to startup the entire Spring context, including all beans and configurations,
    and run tests against the running application
  - You can use Spring features like Dependency Injection and auto-configuration in your test logic

Different webEnvironments for @SpringBootTest
1. MOCK
  - default web environment
  - It starts an embedded web server but does NOT bind to a network interface.
    Instead, requests are intercepted and handled by a MockMvc instance, 
    which provides a fluent API for testing web endpoints. 
  - it is useful for testing the controller layer of your application, 
    without the need for a full web server.
  - It's also faster than the other environments, 
    as it doesn't require starting a full server.

2. RANDOM_PORT
  - This environment starts an embedded web server 
    and binds it to a random port on the network interface
  - It is useful for testing the integration between your application and external systems, 
    such as a database or another web service
  - It's also useful for testing the behavior of your application under load, 
    as you can run multiple instances of the test in PARALLEL.
  - It's useful when your running application in build pipelines like Jenkins,
    it will pick a AVAILABLE random port so that it will not have a conflict even the build server is running multiple builds parallel.

3. DEFINED_PORT
  - This environment is similar to RANDOM_PORT, 
    but it binds the server to a predefined port instead of a random one.
  - This can be useful if you need to test against a specific port that is already reserved for your application.
  - This environment is useful for testing the deployment 
    and configuration of your application in a production-like environment.

4. NONE
  - This environment starts the Spring context without any web server
  - It's useful for testing the non-web components of your application, 
    such as services and repositories
  - This environment is also faster than the other environments, 
    as it doesn't require starting a web server

@AutoConfigureMockMvc
  - an be used in integration tests to automatically configure and inject a MockMvc instance into the test class.

MockMvc
  - Spring Test framework that provides a way to test web endpoints without actually starting an HTTP server
  - Instead, it allows you to simulate HTTP requests and responses by calling your controllers directly, 
    which makes your tests faster and more focused.
  
Using testcontainers:
  for configuration of jdbc url, see: https://www.testcontainers.org/modules/databases/jdbc/
  


-----------
7. Dockerizing SpringBoot application

  Different ways to create docker image for springboot app
    1. Dockerfiles using fat-jar

    2. Multistage Dockerfile with layers

    3. Springboot Maven/Gradle plugin using Buildpacks
      Use the maven wrapper provided by springboot
      >> ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=<dockerhub_username>/<app_name>
      >> ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=ianagasen/spring-api

      OR add this to the pom.xml under build.plugins.plugin
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
        <image>
          <name>ianagasen/spring-api</name>
        <image>
      </configuration>

      and run this command removing the command line argument -D
      >> ./mvnw spring-boot:build-image

      to run:
      >> docker run -p 8080:8080 ianagasen/spring-api
      where -p is to publich a container's port to host's port

      to check: http://localhost:8081/api/bookmarks?page=2

    4. Jib Maven/Gradle Plugin(from google)
      from: https://github.com/GoogleContainerTools/jib
        - add the plugin specified
      
      once the plugin is added to pom.xml
      run:
      >> ./mvnw jib:dockerBuild
        - build the image locally and DONT want to push
        - but this requires local docker

      NOTE: ./mvnw jib:build
        - will create a docker image and will auto push through to docker repository
        - it will not use docker Daemon

  From ChatGPT: How to dockerize a spring boot app
    1. Create a Docker file. Create a file named 'Dockerfile' in the root directory of the spring boot app
      - A dockerfile is a script that contains instructions for build a Docker image.

    2. Specify a base image.
      - The first line in the Dockerfile should specify a base image to use as the starting point
        for building your Docker image
      - Example: You can use the official OpenJDK image
        >> FROM openjdk:11

    3. Setup the working directory. 
      - Use the `WORKDIR` instruction to set the working directory in the Docker image
      Ex >> WORKDIR /app
    4. Copy the application files:
      - Use the `COPY` instruction to copy the application files into the Docker image
      Ex >> COPY target/myapp.jar /app
    
      NOTE: This assumes that you have already built your Spring Boot app as a jar file

    5. Expose the port:
      - Use the `EXPOSE` instruction to expose the port that your spring boot app listens on
      Ex >> EXPOSE 8080

    6. Specify the command to run:
      - Use the `CMD` instruction to specify the command to run when a container is started
        based on the Docker image.
      - For a SpringBoot App the command is usually `java - jar` command to run the jar file
      Ex >> ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]

    7. Build the Docker image: 
      - Use the `docker build` command to build the Docker image. (in CLI)
      Ex >> docker build -t myapp-image .

      NOTE: This assumes that you are in the directory that contains the Dockerfile

    8. Run the Docker container:
    - Use the `docker run` command to run a container based on the Docker image.
    Ex >> docker run -p 8080:8080 myapp-image
      - this maps the container's port 8080 to the host's port 8080 so that you can access the app 
        in a web browser `http://localhost:8080`

    
    Examples of Dockerfile
    1. >>
      FROM openjdk:11-jre-slim
      COPY target/myapp.jar /app.jar
      ENTRYPOINT ["java","-jar","/app.jar"]

    2. >> 
      FROM openjdk:11
      WORKDIR /app
      COPY target/myapp.jar /app
      EXPOSE 8080
      CMD ["java", "-jar", "/app/myapp.jar"]

    See CMD vs ENTRYPOINT in cgpt




-----------
8. Continuous Integration using GitHub Actions

  To configure secrets:
  1. Go to github repo
  2. Click the Settings Tab
  3. Under General, Go to Secrets


  command to run when faced with issue:
    ./mvnw: Permission denied
  git update-index --chmod=+x your_script.sh
  >>  git update-index --chmod=+x  .github/workflows/build.yaml