FROM openjdk:8-jre-alpine

RUN apk add --update curl && rm -rf /var/cache/apk/*

ADD initialize.sh /
ADD moviebuzz-api-develop.yml /
ADD moviebuzz-processor-develop.yml /
ADD moviebuzz-user-service-develop.yml /
ADD es-movies-mapping.json /
ADD es-theaters-mapping.json /

RUN chmod +x /initialize.sh

CMD ["sh", "initialize.sh"]