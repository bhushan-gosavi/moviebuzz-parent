# Moviebuzz
Moviebuzz is a highly scalable Movie Reviews and  Movie Ticket Booking platform created for learning purpose with Production Grade Code. 

# Platform Features

 - Horizontally scalable
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
	 `mvn clean install -Pintegration`
		 
	 - After running above command, docker images will be created on host machine.
	 
		 `/moviebuzz-integration_kafka-processor                                      1.0.0.0-SNAPSHOT`
	 
		 `/moviebuzz-integration_api                                            1.0.0.0-SNAPSHOT`
	 
	 
	 - All the required Container Stack will be up on your local machine to run integration tests.
	 - If all the integration tests are successful, Build will be successful otherwise build will fail.
 
