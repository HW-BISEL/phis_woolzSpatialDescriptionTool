/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
        String outputText = "&sel=0";
        try (PrintWriter out = response.getWriter()) {
            String description = request.getParameter("description");

            ArrayList<String> disconnectedList = new ArrayList<>();
            ArrayList<String> enclosesList = new ArrayList<>();

            StringTokenizer st = new StringTokenizer(description, "*");
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                StringTokenizer st2 = new StringTokenizer(temp, ":");
                String label = st2.nextToken();
                if (label.equalsIgnoreCase("disjoint")) {
                    disconnectedList.add(st2.nextToken());
                }
                if (label.equalsIgnoreCase("encloses")) {
                    enclosesList.add(st2.nextToken());
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

            out.print(outputText.trim());
        }
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
