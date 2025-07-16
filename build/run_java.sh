#!/bin/bash

cd ${HOME}
CMD=$1

java -cp "out:lib/sqlite-jdbc-3.50.2.0.jar" ${CMD}
