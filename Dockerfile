FROM gradle:8-jdk AS build
COPY . /app
WORKDIR /app
RUN ["gradle", "war"]

FROM tomcat:8.5 AS app
COPY --from=build /app/build/libs/learning_new.war /usr/local/tomcat/webapps/ROOT.war