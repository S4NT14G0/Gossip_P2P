#!/bin/bash

ARG1=$1

mkdir -p ./bin

find ./src -name *.java | javac -d ./bin -cp ./libs/commons-cli-1.3.1.jar:./libs/sqlite-jdbc-3.16.1.jar @/dev/stdin

if [ "$ARG1" == "clean" ]; then
    find ./bin -name *.class -type f -delete
fi