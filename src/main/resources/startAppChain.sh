#/bin/bash

cd ../../../;
mvn package && java -cp ./target/test-jar-with-dependencies.jar com.appdynamics.test.Database
cd ./src/main/resources/;

#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &
#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &


#debugAD="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n"
#debugACP="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n"
#debugESB="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9002,suspend=n"
#debugAMQ="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9003,suspend=n"
#debugABL="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9004,suspend=n"
#debugMSW="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9005,suspend=n -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog -Dorg.apache.commons.logging.simplelog.showdatetime=true -Dorg.apache.commons.logging.simplelog.log.org.apache.http=DEBUG"

jmxEnable="--jar ../../../target/test/WEB-INF/lib/jetty-jmx-9.4.11.v20180605.jar --config ../../../src/etc/jetty-jmx.xml"

#appdAD="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App1 "
#appdACP="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App2 "
#appdESB="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App3 "
#appdAMQ="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App4 "
#appdABL="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App5 "
#appdCPG="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App6 "
#appdMSW="-javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=App7 "


jettyAD=" -Djetty.jmxrmiport=9100 -Dappdynamics.agent.tierName=Hybris-AD "
jettyACP=" -Djetty.jmxrmiport=9101 -Dappdynamics.agent.tierName=Hybris-ACP "
jettyESB="  -Djetty.jmxrmiport=9102 -Dappdynamics.agent.tierName=EI-ESB "
jettyAMQ=" -Djetty.jmxrmiport=9103 -Dappdynamics.agent.tierName=EI-AMQ "
jettyABL=" -Djetty.jmxrmiport=9104 -Dappdynamics.agent.tierName=EJ.AbstractionLayer "
jettyCPG=" -Djetty.jmxrmiport=9105 -Dappdynamics.agent.tierName=CustomerPayments.Gateway "
jettyMSW=" -Djetty.jmxrmiport=9106 -Dappdynamics.agent.tierName=MS " 

sleep 2
#Start Hybris-AD
set +x
java $debugAD $jettyAD  $appdAD -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8181 ../../../target/test.war > /tmp/server.log &

sleep 3
#Start Hybris-ACP
java $debugACP $jettyACP $appdACP -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8282 ../../../target/test.war >> /tmp/server.log &

sleep 2
#Start EI-ESB
java $debugESB $jettyESB $appdESB -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8383 ../../../target/test.war >> /tmp/server.log &

sleep 2
#Start EI-AMQ
java $debugAMQ $jettyAMQ $appdAMQ -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8484 ../../../target/test.war >> /tmp/server.log &

sleep 2
#Start AbstractionLayer
java $debugABL $jettyABL $appdABL -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8585 ../../../target/test.war >> /tmp/server.log &

sleep 2
#Start CustomerPayments.Gateway
java $debug $jettyCPG $appdCPG -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8686 ../../../target/test.war >> /tmp/server.log &

sleep 4
#Start CustomerPayments.Gateway
java $debugMSW $jettyMSW $appdMSW -jar ../../../target/dependency/jetty-runner.jar $jmxEnable --port 8787 ../../../target/test.war >> /tmp/server.log &

set +x


tail -f /tmp/server.log