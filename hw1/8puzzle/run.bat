@echo off
javac -encoding utf-8 -d ./bin -sourcepath ./src src/*.java src/jigsaw/*.java
pause
cd ./bin
java Runner
pause