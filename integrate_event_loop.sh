#!/bin/bash

set -e

EVENT_LOOP_DIR="Implementation"
CLIENT_DIR="Client"
JAR_NAME="event-loop.jar"
LIBS_DIR="$CLIENT_DIR/libs"

echo $(date +"%D %T") "| COMPILING EVENT LOOP..."
mkdir -p "$EVENT_LOOP_DIR/out"
find "$EVENT_LOOP_DIR/src" -name "*.java" ! -path "$EVENT_LOOP_DIR/src/tests" > sources.txt
javac -d "$EVENT_LOOP_DIR/out" @sources.txt
rm sources.txt

echo $(date +"%D %T") "| CREATING EVENT LOOP JAR..."
jar cvf "$EVENT_LOOP_DIR/$JAR_NAME" -C "$EVENT_LOOP_DIR/out" .

echo $(date +"%D %T") "| ADDING EVENT LOOP JAR TO THE CLIENT..."
mkdir -p "$LIBS_DIR"
cp "$EVENT_LOOP_DIR/$JAR_NAME" "$LIBS_DIR/"

echo $(date +"%D %T") "| BUILDING CLIENT WITH GRADLE..."
cd "$CLIENT_DIR"
gradle clean build

echo $(date +"%D %T") "| INTEGRATION COMPLETED SUCCESSFULLY"
