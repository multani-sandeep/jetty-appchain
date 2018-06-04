#/bin/bash

#Start Hybris-AD
java -Ddown_port=8282 -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App1 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &

#Start Hybris-ACP
java -Dtype=proxy -Ddown_port=8383 -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App2 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &

#Start EI-ESB
java -Dtype=proxy -Ddown_port=8585 -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App3 -Dappdynamics.agent.tierName=EI-ESB -jar ../../../target/dependency/jetty-runner.jar --port 8383 ../../../target/test.war | tee -a /tmp/server.log &

#Start EI-AMQ
java -Dtype=proxy -Ddown_port=8585 -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App4 -Dappdynamics.agent.tierName=EI-AMQ -jar ../../../target/dependency/jetty-runner.jar --port 8484 ../../../target/test.war | tee -a /tmp/server.log &

#Start AbstractionLayer
java -Dtype=server -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=EJ.AbstractionLayer -jar ../../../target/dependency/jetty-runner.jar --port 8585 ../../../target/test.war | tee -a /tmp/server.log &
