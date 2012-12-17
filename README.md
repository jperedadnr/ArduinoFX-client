ArduinoFX-client
================

JavaFX based client for ArduinoFX project

Please, read full description of hardware setup here:

http://jperedadnr.blogspot.com.es/2012/12/arduinofx-javafx-gui-for-home.html

and here for description of installation of embedded server:

http://jperedadnr.blogspot.com.es/2012/12/arduinofx-javafx-gui-for-home_17.html

The JavaFX application connects to a server (See https://github.com/jperedadnr/ArduinoFX-server) to grab via RESTful
web services the values of the measures stored in an embedded database.

Main tasks are:

    -Connect to the server to get last measures and show them in a LED Matrix Panel control.
    -Connect to the server to get a list of measures between two dates, showing them in a chart. 
    -Show the status of the connection anytime.

Custom controls from http://jfxtras.org are used.

Comments are really welcome.

José Pereda - http://jperedadnr.blogspot.com.es