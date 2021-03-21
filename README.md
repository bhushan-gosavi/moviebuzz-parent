# Moviebuzz
Moviebuzz is a highly scalable Movie Reviews and  Movie Ticket Booking platform created for learning purpose. 
Blog Link: https://bhushan-gosavi.medium.com/moviebuzz-system-design-coding-end-to-end-system-from-scratch-606dbd66e568

# Platform Features

 - Horizontally scalable
 - Highly Concurrent
 - Microservice Architecture
 - Containerized Application
 - Production Grade Code with Integration Tests using Docker Containers
 
 # Technologies
 
 - Cassandra
 - ElasticSearch
 - Docker
 - Apache Kafka
 - Spring Cloud
 - Hashicorp Consul
 - Zookeeper

#  Build Steps

 - map mkafka and mzookeeper hostnames to localhost in local machine
	 127.0.0.1 mkafka
	127.0.0.1 mzookeeper
	
 - Run maven build on moviebuzz-parent module  to generate JAR files
	 `mvn clean install`
	 
 - Run maven build using 'integrate' profile to launch docker containers and run Integration Tests
 - 
	 `mvn clean install -Pintegrate`
		 
	 - After running above command, docker images will be created on host machine.
	 
		 `/moviebuzz-integration_kafka-processor                                      1.0.0.0-SNAPSHOT`
	 
		 `/moviebuzz-integration_api                                            1.0.0.0-SNAPSHOT`
	 
	 
	 - All the required Container Stack will be up on your local machine to run integration tests.
	 - If all the integration tests are successful, Build will be successful otherwise build will fail.

 # Contributing
 Feel free to Fork a repo and submit a PR.
You can add support for following pending Tasks. 
 - Adding APIs to delete existing Entities from ElasticSearch and Cassandra
 - Support for ElasticSeach update, delete Query in ElasticsearchService
 - Integrating SQL database for real time Bookings
 - SQL database schema
 
