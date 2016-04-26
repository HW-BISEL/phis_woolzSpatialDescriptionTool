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
                    <div id="iip_image"><img src="http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png"/></div>
                </div>
                <div class="col-md-3">
                    <br />
                    <span><b>Distance</b></span>
                    <div class="input-group">                        
                        <input id="dstSlider" type="range" class="form-control" max="200" min="0" step="20" value="150" onchange="updateDST()" />
                        <span id="dstValue" class="input-group-addon">150</span>
                    </div> 

                    <br />

                    <div>
                        <img src="http://lxbisel.macs.hw.ac.uk:8080/2016-03-14-pitch_yaw_explanation_small.jpg" />
                    </div>

                    <br />

                    <span><b>Yaw</b></span>
                    <div class="input-group">
                        <input id="yawSlider" type="range" class="form-control" max="200" min="0" step="20" value="90" onchange="updateYAW()" />
                        <span id="yawValue" class="input-group-addon">90</span>
                    </div>                    

                    <br />

                    <span><b>Pitch</b></span>
                    <div class="input-group">                        
                        <input id="pitSlider" type="range" class="form-control" max="200" min="0" step="20" value="90" onchange="updatePIT()" />
                        <span id="pitValue" class="input-group-addon" >90</span>
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
                <div class="col-md-6">
                    <span><b>Image being displayed:</b> </span><span id="url">http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png</span>
                </div>      
            </div>
            <div class="row">                
                <div class="col-md-4">
                    &nbsp;
                </div>
                <div class="col-md-2">
                    <input type="button" class="btn btn-default" id="resetButton" value="reset" onclick="resetImage()">                    
                </div>
                <div class="col-md-2">
                    <input type="button" class="btn btn-default" id="submitButton" value="submit" onclick="updateImage()">                    
                </div>

            </div>
        </div>
        <script>
            var originalURL = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";
            var originalDST = 150;
            var originalPIT = 90;
            var originalYAW = 90;
            // var newTissue = "0";
            var newDST = 150;
            var newPIT = 90;
            var newYAW = 90;
            var newURL = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";
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

            function updateImage() {
                var description = "";
                for (index = 0; index < relnArray.length; index++) {
                    description += relnArray[index].reln + ":" + relnArray[index].tissue + "*";
                }
                alert(description);
                var url = "http://localhost:8080/wlzDemoTool/ProcessSD?description=" + description;

                var getURL = $.ajax({method: "GET", url: url});

                getURL.done(function (response) {
                    alert("response:" + response+"*");
                    newURL = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=" + newPIT + "&YAW=" + newYAW + "&DST=" + newDST + "&WLZ=/data0/local/nginx/html/withAxes.wlz" + response + "&CVT=png";

                    document.getElementById("url").innerHTML = newURL;
                    document.getElementById("iip_image").innerHTML = "<img src=\"" + newURL + "\"/>";

                });

                getURL.fail(function (jqXHR, textStatus) {
                    console.log(textStatus);
                });
            }


            function resetImage() {
                alert("reset clicked!");
                // newDST = 150;
                // newYAW = 90;
                // newPIT = 90;
                // newTissue = "0";
                counter = 0;
                relnArray = [];

                document.getElementById("dstSlider").value = 150;
                document.getElementById("yawSlider").value = 90;
                document.getElementById("pitSlider").value = 90;

                document.getElementById("dstValue").innerHTML = "150";
                document.getElementById("yawValue").innerHTML = "90";
                document.getElementById("pitValue").innerHTML = "90";
                // document.getElementById("tissueSelect").value = "0";

                document.getElementById("relnSelect").value = "0";
                document.getElementById("relnTissueSelect").value = "0";
                document.getElementById("scratch").innerHTML = "";

                document.getElementById("iip_image").innerHTML = "<img src=\"http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png\"/>";
                document.getElementById("url").innerHTML = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&sel=0&CVT=png";
            }


            function addReln() {
                var relationship = document.getElementById("relnSelect").value;
                var relnTissue = document.getElementById("relnTissueSelect").value;
                if (relationship === "0") {
                    alert("Please enter a relationship");
                } else if (relnTissue === "0") {
                    alert("Please enter a tissue");
                } else {
                    var relnDescription = "ROI is " + relationship + " " + relnTissue;
                    var relnObject = {id: counter++, description: relnDescription, tissue: relnTissue, reln: relationship};
                    relnArray.push(relnObject);
                    document.getElementById("relnSelect").value = "0";
                    document.getElementById("relnTissueSelect").value = "0";
                    createTable();
                }
            }

            function deleteReln(id) {
                for (index = 0; index < relnArray.length; index++) {
                    if (relnArray[index].id === id) {
                        relnArray.splice(index, 1);
                    }
                }
                createTable();
            }

            function createTable() {
                var tableCode = "<table class=\"table table-striped\"><thead><tr><td>Description</td><td>Delete?</td></tr></thead>";

                for (index = 0; index < relnArray.length; index++) {
                    tableCode += "<tr><td>" + relnArray[index].description + "</td><td><input type=\"button\" class=\"btn btn-default\" value=\"delete\" onclick=\"deleteReln(" + relnArray[index].id + ")\"></td></tr>";
                }

                tableCode += "</table>"

                document.getElementById("scratch").innerHTML = tableCode;
            }
        </script>         
    </body>
</html>
