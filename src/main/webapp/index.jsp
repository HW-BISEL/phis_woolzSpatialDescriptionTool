<%-- 
    Document   : index
    Created on : 07-Mar-2016, 15:04:47
    Author     : kcm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Spatial demo based on IIP</title>   
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">        

        <!-- Optional theme -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

        <!-- Latest compiled and minified JavaScript -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>          
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>                        

    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <div id="iip_image"><img src="https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png"/></div>
                </div>
                <div class="col-md-3">
                    <br />
                    <span><b>Distance</b></span>
                    <div class="input-group">                        
                        <input id="dstSlider" type="range" class="form-control" max="200" min="0" step="10" value="150" onchange="updateDST()" />
                        <span id="dstValue" class="input-group-addon">150</span>
                    </div> 

                    <br />

                    <div>
                        <span>Pitch, yaw &amp; roll: <img height="100" width="100" src="https://upload.wikimedia.org/wikipedia/commons/0/04/Flight_dynamics_with_text_ortho.svg" /></span>
                    </div>

                    <br />

                    <span><b>Yaw</b></span>
                    <div class="input-group">
                        <input id="yawSlider" type="range" class="form-control" max="200" min="0" step="10" value="90" onchange="updateYAW()" />
                        <span id="yawValue" class="input-group-addon">90</span>
                    </div>                    

                    <br />

                    <span><b>Pitch</b></span>
                    <div class="input-group">                        
                        <input id="pitSlider" type="range" class="form-control" max="200" min="0" step="10" value="90" onchange="updatePIT()" />
                        <span id="pitValue" class="input-group-addon" >90</span>
                    </div>    

                    <br /> 

                    <span><b>Slowly rotate mouse</b></span>
                    <div class="input-group">
                        <input type="button" class="btn btn-default" id="rotateButton" value="rotate" onclick="rotatePosition()" disabled="disabled">                    
                    </div>          

                    <br />
                    
                    <span><b>Choose a side</b></span>
                    <div class="input-group">
                        <input type="button" class="btn btn-default" id="leftButton" value="left" onclick="leftPosition()">
                        <input type="button" class="btn btn-default" id="leftButton" value="rear" onclick="rearPosition()">                        
                    </div>
                    
                    <br />
                    
                    <span><b>Reset position</b></span>
                    <div class="input-group">
                        <input type="button" class="btn btn-default" id="resetPosButton" value="reset" onclick="resetPosition()">                    
                    </div>

                    <!--
                    <br />
                    
                    <span><b>Structure to highlight in red:</b></span>
                    <select id="tissueSelect" onchange="updateImage()" class="form-control" default="0">
                        <option value="0" selected="true">none</option>
                        <option value="22">heart</option>
                        <option value="3">liver</option>
                    </select>      
                    -->
                </div>            
            </div>
        </div>

        <br />

        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <span><b>Describe the position of the ROI</b></span>
                </div>            
            </div>
            <div class="row">
                <div class="col-md-2">                    
                    ROI is
                </div>
                <div class="col-md-2">
                    <select id="relnSelect" class="form-control" default="0">
                        <option value="0" selected="true">none</option>
                        <option value="disjoint">disconnected with the</option>                        
                        <option value="encloses">encloses the</option>   
                        <option value="partial">overlaps the</option>
                        <option value="tangential">is a tangential part of the</option>
                        <option value="non-tangential">is a non-tangential part of the</option>
                    </select>                      
                </div>
                <div class="col-md-2">

                </div>
                <div class="col-md-2">
                    <select id="relnTissueSelect" class="form-control" default="0">
                        <option value="0" selected="true">none</option>                        
                        <option value="diencephalon">diencephalon</option>                        
                        <option value="eye">eye</option>
                        <option value="heart">heart</option>
                        <option value="hyoid arch">hyoid arch</option>
                        <option value="liver">liver</option>          
                        <option value="mandibular arch">mandibular arch</option>
                        <option value="mesencephalon">mesencephalon</option>
                        <option value="metencephalon">metencephalon</option>                        
                        <option value="neural tube">neural tube</option>              
                        <option value="olfactory placode">olfactory placode</option>
                        <option value="otic pit">otic pit</option>
                        <option value="rathke pouch">rathke pouch</option>
                        <option value="tail bud">tail bud</option>
                        <option value="telencephalon">telencephalon</option>                        
                    </select>                     
                </div>
                <div class="col-md-2">
                    <input type="button" class="btn btn-default" id="relnButton" value="Add" onclick="addReln()">                    
                </div>
            </div>
        </div>

        <br />

        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <span><b>Describe the position of the ROI</b></span>
                </div>            
            </div>
            <div class="col-md-2">
                <select id="relnTissueSelect2" class="form-control" default="0">
                    <option value="0" selected="true">none</option>                        
                    <option value="diencephalon">diencephalon</option>                        
                    <option value="eye">eye</option>
                    <option value="heart">heart</option>
                    <option value="hyoid arch">hyoid arch</option>
                    <option value="liver">liver</option>          
                    <option value="mandibular arch">mandibular arch</option>
                    <option value="mesencephalon">mesencephalon</option>
                    <option value="metencephalon">metencephalon</option>                        
                    <option value="neural tube">neural tube</option>              
                    <option value="olfactory placode">olfactory placode</option>
                    <option value="otic pit">otic pit</option>
                    <option value="rathke pouch">rathke pouch</option>
                    <option value="tail bud">tail bud</option>
                    <option value="telencephalon">telencephalon</option>                        
                </select>                     
            </div>            
            <div class="row">
                <div class="col-md-2">                    
                    is
                </div>
                <div class="col-md-2">
                    <select id="relnSelect2" class="form-control" default="0">
                        <option value="0" selected="true">none</option>
                        <option value="caudal">caudal</option>
                        <option value="cranial">cranial</option>    
                        <option value="dorsal">dorsal</option>
                        <option value="left">left</option>
                        <option value="right">right</option>
                        <option value="ventral">ventral</option>
                    </select>                      
                </div>
                <div class="col-md-2">
                    to ROI.
                </div>

                <div class="col-md-2">
                    <input type="button" class="btn btn-default" id="reln2Button" value="Add" onclick="addReln2()">                    
                </div>
            </div>
        </div>        

        <br />
        
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <span><b>Spatial Description</b></span>
                    <div id="scratch">

                    </div>
                </div>
            </div>
        </div>


        <br />

        <div class="container">
            <div class="row">                
                <div class="col-md-4">
                    &nbsp;
                </div>
                <div class="col-md-2">
                    <input type="button" class="btn btn-default" id="resetButton" value="reset" onclick="resetAll()">                    
                </div>
                <div class="col-md-2">
                    <input type="button" class="btn btn-default" id="submitButton" value="submit" onclick="updateImage()">                    
                </div>
            </div>

            <br />
            <hr />
            <br />
            
            <div class="row">                
                <div class="col-md-6">
                    <span><b><em>Debug info to be later hidden!</em></b></span>
                    <span><b>Image being displayed:</b> </span><span id="url">https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png</span>
                </div>      
            </div>            
        </div>
        <script>
            var newDST = 150;
            var newPIT = 90;
            var newYAW = 90;
            var newURL = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";
            var counter = 0;
            var relnArray = [];

            function updateDST() {
                newDST = document.getElementById("dstSlider").value;
                document.getElementById("dstValue").innerHTML = newDST;
                updateImage();
            }

            function updateYAW() {
                newYAW = document.getElementById("yawSlider").value;
                document.getElementById("yawValue").innerHTML = newYAW;
                updateImage();
            }

            function updatePIT() {
                newPIT = document.getElementById("pitSlider").value;
                document.getElementById("pitValue").innerHTML = newPIT;
                updateImage();
            }

            function rotatePosition() {
                var oldYaw = document.getElementById("yawSlider").value;
                newYaw = 0;
                updateImage();
                for (var index2 = 0; index2 < 180; index2++) {
                    newYaw = index2;
                    //updateImage();
                    var t = setTimeout(updateImage, 500);
                }
                newYaw = oldYaw;
                updateImage();
            }


            function leftPosition() {
                document.getElementById("dstSlider").value = 200;
                document.getElementById("yawSlider").value = 70;
                document.getElementById("pitSlider").value = 90;

                document.getElementById("dstValue").innerHTML = "200";
                document.getElementById("yawValue").innerHTML = "70";
                document.getElementById("pitValue").innerHTML = "90";

                newDST = 200;
                newPIT = 90;
                newYAW = 70;
                
                updateImage();
            }
            
            
            function rearPosition() {
                document.getElementById("dstSlider").value = 30;
                document.getElementById("yawSlider").value = 140;
                document.getElementById("pitSlider").value = 90;

                document.getElementById("dstValue").innerHTML = "30";
                document.getElementById("yawValue").innerHTML = "140";
                document.getElementById("pitValue").innerHTML = "90";

                newDST = 30;
                newPIT = 90;
                newYAW = 140;
                
                updateImage();
            }            


            function updateImage() {
                var description = "";
                var url = "";

                if (relnArray.length > 0) {
                    for (var index = 0; index < relnArray.length; index++) {
                        description += relnArray[index].reln + ":" + relnArray[index].tissue + "*";
                    }
                    //url = "https://localhost:8080/wlzDemoTool/ProcessSD?description=" + description;
                    url = "https://lxbisel.macs.hw.ac.uk:8080/wlzDemoTool/ProcessSD?description=" + description;
                    //alert("url: "+url);
                    var getURL = $.ajax({method: "GET", url: url});

                    getURL.done(function (response) {
                        newURL = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=" + newPIT + "&YAW=" + newYAW + "&DST=" + newDST + "&WLZ=/data0/local/nginx/html/withAxes.wlz" + response + "&CVT=png";
                        //alert(newURL);
                        document.getElementById("url").innerHTML = newURL;
                        document.getElementById("iip_image").innerHTML = "<img src=\"" + newURL + "\"/>";
                    });

                    getURL.fail(function (jqXHR, textStatus) {
                        console.log(textStatus);
                    });
                } else {
                    newURL = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=" + newPIT + "&YAW=" + newYAW + "&DST=" + newDST + "&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";
                    document.getElementById("url").innerHTML = newURL;
                    document.getElementById("iip_image").innerHTML = "<img src=\"" + newURL + "\"/>";
                }
            }


            // reset orientation of image
            function resetPosition() {
                document.getElementById("dstSlider").value = 150;
                document.getElementById("yawSlider").value = 90;
                document.getElementById("pitSlider").value = 90;

                document.getElementById("dstValue").innerHTML = "150";
                document.getElementById("yawValue").innerHTML = "90";
                document.getElementById("pitValue").innerHTML = "90";

                newDST = 150;
                newPIT = 90;
                newYAW = 90;

                updateImage();
            }


            function resetAll() {
                resetPosition();

                counter = 0;
                relnArray = [];
                newURL = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";

                document.getElementById("relnSelect").value = "0";
                document.getElementById("relnTissueSelect").value = "0";
                document.getElementById("relnSelect2").value = "0";
                document.getElementById("relnTissueSelect2").value = "0";                
                document.getElementById("scratch").innerHTML = "";

                document.getElementById("iip_image").innerHTML = "<img src=\"https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png\"/>";
                document.getElementById("url").innerHTML = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";
            }


            function addReln() {
                var relationship = document.getElementById("relnSelect").value;
                var relnTissue = document.getElementById("relnTissueSelect").value;
                if (relationship === "0") {
                    alert("Please enter a relationship");
                } else if (relnTissue === "0") {
                    alert("Please enter a tissue");
                } else {
                    var relnDescription = "";
                    if (relationship === "partial") {
                        relnDescription = "ROI partially overlaps " + relnTissue;
                    } else if (relationship === "encloses") {
                        relnDescription = "ROI encloses " + relnTissue;
                    } else if (relationship === "disjoint") {
                        relnDescription = "ROI is disjoint from " + relnTissue;
                    } else if (relationship === "tangential") {
                        relnDescription = "ROI is a tangential part of " + relnTissue;
                    } else if (relationship === "non-tangential") {
                        relnDescription = "ROI is a non-tangential part of " + relnTissue;
                    }
                    var relnObject = {id: counter++, description: relnDescription, tissue: relnTissue, reln: relationship};
                    relnArray.push(relnObject);
                    document.getElementById("relnSelect").value = "0";
                    document.getElementById("relnTissueSelect").value = "0";
                    createTable();
                }
            }
            
           function addReln2() {
                var relationship = document.getElementById("relnSelect2").value;
                var relnTissue = document.getElementById("relnTissueSelect2").value;
                if (relationship === "0") {
                    alert("Please enter a relationship");
                } else if (relnTissue === "0") {
                    alert("Please enter a tissue");
                } else {
                    var relnDescription = "";
                    if (relationship === "cranial") {
                        relnDescription = relnTissue + " is cranial to ROI";
                    } else if (relationship === "caudal") {
                        relnDescription = relnTissue + " is caudal to ROI";
                    } else if(relationship === "dorsal") {
                        relnDescription = relnTissue + " is dorsal to ROI";
                    } else if(relationship === "ventral") {
                        relnDescription = relnTissue + " is ventral to ROI";
                    } else if(relationship === "left") {
                        relnDescription = relnTissue + " is left of ROI";
                    } else if(relationship === "right") {
                        relnDescription = relnTissue + " is right of ROI";
                    }
                    var relnObject = {id: counter++, description: relnDescription, tissue: relnTissue, reln: relationship};
                    relnArray.push(relnObject);
                    document.getElementById("relnSelect").value = "0";
                    document.getElementById("relnTissueSelect").value = "0";
                    createTable();
                }
            }            

            function deleteReln(id) {
                for (var index3 = 0; index3 < relnArray.length; index3++) {
                    if (relnArray[index3].id === id) {
                        relnArray.splice(index3, 1);
                    }
                }
                createTable();
            }

            function createTable() {
                var tableCode = "<table class=\"table table-striped\"><thead><tr><td>Description</td><td>Delete?</td></tr></thead>";

                for (var index4 = 0; index4 < relnArray.length; index4++) {
                    tableCode += "<tr><td>" + relnArray[index4].description + "</td><td><input type=\"button\" class=\"btn btn-default\" value=\"delete\" onclick=\"deleteReln(" + relnArray[index4].id + ")\"></td></tr>";
                }

                tableCode += "</table>"

                document.getElementById("scratch").innerHTML = tableCode;
            }
        </script>         
    </body>
</html>
