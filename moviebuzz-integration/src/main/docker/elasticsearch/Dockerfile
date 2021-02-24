FROM elasticsearch:5.1.2-alpine
USER root
ADD elasticsearch.yml /usr/share/elasticsearch/config/

RUN chown elasticsearch:elasticsearch config/elasticsearch.yml
USER elasticsearch

EXPOSE 9200 9300