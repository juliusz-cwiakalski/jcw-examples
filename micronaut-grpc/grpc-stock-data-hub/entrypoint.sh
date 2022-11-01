#!/bin/sh

#Following https://unix.stackexchange.com/questions/146756/forward-sigterm-to-child-in-bash

_term() {
  echo "Caught SIGTERM signal! forwarding to java process"
  kill "$child" 2>/dev/null
  echo "sent kill $child process - waiting to stop"
  wait "$child"
  exit 0
}

trap _term SIGTERM

java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /app.jar &

child=$!

echo "Child process is $child"

wait "$child"
