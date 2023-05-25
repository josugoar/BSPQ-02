# Commands to test Docker

## Testing Docker
0. Create a Dockerfile
```yaml
# Specifies the base image
FROM maven:3.8.1-openjdk-11
# Copy the source code to the image
COPY . /learnify
# Execute the maven build
RUN cd learnify && mvn compile datanucleus:enhance
# change to this directory
WORKDIR /learnify
# Specify which command will be launched when starting the container
CMD ["mvn", "exec:java", "-Pgrizzly-server"]
```

1. Build an image: 
````
	docker build . -t learnify_server
````

2. Run a container based on the above generated image: 
````
	docker run -p 9080:8080 learnify_server
````

3. Access the server by going to a browser: http://localhost:9080/rest/resource

4. You can launch the container in detached mode: 
````
	docker run -d -p 9080:8080 --name server learnify_server
````

5. You can get a shell to that server by typing: 
````
	docker exec -it server /bin/bash
````

6. Print logs: 
````
	docker logs server
````

## Testing Docker-Compose

7. Create a MySQL container: 
````
	docker run --rm --name database -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.25
````

8. Create script to insert data in the DB: 
````
	docker exec -i database mysql -uroot -proot < sql/create-learnify.sql
````

9. Stop them since we need to connect these containers through a common network:
````
	docker stop server
	docker stop database
````

10. Create a new network: 
````
	docker network create learnify_default
````

11. Modify datanucles.properties to point to the name of the container, which acts as the host/node name:
```java
	javax.jdo.option.ConnectionURL=jdbc:mysql://database/learnifyDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
```

12. Modify the Main class of the Grizzly server to accept connections from anywhere:
```java
	public static final String BASE_URI = "http://0.0.0.0:8080/rest/";
```

13. Configure a user to be able to access from remote servers to the database:
```sql
	CREATE DATABASE learnifyDB;
	CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
	GRANT ALL ON learnifyDB.* TO 'spq'@'localhost';
	CREATE USER IF NOT EXISTS 'spq'@'%' IDENTIFIED BY 'spq';
	GRANT ALL ON learnifyDB.* TO 'spq'@'%';
```

14. Run the two containers connected to the same network:
````
docker run --rm --name database --network learnify_default -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.25
docker exec -i database mysql -uroot -proot < sql/create-learnify.sql

docker run --rm -d -p 9080:8080 --network learnify_default --name server learnify
docker exec server mvn datanucleus:schema-create
docker exec -i database mysql -uroot -proot < sql/create-data.sql
````

15. Run all together thanks to Docker-compose
```yaml
volumes:
    db_data:

services:
    database:
        image: mysql:8.0.25
        volumes:
            - db_data:/var/lib/mysql
        restart: always
        environment:
            MYSQL_ROOT_PASSWORD: root
            MYSQL_DATABASE: learnifyDB
            MYSQL_USER: spq
            MYSQL_PASSWORD: spq
    server:
        depends_on:
            - database
        build: .
        ports:
            - "9080:8080"
        restart: always
```

16. Run the docker-compose
```bash
	docker container stop database
	docker container stop server
	docker container ls
	docker compose build
	docker compose up -d
	docker compose exec server mvn datanucleus:schema-create
    docker compose exec database mysql -uroot -proot < sql/create-data.sql
	docker exec -it server /bin/bash
```

17. Test the service deployed with Docker-compose which accesses to a database in MySQL: http://0.0.0.0:9080/rest/resource
