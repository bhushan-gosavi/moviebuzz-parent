#!/bin/bash

: ${CONSUL_HOST:="consul"}
: ${CONSUL_PORT_ADDR:=8500}

JAVAOPTS="-Dspring.cloud.consul.host=$CONSUL_HOST \
  -Dspring.cloud.consul.port=$CONSUL_PORT_ADDR \
  -javaagent:/opt/appd/javaagent.jar \
  -Dappdynamics.agent.nodeName=gateway-dockerHost \
  -Dappdynamics.agent.applicationName=moviebuzz \
  -Dappdynamics.agent.tierName=gateway
  $JAVA_OPTS"

echo $JAVAOPTS

exec java $JAVAOPTS -jar /opt/app.jar