#!/bin/sh

taskkill //PID $(netstat -ano | findstr :8080 | awk 'NR==1{print $NF}') //f
taskkill //PID $(netstat -ano | findstr :8081 | awk 'NR==1{print $NF}') //f
taskkill //PID $(netstat -ano | findstr :8082 | awk 'NR==1{print $NF}') //f
