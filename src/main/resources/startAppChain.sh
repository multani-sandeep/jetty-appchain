#/bin/bash

cd /Volumes/EXP2/ei-playpit/jetty-appchain/test;
mvn package;
cd /Volumes/EXP2/ei-playpit/jetty-appchain/test/src/main/resources;

#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &
#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &


#Start Hybris-AD
java -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App1 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &

#Start Hybris-ACP
java -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App2 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &

#Start EI-ESB
java -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App3 -Dappdynamics.agent.tierName=EI-ESB -jar ../../../target/dependency/jetty-runner.jar --port 8383 ../../../target/test.war | tee -a /tmp/server.log &

#Start EI-AMQ
java -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App4 -Dappdynamics.agent.tierName=EI-AMQ -jar ../../../target/dependency/jetty-runner.jar --port 8484 ../../../target/test.war | tee -a /tmp/server.log &

#Start AbstractionLayer
java -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=EJ.AbstractionLayer -jar ../../../target/dependency/jetty-runner.jar --port 8585 ../../../target/test.war | tee -a /tmp/server.log &