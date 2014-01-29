@echo off

set cp=.\;.\bin\
set cp=%cp%;.\lib\commons-beanutils-1.8.3.jar
set cp=%cp%;.\lib\commons-beanutils-bean-collections-1.8.3.jar
set cp=%cp%;.\lib\commons-beanutils-core-1.8.3.jar
set cp=%cp%;.\lib\commons-codec-1.6.jar
set cp=%cp%;.\lib\commons-collections-3.2.1.jar
set cp=%cp%;.\lib\commons-configuration-1.9.jar
set cp=%cp%;.\lib\commons-lang-2.6.jar
set cp=%cp%;.\lib\commons-logging-1.1.1.jar
set cp=%cp%;.\lib\commons-math3-3.1.1.jar
set cp=%cp%;.\lib\fluent-hc-4.2.3.jar
set cp=%cp%;.\lib\httpclient-4.2.3.jar
set cp=%cp%;.\lib\httpclient-cache-4.2.3.jar
set cp=%cp%;.\lib\httpcore-4.2.2.jar
set cp=%cp%;.\lib\httpmime-4.2.3.jar
set cp=%cp%;.\lib\java-yield-custom.jar
set cp=%cp%;.\lib\javatuples-1.2.jar
set cp=%cp%;.\lib\jcommon-1.0.17.jar
set cp=%cp%;.\lib\jfreechart-1.0.14.jar
set cp=%cp%;.\lib\jhdf5.jar
set cp=%cp%;.\lib\jhdf5obj.jar
set cp=%cp%;.\lib\jhdfobj.jar
set cp=%cp%;.\lib\joda-time-2.2.jar
set cp=%cp%;.\lib\junit.jar
set cp=%cp%;.\lib\log4j-1.2.17.jar
set cp=%cp%;.\lib\mongo-java-driver-2.11.3.jar
set cp=%cp%;.\lib\postgresql-9.2-1003.jdbc4.jar
set cp=%cp%;.\lib\RCaller-2.1.1-SNAPSHOT.jar
set cp=%cp%;.\lib\super-csv-2.1.0.jar





java -cp %cp%   com.jbp.randommaster.datasource.historical.viewer.HkDerivativesMasterDBPriceViewer 
