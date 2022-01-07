# Version 0.0.1
# Dockerfile for Smsone Smart Search Core Service

# Specify the base image, this is the early stage of building in stages
FROM openjdk:11-jre-slim as builder
MAINTAINER pader.zhang "pader.zhang@starlight-sms.com"
# Executive working directory
WORKDIR application
# Configuration parameter
ARG JAR_FILE=build/libs/*.jar
# Copy the jar file obtained by compiling and building to the mirror space
COPY ${JAR_FILE} application.jar
# Extract the split build result from application.jar by tool spring-boot-jarmode-layertools
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-jre-slim
MAINTAINER pader.zhang "pader.zhang@starlight-sms.com"
WORKDIR application
ENV TZ="Asia/Shanghai"

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get update \
&& apt-get -y install netcat-traditional \
&& update-alternatives --config nc \
&& apt-get clean all

# In the previous stage, multiple files were extracted from the jar. Here, the COPY command was executed to copy to the mirror space. Each COPY is a layer
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
COPY ./build/application.properties ./
# security patch - remove apt from container
EXPOSE 8080

ENTRYPOINT ["sh","-c","java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 $JAVA_OPTS -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher"]

