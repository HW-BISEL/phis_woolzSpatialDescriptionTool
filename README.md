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
     Instructions are on the [Wiki](https://github.com/kcmcleod/phis_woolzSpatialDescriptionTool/wiki).
2. Run mvn package (see https://github.com/kcmcleod/phis_woolzSpatialDescriptionTool/issues/1).
3. Copy the war file to your server


### Available endpoints ###

Assuming the server has the IRI https://lxbisel.macs.hw.ac.uk:8080/ the following endpoints are available:

#### wlzDemoTool ####

1. Server to process spatial description and convert it to WlzIIPServer query:
https://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/ProcessSD?description=cranial:liver*
2. V1 https://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/
3. V2 https://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/v2.jsp

Please note that not every spatial description will change the image. You may need to play around (or just use heart which seems to mostly work).
