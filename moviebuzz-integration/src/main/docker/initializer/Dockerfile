FROM openjdk:8-jre-alpine

RUN apk add --update curl && rm -rf /var/cache/apk/*

ADD initialize.sh /
ADD moviebuzz-api-develop.yml /

RUN chmod +x /initialize.sh

CMD ["sh", "initialize.sh"]