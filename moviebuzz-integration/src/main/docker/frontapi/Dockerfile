FROM java:8

WORKDIR /opt

RUN mkdir /opt/appd

ADD AppServerAgent.zip /opt/appd/AppServerAgent.zip

RUN unzip /opt/appd/AppServerAgent.zip -d /opt/appd

ADD moviebuzz-front-api.jar /opt/app.jar

ADD run.sh /opt

RUN chmod +x /opt/app.jar

RUN chmod +x /opt/run.sh

ENTRYPOINT "/opt/run.sh"

EXPOSE 8084