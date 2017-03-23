#!/bin/bash

cd src
java -cp ".:../libs/commons-cli-1.3.1.jar:../libs/sqlite-jdbc-3.16.1.jar" GossipP2PServer  -p 3333 -d "../res/test.db" &
SERVER_PID=$!
sleep 1
cd ..

printf "Test 1 peers call\n"
nc localhost 3333 <<< "PEERS?\n"

printf "\n*************************************\n"

sleep 1

printf "\nTest 2 adding new peer\n"
nc localhost 3333 <<< "PEER:Fred:PORT=4641:IP=localhost%"

sleep 1

nc localhost 3333 <<< "PEERS?\n"

printf "\n*************************************\n"

sleep 1

printf "\nTest 3 broadcast message to known peers call\n"


nc -l localhost 4641 &
NC_PID=$!

nc localhost 3333 <<< "GOSSIP:mBHL7IKilvdcOFKR03ASvBNX//ypQkTRUvilYmB1/OY=:2017-01-09-16-18-20-001Z:Tom eats Jerry%"

printf "\n*************************************\n"

sleep 1

kill $SERVER_PID

rm "./res/test.db"
