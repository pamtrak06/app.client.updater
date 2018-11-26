#!/bin/sh

APP_NAME=&1
ARCHIVE_PATH=&2
VERSIONS_PATH=&3

java -classpath ./*.jar -jar <jar-name> org.app.client.updater.UpdateServerVersions $APP_NAME $ARCHIVE_PATH $VERSIONS_PATH