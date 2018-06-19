#/bin/bash

cd ../../../;
mvn package;
cd ./src/main/resources/;

#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &
#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &


#debugAD="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n"
#debugACP="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n"
#debugESB="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9002,suspend=n"
#debugAMQ="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9003,suspend=n"
#debugABL="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9004,suspend=n"

jmxEnable="--jar ../../../target/test/WEB-INF/lib/jetty-jmx-9.4.11.v20180605.jar --config ../../../src/etc/jetty-jmx.xml"

appdAD="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App1 -Dappdynamics.agent.tierName=Hybris-AD -Djetty.jmxrmiport=9100 "
appdACP="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App2 -Dappdynamics.agent.tierName=Hybris-ACP -Djetty.jmxrmiport=9101 "
appdESB="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App3 -Dappdynamics.agent.tierName=EI-ESB -Djetty.jmxrmiport=9102 "
appdAMQ="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App4 -Dappdynamics.agent.tierName=EI-AMQ -Djetty.jmxrmiport=9103 "
appdABL="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=EJ.AbstractionLayer -Djetty.jmxrmiport=9104 "
appdCPG="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App6 -Dappdynamics.agent.tierName=CustomerPayments.Gateway -Djetty.jmxrmiport=9105 "

#Start Hybris-AD
java $debugAD  $appdAD -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8181 ../../../target/test.war > /tmp/server.log &

#Start Hybris-ACP
java $debugACP $appdACP -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8282 ../../../target/test.war >> /tmp/server.log &

#Start EI-ESB
java $debugESB $appdESB -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8383 ../../../target/test.war >> /tmp/server.log &

#Start EI-AMQ
java $debugAMQ $appdAMQ -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8484 ../../../target/test.war >> /tmp/server.log &

#Start AbstractionLayer
java $debugABL $appdABL -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8585 ../../../target/test.war >> /tmp/server.log &

#Start CustomerPayments.Gateway
java $debug $appdCPG -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8686 ../../../target/test.war >> /tmp/server.log &

tail -f /tmp/server.log