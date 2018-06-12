#/bin/bash

ps -ef | grep jetty-runner | grep -v grep | cut -d' ' -f 4,5 | xargs kill -9
ps -ef | grep jetty-runner | grep -v grep
