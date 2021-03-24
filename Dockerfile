FROM openjdk:8-jre-alpine

# Creating user
ENV USER dev
RUN adduser -D -g '' $USER

# Creating work directory and giving permissions
RUN mkdir /app \
    && mkdir /app/resources \
    && chown -R $USER /app \
    && chmod -R 755 /app

# Setting user to use when running the image
USER $USER

# Copying needed files
COPY /build/libs/*all.jar /app/tms.jar
COPY /build/resources/ /app/resources/
WORKDIR /app

# Entrypoint definition from https://ktor.io/docs/containers.html#docker
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "tms.jar"]