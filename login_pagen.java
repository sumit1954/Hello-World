/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.journaldev.mongodb.servlets;

import com.journaldev.mongodb.dao.Registration_DAO;
import com.journaldev.mongodb.model.Registration;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
//        processRequest(request, response);

        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("userName");
        RequestDispatcher rd;
        if (user != null && !user.equals("")) {
            request.setAttribute("message", "Already LoggedIn");
            rd = getServletContext().getRequestDispatcher(
                    "/Login_pagen.jsp");
            rd.forward(request, response);
        } else {
            String user_id = request.getParameter("user_id");
            String password = request.getParameter("password");

            if ((user_id == null || user_id.equals(""))
                    || (password == null || password.equals(""))) {
                request.setAttribute("error", "Mandatory Parameters Missing");
                request.setAttribute("message", "Mandatory Parameters Missing");
                rd = getServletContext().getRequestDispatcher(
                        "/Failure_Registration.jsp");
                rd.forward(request, response);
            } else {
                Registration registration = new Registration();
                registration.setUser_id(user_id);
                registration.setPassword(password);

                MongoClient mongo = (MongoClient) request.getServletContext()
                        .getAttribute("MONGO_CLIENT");

                Registration_DAO registrationDAO = new Registration_DAO(mongo);
                if (registrationDAO.checkLogin(registration.getUser_id(), registration.getPassword())) {
                    System.out.println("Login Successfull");
                    request.setAttribute("message", "Login Successfull");
                    request.setAttribute("showLogOutB", "true");

                    session = request.getSession();
                    session.setAttribute("userName", user_id);

//        out.print("<a href='servlet2'>visit</a>");  
//        out.close();  
                } else {
                    System.out.println("Login Failed");
                    request.setAttribute("message", "Login Failed");
                    request.setAttribute("showLogOutB", "false");

                }

                rd = getServletContext().getRequestDispatcher(
                        "/ResponsePage.jsp");
                rd.forward(request, response);
//            response.sendRedirect("ResponsePage.jsp");
            }
        }

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

//        String user_id = request.getParameter("user_id");
//        String password = request.getParameter("password");
//
//        if ((user_id == null || user_id.equals(""))
//                || (password == null || password.equals(""))) {
//            request.setAttribute("error", "Mandatory Parameters Missing");
//            request.setAttribute("message", "Mandatory Parameters Missing");
//            RequestDispatcher rd = getServletContext().getRequestDispatcher(
//                    "/Failure_Registration.jsp");
//            rd.forward(request, response);
//        } else {
//            Registration registration = new Registration();
//            registration.setUser_id(user_id);
//            registration.setPassword(password);
//
//            MongoClient mongo = (MongoClient) request.getServletContext()
//                    .getAttribute("MONGO_CLIENT");
//
//            Registration_DAO registrationDAO = new Registration_DAO(mongo);
//            if(registrationDAO.checkLogin(registration.getUser_id(),registration.getPassword())){
//                System.out.println("Login Successfull");
//                request.setAttribute("message", "Login Successfull");
//
//            }else{
//                System.out.println("Login Failed");
//                request.setAttribute("message", "Login Failed");
//
//            }
//
//            RequestDispatcher rd = getServletContext().getRequestDispatcher(
//                    "/ResponsePage.jsp");
//            rd.forward(request, response);
//            response.
//            response.sendRedirect("/ResponsePage.jsp");
//        }
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
