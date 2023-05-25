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
