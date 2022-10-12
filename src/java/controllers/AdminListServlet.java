/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ArticleDAO;
import daos.ReportDAO;
import dtos.Member;
import dtos.Report;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "AdminListServlet", urlPatterns = {"/AdminListServlet"})
public class AdminListServlet extends HttpServlet {

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
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            } else if (session.getAttribute("userdata") != null) {
                //Trang Dashboard:
                // Thống kê tổng số bài report trong ngày hôm nay
                // Thống kê tổng số bài LOST trong ngày hôm nay
                // Thống kê tổng số bài FOUND trong ngày hôm nay
                // Thống kê tổng số bài LOST trong system
                // Thống kê tổng số bài FOUND trong system
                Member memberLogin = (Member) session.getAttribute("userdata");
                if (memberLogin.getMemberRole() == 0) {
                    if (memberLogin.getStatus() == 1) {
                        ReportDAO rdao = new ReportDAO();                       
                        List<Report> listReports = rdao.getAllReports();
                        request.setAttribute("listRp", listReports);
                        int numberReport = rdao.countAllReports();
                        request.setAttribute("numberReport", numberReport);
                        ArticleDAO adao = new ArticleDAO();
                        int numberLostToday = adao.countAllPostsToday(1);
                        int numberFoundToday = adao.countAllPostsToday(2);
                        int numberLost = adao.countAllPosts(1);
                        int numberFound = adao.countAllPosts(2);
                        request.setAttribute("numberLostToday", numberLostToday);
                        request.setAttribute("numberFoundToday", numberFoundToday);
                        request.setAttribute("numberLost", numberLost);
                        request.setAttribute("numberFound", numberFound);                                               
                        request.getRequestDispatcher("admin.jsp").forward(request, response);
                    } else {
                        request.setAttribute("errormessage", "Your account has been banned!");
                        request.getRequestDispatcher("paging").forward(request, response);
                    }
                } else {
                    request.setAttribute("errormessage", "Incorrect Role! Must be ADMIN");
                    request.getRequestDispatcher("paging").forward(request, response);
                }
            } else {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();}
//        } finally {
//            request.getRequestDispatcher("admin.jsp").forward(request, response);
//        }
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
