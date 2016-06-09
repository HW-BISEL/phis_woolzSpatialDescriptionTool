/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kcm
 */
@WebServlet(name = "ProcessSD", urlPatterns = {"/ProcessSD"})
public class ProcessSD extends HttpServlet {

    Integer ventral = 0;
    Integer dorsal = 0;
    Integer cranial = 0;
    Integer caudal = 0;
    Integer right = 0;
    Integer left = 0;

    
    String start_pos;
    String stop_pos;

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String outputText = "&sel=0";
        try (PrintWriter out = response.getWriter()) {
            String description = request.getParameter("description");
            //out.println(description);

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
                outputText += "&sel=" + convertTissueToNumber(disconnectedList.get(0)) + ",255,0,0";
            } else if (disconnectedList.size() > 0) {
                outputText += "&sel=union(";
                for (String temp : disconnectedList) {
                    outputText += convertTissueToNumber(temp) + ",";
                }
                outputText = outputText.substring(0, outputText.length() - 1);
                outputText += "),255,0,0";
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
                    String url = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&SEL=transfer(" + convertTissueToNumber(name) + ",45)&OBJ=Wlz-Grey-Stats";                    
                    boolean result = talk(url, out);
                    if (!result) {
                        break;
                    }
                    Integer temp = new Integer(stop_pos);
                    if (temp < dorsal) {
                        dorsal = temp;
                    }
                    // only need min for dorsal
                    outputText += "&sel=domain(threshold(45,"+dorsal+",ge)),255,0,0,128";
                }                
            }
            if (ventralList.size() > 0) {                
                for (String name : ventralList) {
                    String url = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&SEL=transfer(" + convertTissueToNumber(name) + ",45)&OBJ=Wlz-Grey-Stats";                    
                    boolean result = talk(url, out);
                    if (!result) {
                        break;
                    }
                    Integer temp = new Integer(start_pos);
                    if (temp > ventral) {
                        ventral = temp;
                    }
                    // only need max for ventral
                    outputText += "&sel=domain(threshold(45,"+ventral+",le)),255,0,0,128";
                }                
            }
            
            if (cranialList.size() > 0) {                
                for (String name : cranialList) {
                    String url = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&SEL=transfer(" + convertTissueToNumber(name) + ",46)&OBJ=Wlz-Grey-Stats";                    
                    boolean result = talk(url, out);
                    if (!result) {
                        break;
                    }
                    Integer temp = new Integer(start_pos);
                    if (temp > cranial) {
                        cranial = temp;
                    }
                    // only need max for ventral
                }
                outputText += "&sel=domain(threshold(46,"+cranial+",le)),255,0,0,128";
            }   
                        
            if (caudalList.size() > 0) {                    
                for (String name : caudalList) {
                    String url = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&SEL=transfer(" + convertTissueToNumber(name) + ",46)&OBJ=Wlz-Grey-Stats";                    
                    boolean result = talk(url, out);                    
                    if (!result) {                        
                        break;
                    }                    
                    Integer temp = new Integer(stop_pos);                    
                    if (temp < caudal) {
                        caudal = temp;
                    }                    
                }
                outputText += "&sel=domain(threshold(46,"+caudal+",ge)),255,0,0,128";
            }            

            if (leftList.size() > 0) {                
                for (String name : leftList) {
                    String url = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&SEL=transfer(" + convertTissueToNumber(name) + ",47)&OBJ=Wlz-Grey-Stats";                    
                    boolean result = talk(url, out);
                    if (!result) {
                        break;
                    }
                    Integer temp = new Integer(stop_pos);
                    if (temp < left) {
                        left = temp;
                    }                   
                }
                outputText += "&sel=domain(threshold(47,"+left+",ge)),255,0,0,128";
            }            
            if (rightList.size() > 0) {                
                for (String name : rightList) {
                    String url = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes.wlz&SEL=transfer(" + convertTissueToNumber(name) + ",47)&OBJ=Wlz-Grey-Stats";                    
                    boolean result = talk(url, out);
                    if (!result) {
                        break;
                    }
                    Integer temp = new Integer(start_pos);
                    if (temp > right) {
                        right = temp;
                    }                   
                }
                outputText += "&sel=domain(threshold(47,"+right+",le)),255,0,0,128";
            }             

            out.print(outputText.trim());
        }
    }

    private boolean talk(String queryURL, PrintWriter out) {
        BufferedReader in = null;
        try {
            // connect to SOLR and run query
            URL url = new URL(queryURL);

            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("wlziip.txt");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.flush();
            fos.close();
            rbc.close();

        } catch (IOException e) {
            return false;
        }

        try {
            File temp = new File("wlziip.txt");
            BufferedReader input = new BufferedReader(new FileReader(temp));
            input.readLine(); //blank
            StringTokenizer st = new StringTokenizer(input.readLine());
            st.nextToken();
            st.nextToken();
            start_pos = st.nextToken();
            stop_pos = st.nextToken();
            input.close();
            temp.delete();
            return true;
        } catch (IOException ioe) {

        }

        return true;
    }

    private int convertTissueToNumber(String tissue) {
        if (tissue.equals("diencephalon")) {
            return 15;
        }
        if (tissue.equals("eye")) {
            return 8;
        }
        if (tissue.equals("heart")) {
            return 19;
        }
        if (tissue.equals("hyoid arch")) {
            return 5;
        }
        if (tissue.equals("liver")) {
            return 23;
        }
        if (tissue.equals("mandibular arch")) {
            return 35;
        }
        if (tissue.equals("mesencephalon")) {
            return 42;
        }
        if (tissue.equals("metencephalon")) {
            return 20;
        }
        if (tissue.equals("neural tube")) {
            return 39;
        }
        if (tissue.equals("olfactory placode")) {
            return 31;
        }
        if (tissue.equals("otic pit")) {
            return 2;
        }
        if (tissue.equals("rathke pouch")) {
            return 24;
        }
        if (tissue.equals("tail bud")) {
            return 32;
        }
        if (tissue.equals("telencephalon")) {
            return 11;
        }
        return 0;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
