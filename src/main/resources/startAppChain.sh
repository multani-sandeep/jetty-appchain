#/bin/bash

cd /Users/sandeepsingh/git/jetty-appchain/;
mvn package;
cd /Users/sandeepsingh/git/jetty-appchain/src/main/resources/;

#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &
#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &


#debugAD="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n"
#debugACP="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n"
#debugESB="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9002,suspend=n"
#debugAMQ="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9003,suspend=n"

#Start Hybris-AD
java $debugAD -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App1 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war > /tmp/server.log &

#Start Hybris-ACP
java $debugACP -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App2 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war >> /tmp/server.log &

#Start EI-ESB
java $debugESB -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App3 -Dappdynamics.agent.tierName=EI-ESB -jar ../../../target/dependency/jetty-runner.jar --port 8383 ../../../target/test.war >> /tmp/server.log &

#Start EI-AMQ
java $debugAMQ -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App4 -Dappdynamics.agent.tierName=EI-AMQ -jar ../../../target/dependency/jetty-runner.jar --port 8484 ../../../target/test.war >> /tmp/server.log &

#Start AbstractionLayer
java $debug -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=EJ.AbstractionLayer -jar ../../../target/dependency/jetty-runner.jar --port 8585 ../../../target/test.war >> /tmp/server.log &

#Start CustomerPayments.Gateway
java $debug -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App6 -Dappdynamics.agent.tierName=CustomerPayments.Gateway -jar ../../../target/dependency/jetty-runner.jar --port 8686 ../../../target/test.war >> /tmp/server.log &
