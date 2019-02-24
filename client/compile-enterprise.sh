#!/bin/bash
NETBEANS_HOME=/c/TEMP/netbeans72-projects-treasury
ANT_HOME="$NETBEANS_HOME/java/ant"
REPO_HOME=`pwd`
echo ""
echo "NETBEANS_HOME: $NETBEANS_HOME"
echo "ANT_HOME     : $ANT_HOME"
echo "JAVA_HOME    : $JAVA_HOME"
echo "REPO_HOME    : $REPO_HOME"
cd $ANT_HOME/bin
chmod +x ant
./ant -version
echo ""
./ant -f $REPO_HOME/rameses-enterprise clean jar
./ant -f $REPO_HOME/rameses-enterprise-admin clean jar
./ant -f $REPO_HOME/rameses-enterprise-entity clean jar
./ant -f $REPO_HOME/rameses-enterprise-financial clean jar
