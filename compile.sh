#!/bin/bash

ARG1=$1

find ./src -name *.java | javac -cp ./libs/commons-cli-1.3.1.jar:./libs/sqlite-jdbc-3.16.1.jar @/dev/stdin

if [ "$ARG1" == "clean" ]; then
    find ./src -name *.class -type f -delete
fi