#!/bin/bash

PORT=$1

cd src
java -cp ".:../libs/commons-cli-1.3.1.jar:../libs/sqlite-jdbc-3.16.1.jar" GossipP2PServer  -p "$PORT" -d "../res/database.db"