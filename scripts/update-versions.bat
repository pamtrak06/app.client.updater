@echo off

set APP_NAME=%1
set ARCHIVE_PATH=%2
set VERSIONS_PATH=%3

java -jar <jar-name> org.app.client.updater.UpdateServerVersions %APP_NAME% %ARCHIVE_PATH% %VERSIONS_PATH%
