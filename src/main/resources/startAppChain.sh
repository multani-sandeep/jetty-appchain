#/bin/bash

. ./local.sh
echo > /tmp/server.log
#set -x
#./jmeter.sh -n -t ~/git/jetty-appchain/FCP_R1.jmx -l /tmp/FCP_R1.txt

if [[ $* = *'-nobuild'* ]]; then
	echo "Not building"
else
	cd $APPCHAIN_HOME;
	mvn clean && mvn generate-sources && mvn package
	cd $APPCHAIN_HOME"/src/main/resources";
	
fi

if [[ $* = *'-nodb'* ]]; then
	echo "Not refreshing db"
else
	java -cp "$APPCHAIN_HOME"/target/test-jar-with-dependencies.jar com.appdynamics.test.Database
	
	
fi

#mvn package && java -javaagent:/Users/sandeepsingh/Downloads/appagent/javaagent.jar -Dappdynamics.agent.nodeName=T-App11  -Dappdynamics.agent.tierName=MSW -Dappdynamics.agent.applicationName=Test -Djetty.jmxrmiport=2010   -jar ./target/dependency/jetty-runner.jar  --port 9191 ./target/test.war >> /tmp/server.log

#debugport="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,suspend=n,address=500"
#jmxport="-Djetty.jmxrmiport=600"
#jmxEnable="--jar $APPCHAIN_HOME/target/test/WEB-INF/lib/jetty-jmx-9.4.11.v20180605.jar --config "$APPCHAIN_HOME"/src/etc/jetty-jmx.xml"
jettyJar="-jar $APPCHAIN_HOME/target/dependency/jetty-runner.jar $jmxEnable"
jettyWar="$APPCHAIN_HOME/target/test.war"
appd="-javaagent:$APPD_AGENT_JAR -Dappdynamics.agent.applicationName=$APPD_AGENT_APP_NAME -Dappdynamics.agent.nodeName="$APPD_AGENT_NODE_NAME_PREFIX"App<APP_INDEX> -Dappagent.install.dir=$APPD_AGENT_HOME"
errDisable="-Ddisable-errors=true"
delayDisable="-Ddisable-delay=true"
appport="808<APP_INDEX>"


sleep $APP_START_DELAY

#Start ManageBooking
appIndex=1
#$debugport$appIndex $jmxport$appIndex
java $errDisable $delayDisable ${appd/\<APP_INDEX\>/$appIndex} -Dappdynamics.agent.tierName=ManageBooking $jettyJar --port ${appport/\<APP_INDEX\>/$appIndex} $jettyWar  > /tmp/server.log &

sleep $APP_START_DELAY

#Start B2B
appIndex=2
#$debugport$appIndex $jmxport$appIndex
java $errDisable $delayDisable ${appd/\<APP_INDEX\>/$appIndex} -Dappdynamics.agent.tierName=B2B-Test $jettyJar --port ${appport/\<APP_INDEX\>/$appIndex} $jettyWar  > /tmp/server.log &

sleep $APP_START_DELAY

#Start Seating
appIndex=3
#$debugport$appIndex $jmxport$appIndex
java $errDisable $delayDisable ${appd/\<APP_INDEX\>/$appIndex} -Dappdynamics.agent.tierName=Seating $jettyJar --port ${appport/\<APP_INDEX\>/$appIndex} $jettyWar  > /tmp/server.log &

sleep $APP_START_DELAY

#Start Payments
appIndex=4
#$debugport$appIndex $jmxport$appIndex
java $errDisable $delayDisable ${appd/\<APP_INDEX\>/$appIndex} -Dappdynamics.agent.tierName=Payments $jettyJar --port ${appport/\<APP_INDEX\>/$appIndex} $jettyWar  > /tmp/server.log &


set +x

(sleep 30 && ps -ef | grep jetty | grep -v "grep" | sed 's/.*tierName=\([^ ]*\) .*/\1/' >> /tmp/server.log)  &


tail -f /tmp/server.log