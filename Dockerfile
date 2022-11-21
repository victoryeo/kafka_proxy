FROM openjdk:11

# copy jar file
WORKDIR /app
COPY target/kafkaproxy.jar kafkaproxy.jar


# create non-root user
RUN addgroup --system --gid 1001 spring
RUN adduser --system --uid 1001 --group spring

# give permission to new user
RUN  chown -R spring:spring /app

# switch user
USER spring:spring

# expose port
EXPOSE 8081

# start app
ENTRYPOINT ["java","-jar","/app/kafkaproxy.jar"]