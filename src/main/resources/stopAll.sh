#/bin/bash

ps -ef | grep jetty-runner | grep -v grep | cut -d' ' -f 5 | xargs kill -9