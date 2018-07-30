#/bin/bash

cd /c/jetty-app/jetty-appchain/;
mvn package;
cd /c/jetty-app/jetty-appchain/src/main/resources/;

#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9000,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-AD -jar ../../../target/dependency/jetty-runner.jar --port 8181 ../../../target/test.war | tee -a /tmp/server.log &
#java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9001,suspend=n -Dappdynamics.agent.nodeName=App5 -Dappdynamics.agent.tierName=Hybris-ACP -jar ../../../target/dependency/jetty-runner.jar --port 8282 ../../../target/test.war | tee -a /tmp/server.log &


#debugAD="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20000,suspend=n"
#debugACP="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20001,suspend=n"
#debugESB="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20002,suspend=n"
#debugAMQ="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20003,suspend=n"
#debugABL="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20004,suspend=n"
debugMSW="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=200005 suspend=n -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog -Dorg.apache.commons.logging.simplelog.showdatetime=true -Dorg.apache.commons.logging.simplelog.log.org.apache.http=DEBUG"

jmxEnable="--jar /c/jetty-app/jetty-appchain/target/test/WEB-INF/lib/jetty-jmx-9.4.11.v20180605.jar --config /c/jetty-app/jetty-appchain/src/etc/jetty-jmx.xml"


appdAD="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App1  -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=Hybris-AD -Djetty.jmxrmiport=2000"
appdACP="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App2 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=Hybris-ACP -Djetty.jmxrmiport=2001"
appdESB="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App3 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=EI-ESB -Djetty.jmxrmiport=2002"
appdAMQ="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App4 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=EI-AMQ -Djetty.jmxrmiport=2003"
appdABL="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App5 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=EJ.AbstractionLayer -Djetty.jmxrmiport=2004"
appdCPG="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App6 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=CustomerPayments.Gateway -Djetty.jmxrmiport=2005"
appdMSW="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App7 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=MS -Djetty.jmxrmiport=2010"
appdAPIGWY="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App8 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=APIGWY -Djetty.jmxrmiport=2007"
appdB2B="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App9 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=B2B -Djetty.jmxrmiport=2008"
appdERES="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App10 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=ERES -Djetty.jmxrmiport=2009"
appdMeSW="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App11 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=MSW -Djetty.jmxrmiport=2010"
appdAkamai="-javaagent:/c/AppServerAgent-4.3.7.1/javaagent.jar -Dappdynamics.agent.nodeName=G-App1 -Dappagent.install.dir=/C/AppServerAgent-4.3.7.1 -Dappdynamics.agent.tierName=AKAMAI -Djetty.jmxrmiport=2011"


sleep 2
#Start Hybris-AD
set +x
java $debugAD  $appdAD -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8181 /c/jetty-app/jetty-appchain/target/test.war > /tmp/server.log &

sleep 3
#Start Hybris-ACP
java $debugACP $appdACP -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8282 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 2
#Start EI-ESB
java $debugESB $appdESB -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8383 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 2
#Start EI-AMQ
java $debugAMQ $appdAMQ -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8484 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 2
#Start AbstractionLayer

java $debugABL $appdABL -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8585 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 2
#Start CustomerPayments.Gateway
java $debug $appdCPG -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8686 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 4
#Start MS
#java $debugMSW $appdMSW -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9191 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 4
#Start API.Gateway
java $debugAPIGWY $appdAPIGWY -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8888 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 4
#Start B2B
java $debugB2B $appdB2B -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8989 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 4
#Start ERES
java $debugERES $appdERES -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9090 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &


sleep 4
#Start MSW
java $debugMeSW $appdMeSW -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9191 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &

sleep 4
#Start AKAMAI
java $debugAkamai $appdAkamai -jar /c/jetty-app/jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9292 /c/jetty-app/jetty-appchain/target/test.war >> /tmp/server.log &


set +x


tail -f /tmp/server.log