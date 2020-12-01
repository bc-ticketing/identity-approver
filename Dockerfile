# Build Stage for Spring boot application image
FROM openjdk:8-jdk-alpine as build
ENV LC_ALL=C
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw
# download the dependency if needed or if the pom file is changed
RUN apk add --no-cache tesseract-ocr

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production Stage for Spring boot application image
FROM openjdk:8-jre-alpine as production
ARG DEPENDENCY=/app/target/dependency
ENV LC_ALL=C

RUN apk update

RUN apk add --no-cache tesseract-ocr -y

# Download last language package
RUN mkdir -p /usr/share/tessdata
ADD https://github.com/tesseract-ocr/tessdata_fast/raw/master/eng.traineddata /usr/share/tessdata/eng.traineddata

RUN tesseract --list-langs
RUN tesseract -v


# Copy the dependency application file from build stage artifact
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app


# Run the Spring boot application
ENTRYPOINT [ "java", "-cp", "app:app/lib/*","com.idetix.identityapprover.IdentityApproverApplication"]

