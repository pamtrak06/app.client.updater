Tests

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
	