#/bin/bash


. ./local.sh
echo > /tmp/server.log

#./jmeter.sh -n -t ~/git/jetty-appchain/FCP_R1.jmx -l /tmp/FCP_R1.txt

cd $APPCHAIN_HOME;
 mvn clean && mvn generate-sources && mvn package && java -cp "$APPCHAIN_HOME"/target/test-jar-with-dependencies.jar com.appdynamics.test.Database
cd $APPCHAIN_HOME"/src/main/resources";

#mvn package && java   -jar ./target/dependency/jetty-runner.jar  --port 9191 ./target/test.war >> /tmp/server.log


#debugAD="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20000,suspend=n"
#debugACP="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20001,suspend=n"
#debugESB="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20002,suspend=n"
#debugAMQ="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20003,suspend=n"
#debugABL="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=20004,suspend=n"
#debugMSW="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=200005 suspend=n -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog -Dorg.apache.commons.logging.simplelog.showdatetime=true -Dorg.apache.commons.logging.simplelog.log.org.apache.http=DEBUG"

jmxEnable="--jar $APPCHAIN_HOME/target/test/WEB-INF/lib/jetty-jmx-9.4.11.v20180605.jar --config "$APPCHAIN_HOME"/src/etc/jetty-jmx.xml"


appdACP="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App2 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=Hybris-ACP -Djetty.jmxrmiport=2001"
appdESB="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App3 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=EI-ESB -Djetty.jmxrmiport=2002"
appdABL="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App5 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=EJ.AbstractionLayer -Djetty.jmxrmiport=2004"
appdAPIGWY="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App8 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=APIGWY -Djetty.jmxrmiport=2007"
appdB2B="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App9 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=B2B -Djetty.jmxrmiport=2008"
appdMeSW="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App11 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=MSW -Djetty.jmxrmiport=2010"

#appdAD="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App1  -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=Hybris-AD -Djetty.jmxrmiport=2000"
#appdCPG="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App6 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=CustomerPayments.Gateway -Djetty.jmxrmiport=2005"
#appdAMQ="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App4 -Dappagent.install.dir=$APPD_AGENT_HOME -Dappdynamics.agent.tierName=EI-AMQ -Djetty.jmxrmiport=2003"
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

#./jmeter.sh -n -t ~/git/jetty-appchain/FCP_R1.jmx -l /tmp/FCP_R1.txt

# Machine Agent Config End

sleep $APP_START_DELAY
sleep $APP_START_DELAY
#Start Hybris-AD
set +x
#java $debugAD  $appdAD -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8181 $APPCHAIN_HOME/target/test.war > /tmp/server.log &

sleep $APP_START_DELAY
#Start Hybris-ACP
java $debugACP $appdACP -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8282 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start EI-ESB
java $debugESB $appdESB -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8383 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start EI-AMQ
#java $debugAMQ $appdAMQ -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8484 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start AbstractionLayer
java $debugABL $appdABL -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8585 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start CustomerPayments.Gateway
#java $debug $appdCPG -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8686 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start MS
#java $debugMSW $appdMSW -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 9191 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start API.Gateway
#java  $jettyAPIGWY -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8888 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start B2B
java $debugB2B $appdB2B -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 8989 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &

sleep $APP_START_DELAY
#Start ERES -- Now modelled as Database
#java $debugERES $appdERES -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 9090 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &


sleep $APP_START_DELAY
#Start MSW
java $debugMeSW $appdMeSW -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 9191 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &


sleep $APP_START_DELAY
#Start AKAMAI
#java $debugAkamai $jettyAkamai -jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable --port 9292 $APPCHAIN_HOME/target/test.war >> /tmp/server.log &


set +x

(sleep 30 && ps -ef | grep jetty | grep -v "grep" | sed 's/.*tierName=\([^ ]*\) .*/\1/' >> /tmp/server.log)  &


tail -f /tmp/server.log