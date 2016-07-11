# README #

Spatial tools based on the woolz library and tools provided by the eMouseAtlas.

Currently only one tool exists:

1. wlzDemoTool - allows the user to describe a spatial region using a controlled language and visualises the result in the mouse embryo (TS17).


### How do I get set up? ###

#### wlzDemoTool ####

1. From the [eMouseAtlas GitHub page](https://github.com/ma-tech/) install:
     * External (jpeg, tiff, nifti, fcgi & log4cpp)
     * Woolz
     * WlzIIPServer
2. Run mvn package
3. Copy the war file to your server


### Available endpoints ###

Assuming the server has the IRI http://lxbisel.macs.hw.ac.uk:8080/ the following endpoints are available:

#### wlzDemoTool ####

1. Server to process spatial description and convert it to WlzIIPServer query:
http://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/ProcessSD?description=cranial:liver*
2. V1 http://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/
3. V2 http://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/v2.jsp

### Who do I talk to? ###

kcm1@hw.ac.uk