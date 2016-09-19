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
    
    private ArrayList<String> tissuesOut = null;

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
        
        tissuesOut = new ArrayList<>();
        tissuesOut.add("diencephalon");
        tissuesOut.add("eye");
        tissuesOut.add("heart");
        tissuesOut.add("hyoid arch");
        tissuesOut.add("liver");
        tissuesOut.add("mandibular arch");
        tissuesOut.add("mesencephalon");
        tissuesOut.add("metencephalon");
        tissuesOut.add("neural tube");
        tissuesOut.add("olfactory placode");
        tissuesOut.add("otic pit");
        tissuesOut.add("rathke pouch");
        tissuesOut.add("tail bud");
        tissuesOut.add("telencephalon");
                
                
        try (PrintWriter out = response.getWriter()) {
            String description = pd.process(request.getParameter("description")).trim();
            // example &sel=0&sel=domain(threshold(45,69,ge)),255,0,0,128            
            description = description.replace("&sel=0", "");
            description = description.replace("&sel=", "");
            description = description.replace(",128,128,128", "");
            description = description.replace(",255,0,0", "");
            description = description.trim();

            //out.println(description);
            String finalDescription = "";

            if (description.startsWith("union")) {
                String temp = description.substring(6, description.indexOf(")"));
                description = description.substring(description.indexOf(")") + 1);

                String[] tempA = temp.split(",");
                for (String tiss : tempA) {
                    tissuesOut.remove(pd.convertNumberToTissue(Integer.parseInt(tiss)));
                }
            } else if(description.startsWith("1") || description.startsWith("2") || description.startsWith("3") || description.startsWith("4") || description.startsWith("5") || description.startsWith("6") || description.startsWith("7") || description.startsWith("8") || description.startsWith("9")) {
                int pos = description.indexOf("d");
                if(pos > 1) {
                    // do nothing as this is handled later!
                    //tissuesIn.add(pd.convertNumberToTissue(Integer.parseInt(description.substring(0, pos))));
                } else {
                    
                    tissuesOut.remove(pd.convertNumberToTissue(Integer.parseInt(description)));
                }
            }

            // count number of domains            
            if (description.contains("domain")) {
                String[] tempP = description.split("domain");
                ArrayList<String> tempP2 = new ArrayList<>();
                for (String temp : tempP) {
                    if (temp.equalsIgnoreCase("")) {
                        continue;  // happens if there is only 1 axis reln, weird side effect of split
                    }
                    if (!temp.startsWith("(")) {
                        tissuesOut.remove(pd.convertNumberToTissue(Integer.parseInt(temp)));
                    } else { // if disjoint
                        tempP2.add(temp);
                    }
                }

                switch (tempP2.size()) {
                    case 1:
                        finalDescription = "domain" + tempP2.get(0);
                        break;
                    case 2:
                        finalDescription = "union(domain" + tempP2.get(0) + ",domain" + tempP2.get(1) + ")";
                        break;
                    case 3:
                        finalDescription = "union(union(domain" + tempP2.get(0) + ",domain" + tempP2.get(1) + "),domain" + tempP2.get(2) + ")";
                        break;
                    case 4:
                        finalDescription = "union(union(union(domain" + tempP2.get(0) + ",domain" + tempP2.get(1) + "),domain" + tempP2.get(2) + "),domain" + tempP2.get(3) + ")";
                        break;
                    default:
                        return;
                }
            }
            
            // finalDescription = description of area rejected.
            
            // actual ROI, not the area the user greyed out
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=diff(0,domain(threshold(46,590,le)))&obj=wlz-volume
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=intersect(2,domain(diff(22,domain(threshold(46,590,le)))))&obj=wlz-volume
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=0&sel=diff(22,domain(threshold(46,590,le))),128,128,128&CVT=png
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=0&sel=intersect(2,domain(diff(22,domain(threshold(46,590,le))))),128,128,128&CVT=png
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=intersect(2,domain(diff(22,domain(threshold(46,590,le)))))&obj=wlz-volume           
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=diff(22,domain(threshold(46,590,le)))&obj=wlz-volume
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=diff(22,domain(threshold(46,590,le))),128,128,128&CVT=png
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=domain(threshold(46,590,le)),128,128,128&CVT=png
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=22&obj=wlz-volume
            // http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=intersect(2,domain(threshold(46,590,le)))&obj=wlz-volume

            // determine if each region in ROI
            if (!finalDescription.equalsIgnoreCase("")) {
                for (int i = 1; i < 45; i++) {
                    String tissue = pd.convertNumberToTissue(i);
                    if (tissue != null) {
                        String queryUrl = "http://lxbisel.macs.hw.ac.uk:8080/wlziip?PIT=90&YAW=90&DST=150&WLZ=/data0/local/nginx/html/withAxes2.wlz&sel=intersect(" + i + "," + finalDescription + ")&obj=wlz-volume";
                        //out.println(queryUrl);
                        int volume = talk(queryUrl);
                        if (volume < 500) {
                            // tissuesOut.add(tissue);
                        } else {
                            tissuesOut.remove(tissue);
                        }
                    }
                }
            }
            String tempS = tissuesOut.toString();
            tempS = tempS.substring(1, tempS.length()-1);
            out.print(tempS);
            
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
