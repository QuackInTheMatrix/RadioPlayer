# RadioPlayer
<hr/>

## About
A basic radio player application that utilizes JavaFX, FFmpeg, radiobrowser4j and a h2 database to fetch, store and
play online radio streams.

## Features
* Searching for radio stations by different criteria such as name, country, codec, bitrate and tags
* User roles with different functionalities(ex. Admin management of users/stations)
* Displaying current song name in the title
* Admin user management(search, add, change, delete)
* Admin station management(search, add, change, delete)
* Admin logs overview with all changes made identified by user, action and data changed
* User personal favourite stations(add/remove)
* User management of account information(ex. change username, email, etc.)
* Controllable playback and audio from every page using playback and audio menus
* Threaded fetching of current song and synchronized access to the title variable

## Setup
<b>Note:</b> The docker database container uses linux so make sure you are running it on WSL if you are on Windows.

The recommended way to use RadioPlayer is with a prebuilt jar and a dockerized database.<br/>
This process is described in the [Automatic](#Automatic) section.<br/>
If however you wish to tinker with the application yourself or set up the database manually this can also be done.
The process of getting everything ready is described in the [Build it yourself](#Build-it-yourself) section below.

### Automatic
The process of setting up the application is very simple and can be done in only 3 steps.<br/>
1. Make sure you have java and docker installed.
2. Run the container which contains the preconfigured database:<br>
    `docker run -p 8082:8082 -p 9092:9092 -v h2_data:/root/ -d --name h2_database duckerize/radioplayer_h2db:latest`
3. Download the <a href="https://github.com/QuackInTheMatrix/RadioPlayer/releases">latest  release</a> from the repository and run it by double-clicking on the jar file.

And that's it, you now have a functional RadioPlayer where you can save, listen to and manage your favourite radio stations.<br/>
You can either register or use one of the preconfigured accounts:'user', 'admin' both with password: 'password'.<br/>
Enjoy!<br>

### Build it yourself
The application requires setting up a h2 database before you can start tinkering with it in your favourite IDE.<br/>
Below is a list of steps needed to get everything running:

1. Make sure you have git, docker(optional but recommended), java and an IDE like IntelliJ installed
2. Clone the repository with git:<br>
   `git clone https://github.com/QuackInTheMatrix/RadioPlayer`
3. Set up the h2 database(it is already included in the project)<br/>
   #### Recommended(with docker)
   `docker run -p 8082:8082 -p 9092:9092 -v h2_data:/root/ -d --name h2_database duckerize/radioplayer_h2db:latest`<br/>
    Note: The docker database image can be manually built from the dockerize_db directory if needed.
    
    #### Manual (without docker)
    1. Open a CLI like cmd/bash and navigate to the RadioPlayer/dockerize_db directory:<br/>
        `cd RadioPlayer/dockerize_db`
    2. Start the database(make sure to replace the '*' in the example with the actual version):<br/>
        `java -jar h2-*.jar`
    3. Go to dat/database.properties and change the path to the radiobrowser database:<br/>
       For example if you are on linux, and cloned the repository to the Downloads directory the new path would look 
       something like this: <br/>
       `jdbc:h2:tcp://localhost/~/Downloads/git/RadioPlayer/dockerize_db/radioplayer`
4. The last step is to load the project in your favourite IDE and build it.

And that's it, you can now start tinkering with the project or just enjoy some music. Have fun with it!

## Contributing
Contributions are always welcome and I could always use more hands on this project to: add new features, optimize code, 
fix bugs etc.<br/>
When contributing make sure to describe what you changed/fixed/optimized and title it accordingly.