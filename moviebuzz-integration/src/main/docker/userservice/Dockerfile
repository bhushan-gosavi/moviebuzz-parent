FROM java:8

WORKDIR /opt

ADD moviebuzz-user-service.jar /opt/app.jar

ADD run.sh /opt

RUN chmod +x /opt/app.jar

RUN chmod +x /opt/run.sh

ENTRYPOINT "/opt/run.sh"

EXPOSE 8082