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
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProcessDescription;

/**
 *
 * @author kcm
 */
@WebServlet(name = "ListTissues", urlPatterns = {"/ListTissues"})
public class ListTissues extends HttpServlet {

    private ArrayList<String> completelyUnCoveredTissues = new ArrayList<String>();
    private ArrayList<String> completelyCoveredTissues = new ArrayList<String>();
    private HashMap<Double, String> jaqResults = new HashMap<Double, String>();
    private TreeSet<Double> sortJaqResults = new TreeSet<Double>();

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
        ProcessDescription pd = new ProcessDescription();
        jaqResults.clear();
        sortJaqResults.clear();
        completelyCoveredTissues.clear();
        completelyUnCoveredTissues.clear();
        String finalDescription = "";

        try (PrintWriter out = response.getWriter()) {
            // example &sel=0&sel=domain(threshold(45,69,ge)),255,0,0,128            
            String description = pd.process(request.getParameter("description")).trim();                     
            description = description.replace("&sel=0", "");            
            description = description.replace(",128,128,128", "");
            description = description.replace(",255,0,0", "");
            description = description.trim();                        

            String[] splitDescription = description.split("&sel=");     
            switch (splitDescription.length) {
                case 2:
                    finalDescription = splitDescription[1];
                    break;
                case 3:
                    finalDescription = "union("+splitDescription[1]+","+splitDescription[2]+")";
                    break;
                case 4:
                    finalDescription = "union("+splitDescription[1]+",union("+splitDescription[2]+","+splitDescription[3]+"))";
                    break;
                case 5:
                    finalDescription = "union("+splitDescription[1]+",union("+splitDescription[2]+",union("+splitDescription[3]+","+splitDescription[4]+")))";
                    break;
                default:
                    out.println("too many parameters in description");
                    break;
            }
                                        

            // for each domain
            // get the size of the domain
            // get the intersection of domain and ROI
            if (!finalDescription.equalsIgnoreCase("")) {
                for (int i = 1; i < 45; i++) {
                    Double domainVolume = new Double(getVolume(i));
                    String tissName = pd.convertNumberToTissue(i);                    
                    if (domainVolume != 0) {
                        String queryUrl = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=intersect(" + i + "," + finalDescription + ")&obj=wlz-volume";                        
                        Double intersection = new Double(talk(queryUrl));
                        Double dividedBy = domainVolume - intersection;
                        Double jaquardIndex = intersection / dividedBy;
                        if (intersection.equals(0.0)) {
                            // no overlap... so none of domain in ROI
                            completelyUnCoveredTissues.add(tissName);
                        } else if (jaquardIndex.isInfinite()) {
                            // intersection is complete... so all of domain in ROI`
                            completelyCoveredTissues.add(tissName);
                        } else {                                                        
                            jaqResults.put(jaquardIndex, tissName);
                            sortJaqResults.add(jaquardIndex);
                        }
                    }                    
                }
            }

            // sort output
            String tempS = "";
            Iterator<String> it1 = completelyUnCoveredTissues.iterator();
            while (it1.hasNext()) {
                tempS += it1.next() + ",";
            }
            tempS += "*";
            Iterator<Double> it = sortJaqResults.iterator();
            while (it.hasNext()) {
                tempS += jaqResults.get(it.next()) + ",";
            }
            tempS += "*";
            it1 = completelyCoveredTissues.iterator();
            while (it1.hasNext()) {
                tempS += it1.next() + ",";
            }
            if (tempS.length() != 0) {
                tempS = tempS.substring(0, tempS.length() - 1);
            }
            out.println(tempS);
            out.flush();
        }
    }

    private int talk(String queryURL) {
        BufferedReader in = null;
        ProcessDescription pd = new ProcessDescription();
        String fileName = pd.getRandomFileName();
        try {
            // connect to SOLR and run query
            URL url = new URL(queryURL);

            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.flush();
            fos.close();
            rbc.close();

        } catch (IOException e) {
            return -1;
        }

        try {
            File temp = new File(fileName);
            BufferedReader input = new BufferedReader(new FileReader(temp));
            input.readLine(); //blank
            int tiss = Integer.parseInt(input.readLine().trim().replace("Wlz-volume:", ""));
            input.close();
            temp.delete();
            return tiss;

        } catch (IOException ioe) {

        }
        return -1;
    }

    public int getVolume(int num) {
        if (num == 17) {
            return 1219472;
        }
        if (num == 28) {
            return 56729;
        }
        if (num == 35) {
            return 840612;
        }
        if (num == 12) {
            return 165060;
        }
        if (num == 16) {
            return 88757;
        }
        if (num == 14) {
            return 365770;
        }
        if (num == 10) {
            return 745515;
        }
        if (num == 21) {
            return 1768228;
        }
        if (num == 38) {
            return 1377217;
        }
        if (num == 42) {
            return 618910;
        }
        if (num == 8) {
            return 101926;
        }
        if (num == 34) {
            return 13624;
        }
        if (num == 25) {
            return 119066;
        }
        if (num == 36) {
            return 1002166;
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
