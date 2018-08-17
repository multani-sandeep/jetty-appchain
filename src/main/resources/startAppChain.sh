#/bin/bash


APPCHAIN_HOME=/Users/sandeepsingh/git/jetty-appchain/
APPD_AGENT_HOME=/Users/sandeepsingh/Downloads/appagent/
APPD_AGENT_JAR="$APPD_AGENT_HOME"/javaagent.jar
APPD_AGENT_NODE_NAME_PREFIX=S-
APPD_MACH_AGENT_HOME="/C/MachineAgent-4.5.0.1285/"
APPD_MACH_AGENT_JAR="$APPD_MACH_AGENT_HOME"/machineagent.jar
APP_START_DELAY=2

cd $APPCHAIN_HOME;
 mvn clean && mvn -X generate-sources && mvn package && java -cp "$APPCHAIN_HOME"/target/test-jar-with-dependencies.jar com.appdynamics.test.Database
cd $APPCHAIN_HOME"/src/main/resources";

#mvn package && java   -jar ./target/dependency/jetty-runner.jar  --port 9191 ./target/test.war >> /tmp/server.log


#debugAD="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20000,suspend=n"
#debugACP="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20001,suspend=n"
#debugESB="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20002,suspend=n"
#debugAMQ="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20003,suspend=n"
#debugABL="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20004,suspend=n"
#debugMSW="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=200005 suspend=n -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog -Dorg.apache.commons.logging.simplelog.showdatetime=true -Dorg.apache.commons.logging.simplelog.log.org.apache.http=DEBUG"

jmxEnable="--jar ../../../../jetty-appchain/target/test/WEB-INF/lib/jetty-jmx-9.4.11.v20180605.jar --config "$APPCHAIN_HOME"/src/etc/jetty-jmx.xml"


appdAD="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App1  -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=Hybris-AD -Djetty.jmxrmiport=2000"
appdACP="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App2 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=Hybris-ACP -Djetty.jmxrmiport=2001"
appdESB="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App3 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=EI-ESB -Djetty.jmxrmiport=2002"
appdAMQ="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App4 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=EI-AMQ -Djetty.jmxrmiport=2003"
appdABL="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App5 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=EJ.AbstractionLayer -Djetty.jmxrmiport=2004"
appdCPG="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App6 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=CustomerPayments.Gateway -Djetty.jmxrmiport=2005"
appdAPIGWY="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App8 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=APIGWY -Djetty.jmxrmiport=2007"
appdB2B="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App9 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=B2B -Djetty.jmxrmiport=2008"
appdMeSW="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App11 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=MSW -Djetty.jmxrmiport=2010"
#appdERES="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App10 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=ERES -Djetty.jmxrmiport=2009"
#appdMSW="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App7 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=MS -Djetty.jmxrmiport=2010"

 
jettyAkamai=" -Djetty.jmxrmiport=2011 -Dappdynamics.agent.tierName=AKAMAI"
jettyAPIGWY=" -Djetty.jmxrmiport=2007 -Dappdynamics.agent.tierName=APIGWY"


# Machine Agent Config Start

#appdmachineAD="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App1  -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=Hybris-AD" 
#appdmachineACP="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App2 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=Hybris-ACP"
#appdmachineESB="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App3 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=EI-ESB"
#appdmachineAMQ="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App4 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=EI-AMQ"
#appdmachineABL="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App5 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=EJ.AbstractionLayer"
#appdmachineCPG="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App6 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=CustomerPayments.Gateway"
#appdmachineMSW="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App7 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=MS"
#appdmachineAPIGWY="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App8 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=APIGWY"
#appdmachineB2B="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App9 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=B2B"
#appdmachineERES="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App10 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=ERES"
#appdmachineMeSW="-machineagent:$APPD_MACH_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App11 -Dmachineagent.install.dir=$APPD_MACH_AGENT_HOME -Dappdynamics.agent.tierName=MSW"


# Machine Agent Config End

sleep $APP_START_DELAY
#Start Hybris-AD
set +x
java $debugAD  $appdAD -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8181 ../../../../jetty-appchain/target/test.war > /tmp/server.log &

sleep $APP_START_DELAY
#Start Hybris-ACP
java $debugACP $appdACP -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8282 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start EI-ESB
java $debugESB $appdESB -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8383 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start EI-AMQ
java $debugAMQ $appdAMQ -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8484 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start AbstractionLayer

java $debugABL $appdABL -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8585 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start CustomerPayments.Gateway
java $debug $appdCPG -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8686 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start MS
#java $debugMSW $appdMSW -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9191 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start API.Gateway
java  $jettyAPIGWY -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8888 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start B2B
java $debugB2B $appdB2B -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 8989 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start ERES -- Now modelled as Database
#java $debugERES $appdERES -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9090 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &


sleep $APP_START_DELAY
#Start MSW
java $debugMeSW $appdMeSW -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9191 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &


sleep $APP_START_DELAY
#Start AKAMAI
java $debugAkamai $jettyAkamai -jar ../../../../jetty-appchain/target/dependency/jetty-runner.jar $jmxEnable --port 9292 ../../../../jetty-appchain/target/test.war >> /tmp/server.log &


set +x


tail -f /tmp/server.log