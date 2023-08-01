FROM sapmachine:20 as builder

WORKDIR application

ARG JAR_FILE=build/libs/sygotchi-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM sapmachine:20
WORKDIR application
RUN mkdir -p application/log

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
COPY /ssl/ ./

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "org.springframework.boot.loader.JarLauncher"]
