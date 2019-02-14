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
./ant -f $REPO_HOME/etracs/rameses-treasury-common clean jar
./ant -f $REPO_HOME/etracs/rameses-etracs-main clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-entity clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-tools clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-lgu clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-treasury clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-lgu-treasury clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-treasury-report clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-treasury-tool clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-depositslip clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-signatory-template clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-bpls clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-bpls-reports clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-bpls-ceo clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-bpls-tracs clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis-report clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis-city clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis-municipality clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis-province clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis-landtax clean jar
./ant -f $REPO_HOME/etracs/rameses-gov-etracs-rptis-landtax-report clean jar
