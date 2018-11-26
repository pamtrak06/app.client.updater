# ----------------------------------------
# USE CASE - SERVER
# ----------------------------------------

# USE CASE :  NEW APP TO INSTANTIATE

# USE CASE :  NEW APP VERSION TO INTEGRATE

# USE CASE :  NEW APP VERSION TO DEPLOY

# USE CASE :  ANALYSIS OF DEPLOYMENT RESULT


# ----------------------------------------
# USE CASE - CLIENT
# ----------------------------------------

# USE CASE : GET NEW VERSION
	- WHICH
		- DEFINITIVE LAST VERSION (FROM VERSION NUMBER OR CHECKSUM)
		- MARKED AS ACTIVE VERSION
		- CHOOSE VERSION
	- HOW
		- IDENTIFY INSTALLED VERSION
		- INSTALL A VERSION
		- WRITE DEPLOYMENT RESULT (LOCAL, REMOTE)
	
# USE CASE : GET OFFLINE VERSION
	- DOWNLOAD ARCHIVE + JSON in specific path
	- RUN APP CLIENT UPDATER
	
# USE CASE : DEGRADED
	- NO CONNECTION
	- LOOSE CONEECTION
	- RESOURCES UNAVAILABLE

# ----------------------------------------
# TESTS CASE
# ----------------------------------------

# Test reading app config.json file				UpdaterConfigTest	OK

# Test reading app version.json file			AppVersionsTest		OK

# Test updater mode
	- offline installation 
		(file already downloaded, server connection off)			NOK
	- online 
		(connection with server ok)									NOK		

# Test checking local app version structure		AppStructureTest	OK
	- FOLDER exist								
	- FILE exist
	- checksum ok
	
# Test requirements
	- third-party applications
		- exist ? (ex. : database mySql)
		- process is running ? (ex. : database mySql connection started)


# Test find local versions
	- identify version
		- filename
		- checksum
		- contents information
		
# Test find remote versions						NOK
	- getLatestVersion							NOK
		- method version enabled				NOK
		- method upper version					NOK
		- from os name & version				NOK
		- from os name only						NOK
		- all os version available				NOK
		- context os not available				NOK
		- context : os version unavail.			NOK

# Test download glis file
		- one from os name & version			NOK
		- one from os name only					NOK
		- all os version available				NOK

# Test unzip glis archive


# Test execute main file from archive installed

# Test record result of update processus
	- in local file
	- in remote file
	- in remote database
	- stored informations
		- os name and version
		- installation
			- app version
			- path
			- date

# Test no connection, put to offline mode		NOK

# Test internationalization						NOK

# Test logger									NOK
	
# ----------------------------------------
# TODO
# ----------------------------------------
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

	