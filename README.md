# Application software updater

## Use case - server side

### 1. USE CASE :  NEW APP TO INSTANTIATE
### 2. USE CASE :  NEW APP VERSION TO INTEGRATE
### 3. USE CASE :  NEW APP VERSION TO DEPLOY
### 4.  USE CASE :  ANALYSIS OF DEPLOYMENT RESULT


## Use case - client side

### 1. USE CASE : GET NEW VERSION
	- WHICH
		- DEFINITIVE LAST VERSION (FROM VERSION NUMBER OR CHECKSUM)
		- MARKED AS ACTIVE VERSION
		- CHOOSE VERSION
	- HOW
		- IDENTIFY INSTALLED VERSION
		- INSTALL A VERSION
		- WRITE DEPLOYMENT RESULT (LOCAL, REMOTE)
	
### 2. USE CASE : GET OFFLINE VERSION
	- DOWNLOAD ARCHIVE + JSON in specific path
	- RUN APP CLIENT UPDATER
	
### 3. USE CASE : DEGRADED
	- NO CONNECTION
	- LOOSE CONEECTION
	- RESOURCES UNAVAILABLE

## TESTS CASE

### 1. Test reading app config.json file
UpdaterConfigTest	OK

### 2. Test reading app version.json file
AppVersionsTest		OK

### 3. Test updater mode
	- offline installation 
		(file already downloaded, server connection off)			NOK
	- online 
		(connection with server ok)						NOK		

### 4. Test checking local app version structure
AppStructureTest	OK
	- FOLDER exist								
	- FILE exist
	- checksum ok
	
### 5. Test requirements
	- third-party applications
		- exist ? (ex. : database mySql)
		- process is running ? (ex. : database mySql connection started)

### 6. Test find local versions
	- identify version
		- filename
		- checksum
		- contents information
		
### 7. Test find remote versions						NOK
	- getLatestVersion							NOK
		- method version enabled				NOK
		- method upper version					NOK
		- from os name & version				NOK
		- from os name only						NOK
		- all os version available				NOK
		- context os not available				NOK
		- context : os version unavail.			NOK

### 8. Test download glis file
		- one from os name & version			NOK
		- one from os name only					NOK
		- all os version available				NOK

### 9. Test unzip glis archive


### 10. Test execute main file from archive installed

### 11. Test record result of update processus
	- in local file
	- in remote file
	- in remote database
	- stored informations
		- os name and version
		- installation
			- app version
			- path
			- date

### 12. Test no connection, put to offline mode		NOK

### 13. Test internationalization			NOK

### 14. Test logger					NOK
	
## TODO LIST

* Retrieve proxy from system
* Schema validator : http://jsonlint.com/, http://json.parser.online.fr/
* Web project
* I18n
* Write to server : upload file
* Offline mode
* Admin mode : merge versions file, json editor
* Http degraded tests : no connection, json absent, archives absent, download interruption
* Progress bar for http process + file search
* jnlp generation
* Ui to manage server side:
	- project : create new
		- create new <app>-config.json
		- create new <app>-versions.json
		- create new <app>.jnlp
	- project : validation
		- validate <app>-config.json
		- validate <app>-versions.json
		- validate <app>.jnlp
		- validate *.glis (zip valid)
		- validate all <app>-...-version.json corresponding to each *.glis
	- project : update
		- modify <app>-config.json
		- modify new <app>-versions.json
			- add new version
			- remove version
	- project : update versions files
		- update global <app>-versions.json file from each <app>-...-version.json corresponding to each *.glis

	
