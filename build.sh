#!/bin/bash

mkdir -p bin/
cd src/
javac * -d ../bin/
status=$?
cd ..
exit $status
