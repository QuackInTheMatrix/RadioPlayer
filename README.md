# RadioPlayer
## About
A basic radio player application that utilizes JavaFX, FFmpeg, radiobrowser4j and h2 database to fetch, store and play online radio streams.
## Setup
The application requires minimal setup to start working<br/>
The only thing that needs to be manually configured is the h2 database<br/>
To do so download the h2 database and create a new database called 'radioplayer' with user: 'username' and password 'password'
(If there is a need to change the database name, username and password it can be changed in the dat/database.properties file)<br/>
After the database is created import the sql dump file from dat/dump.sql<br/>
And that's it, the application is ready to be used to listen to your favorite radio stations
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
* Threaded fetching of current song and sychronized access to the title variable
