/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kcm
 */
public class ProcessDescription {

    private static final Logger LOG = Logger.getLogger(ProcessDescription.class.getName());

    private Integer ventral = 0;
    private Integer dorsal = 0;
    private Integer cranial = 0;
    private Integer caudal = 0;
    private Integer right = 0;
    private Integer left = 0;

    private String start_pos;
    private String stop_pos;

    public String process(String description) {
        String outputText = "&sel=0";
        ArrayList<String> disconnectedList = new ArrayList<>();
        ArrayList<String> enclosesList = new ArrayList<>();
        ArrayList<String> nonTList = new ArrayList<>();
        ArrayList<String> dorsalList = new ArrayList<>();
        ArrayList<String> ventralList = new ArrayList<>();
        ArrayList<String> cranialList = new ArrayList<>();
        ArrayList<String> caudalList = new ArrayList<>();
        ArrayList<String> leftList = new ArrayList<>();
        ArrayList<String> rightList = new ArrayList<>();
        ventral = -198;
        dorsal = 69;
        cranial = 0;
        caudal = 1599;
        right = -123;
        left = 120;

        StringTokenizer st = new StringTokenizer(description, "*");
        //out.println(st.countTokens());
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            //out.println(temp);
            StringTokenizer st2 = new StringTokenizer(temp, ":");
            String label = st2.nextToken();
            String tissue = st2.nextToken();
            //out.println("*" + label + " " + tissue + "*");
            if (label.equalsIgnoreCase("disjoint")) {
                disconnectedList.add(tissue);
            }
            if (label.equalsIgnoreCase("encloses")) {
                enclosesList.add(tissue);
            }
            if (label.equalsIgnoreCase("partial")) {
                // we have no way of knowing what part of the structure is 
                // partially overlapped, so we take the whole structure
                // hence behaviour is the same as encloses
                enclosesList.add(tissue);
            }
            if (label.equalsIgnoreCase("tangential")) {
                enclosesList.add(tissue);
            }
            if (label.equalsIgnoreCase("non-tangential")) {
                nonTList.add(tissue);
            }
            if (label.equalsIgnoreCase("dorsal")) {
                dorsalList.add(tissue);
            }
            if (label.equalsIgnoreCase("ventral")) {
                ventralList.add(tissue);
            }
            if (label.equalsIgnoreCase("cranial")) {
                cranialList.add(tissue);
            }
            if (label.equalsIgnoreCase("caudal")) {
                caudalList.add(tissue);
            }
            if (label.equalsIgnoreCase("left")) {
                leftList.add(tissue);
            }
            if (label.equalsIgnoreCase("right")) {
                rightList.add(tissue);
            }

        }

        // process disconnected list
        /* to remove
            if (disconnectedList.size() > 0) {
                outputText += "&sel=diff(0,";
                if (disconnectedList.size() > 1) {
                    outputText += "union(";
                    for (String temp : disconnectedList) {
                        outputText += convertTissueToNumber(temp) + ",";
                    }
                    outputText = outputText.substring(0, outputText.length() - 1);
                    outputText += "))";
                } else {
                    outputText += convertTissueToNumber(disconnectedList.get(0)) + ")";
                }
            }
         */
        if (disconnectedList.size() == 1) {
            outputText += "&sel=" + convertTissueToNumber(disconnectedList.get(0)) + ",128,128,128";
        } else if (disconnectedList.size() > 0) {
            outputText += "&sel=union(";
            for (String temp : disconnectedList) {
                outputText += convertTissueToNumber(temp) + ",";
            }
            outputText = outputText.substring(0, outputText.length() - 1);
            outputText += "),128,128,128";
        }

        // process encloses list
        if (enclosesList.size() == 1) {
            outputText += "&sel=" + convertTissueToNumber(enclosesList.get(0)) + ",0,255,0";
        } else if (enclosesList.size() > 0) {
            outputText += "&sel=union(";
            for (String temp : enclosesList) {
                outputText += convertTissueToNumber(temp) + ",";
            }
            outputText = outputText.substring(0, outputText.length() - 1);
            outputText += "),0,255,0";
        }

        // process non-tangential list
        if (nonTList.size() == 1) {
            outputText += "&sel=erosion(" + convertTissueToNumber(nonTList.get(0)) + ",2),0,255,0";
        } else if (nonTList.size() > 0) {
            outputText += "&sel=erosion(union(";
            for (String temp : nonTList) {
                outputText += convertTissueToNumber(temp) + ",";
            }
            outputText = outputText.substring(0, outputText.length() - 1);
            outputText += "),2),0,255,0";
        }

        if (dorsalList.size() > 0) {
            for (String name : dorsalList) {
                String url = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&SEL=transfer(intersect(46," + convertTissueToNumber(name) + "),46)&OBJ=Wlz-Grey-Stats";
                boolean result = talk(url);
                if (!result) {
                    break;
                }
                Integer temp = new Integer(stop_pos);
                if (temp < dorsal) {
                    dorsal = temp;
                }
                // only need min for dorsal
                //outputText += "&sel=domain(threshold(45," + dorsal + ",ge)),128,128,128";
                outputText += "&sel=domain(threshold(46," + dorsal + ",ge)),128,128,128";
            }
        }
        if (ventralList.size() > 0) {
            for (String name : ventralList) {
                String url = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&SEL=transfer(intersect(46," + convertTissueToNumber(name) + "),46)&OBJ=Wlz-Grey-Stats";
                boolean result = talk(url);
                if (!result) {
                    break;
                }
                Integer temp = new Integer(start_pos);
                if (temp > ventral) {
                    ventral = temp;
                }
                // only need max for ventral
                //outputText += "&sel=domain(threshold(45," + ventral + ",le)),128,128,128";
                outputText += "&sel=domain(threshold(46," + ventral + ",le)),128,128,128";
            }
        }

        if (cranialList.size() > 0) {
            for (String name : cranialList) {
                String url = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&SEL=transfer(intersect(47," + convertTissueToNumber(name) + "),47)&OBJ=Wlz-Grey-Stats";
                boolean result = talk(url);
                if (!result) {
                    break;
                }
                Integer temp = new Integer(start_pos);
                if (temp > cranial) {
                    cranial = temp;
                }
                // only need max for ventral
            }
            //outputText += "&sel=domain(threshold(46," + cranial + ",le)),128,128,128";
            outputText += "&sel=domain(threshold(47," + cranial + ",le)),128,128,128";
        }

        if (caudalList.size() > 0) {
            for (String name : caudalList) {
                String url = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&SEL=transfer(intersect(47," + convertTissueToNumber(name) + "),47)&OBJ=Wlz-Grey-Stats";
                boolean result = talk(url);
                if (!result) {
                    break;
                }
                Integer temp = new Integer(stop_pos);
                if (temp < caudal) {
                    caudal = temp;
                }
            }
            //outputText += "&sel=domain(threshold(46," + caudal + ",ge)),128,128,128";
            outputText += "&sel=domain(threshold(47," + caudal + ",ge)),128,128,128";
        }

        if (leftList.size() > 0) {
            for (String name : leftList) {
                String url = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&SEL=transfer(intersect(45," + convertTissueToNumber(name) + "),45)&OBJ=Wlz-Grey-Stats";
                boolean result = talk(url);
                if (!result) {
                    break;
                }
                Integer temp = new Integer(stop_pos);
                if (temp < left) {
                    left = temp;
                }
            }
            //outputText += "&sel=domain(threshold(47," + left + ",ge)),128,128,128";
            outputText += "&sel=domain(threshold(45," + left + ",ge)),128,128,128";
        }
        if (rightList.size() > 0) {
            for (String name : rightList) {
                String url = "https://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&SEL=transfer(intersect(45," + convertTissueToNumber(name) + "),45)&OBJ=Wlz-Grey-Stats";
                boolean result = talk(url);
                if (!result) {
                    break;
                }
                Integer temp = new Integer(start_pos);
                if (temp > right) {
                    right = temp;
                }
            }
            //outputText += "&sel=domain(threshold(47," + right + ",le)),128,128,128";
            outputText += "&sel=domain(threshold(45," + right + ",le)),128,128,128";
        }
        return outputText;
    }

    private boolean talk(String queryURL) {
        String line = "";

        try {
            // connect to SOLR and run query
            URL url = new URL(queryURL);

            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            ByteBuffer buf = ByteBuffer.allocate(512);

            while (rbc.read(buf) > 0) {
            	
                buf.flip();

                while (buf.hasRemaining()) {
                    char ch = (char) buf.get();
                    line += ch;
                }

            }
            line = line.trim();
        } catch (IOException e) {
            return false;
        }

        try {
            LOG.log(Level.INFO, "line read: {0}", line);

            StringTokenizer st = new StringTokenizer(line);
            st.nextToken();
            st.nextToken();
            start_pos = st.nextToken();
            stop_pos = st.nextToken();
            

            LOG.log(Level.INFO, "start_pos is: {0}", start_pos);
            LOG.log(Level.INFO, "stop_pos is: {0}", stop_pos);
            
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    
    public int convertTissueToNumber(String tissue) {
        if (tissue.equals("diencephalon")) {
            return 17;
        }
        if (tissue.equals("eye")) {
            return 28;
        }
        if (tissue.equals("heart")) {
            return 35;
        }
        if (tissue.equals("hyoid arch")) {
            return 12;
        }
        if (tissue.equals("liver")) {
            return 16;
        }
        if (tissue.equals("mandibular arch")) {
            return 14;
        }
        if (tissue.equals("mesencephalon")) {
            return 10;
        }
        if (tissue.equals("metencephalon")) {
            return 21;
        }
        if (tissue.equals("neural tube")) {
            return 38;
        }
        if (tissue.equals("olfactory placode")) {
            return 42;
        }
        if (tissue.equals("otic pit")) {
            return 8;
        }
        if (tissue.equals("rathke pouch")) {
            return 34;
        }
        if (tissue.equals("tail bud")) {
            return 25;
        }
        if (tissue.equals("telencephalon")) {
            return 36;
        }
        return 0;
    }

    public String convertNumberToTissue(int num) {
        if (num == 17) {
            return "diencephalon";
        }
        if (num == 28) {
            return "eye";
        }
        if (num == 35) {
            return "heart";
        }
        if (num == 12) {
            return "hyoid arch";
        }
        if (num == 16) {
            return "liver";
        }
        if (num == 14) {
            return "mandibular arch";
        }
        if (num == 10) {
            return "mesencephalon";
        }
        if (num == 21) {
            return "metencephalon";
        }
        if (num == 38) {
            return "neural tube";
        }
        if (num == 42) {
            return "olfactory placode";
        }
        if (num == 8) {
            return "otic pit";
        }
        if (num == 34) {
            return "rathke pouch";
        }
        if (num == 25) {
            return "tail bud";
        }
        if (num == 36) {
            return "telencephalon";
        }

        return null;
    }

    public String getRandomFileName() {
        long ct = System.currentTimeMillis();
        ct = ct - 1420070400; // Substract Jan 1st, 2015
        return ct + "" + UUID.randomUUID().getMostSignificantBits() % 100+".txt";        
    }
}
