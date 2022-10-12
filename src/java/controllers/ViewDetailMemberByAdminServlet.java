/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ArticleDAO;
import daos.MemberDAO;
import daos.ReportDAO;
import dtos.Article;
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
@WebServlet(name = "ViewDetailMemberByAdminServlet", urlPatterns = {"/ViewDetailMemberByAdminServlet"})
public class ViewDetailMemberByAdminServlet extends HttpServlet {

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
                // Xem chi tiết Member bằng quyền ADMIN
                // Xem ttin khái quát của member 
                // Xem tổng số bài viết mà member này đã đăng
                // Xem số bài Lost member này đã đăng
                // Xem số bài Found member này đã đăng
                // Xem tất cả các bài tố cáo đối với member này
                // Xem các bài viết bị cảnh cáo bởi ADMIN đối với member này
                // Tổng số bài đã Post
                // Tổng số report
                Member memberLogin = (Member) session.getAttribute("userdata");
                if (memberLogin.getMemberRole() == 0) {
                    if (memberLogin.getStatus() == 1) {
                        String memberID = request.getParameter("uId");
                        MemberDAO mdao = new MemberDAO();
                        Member member = mdao.find(memberID);
                        request.setAttribute("MemberInfo", member);
                        ReportDAO rdao = new ReportDAO();
                        // Lấy ra các report của member này
                        List<Report> listReports = rdao.getAllReportsOfAMember(memberID);
                        request.setAttribute("listReports", listReports);

                        ArticleDAO adao = new ArticleDAO();
                        int numberPost = adao.countAllPostsOfAMember(memberID);
                        int numberLost = adao.countAllPostsOfAMemberByType(memberID, 1);
                        int numberFound = adao.countAllPostsOfAMemberByType(memberID, 2);
                        List<Article> listPostWarning = adao.getAllPostWarningOfMember(memberID);
                        request.setAttribute("numberPost", numberPost);
                        request.setAttribute("numberLost", numberLost);
                        request.setAttribute("numberFound", numberFound);
                        request.setAttribute("listPostWarning", listPostWarning);
                    } else {
                        request.setAttribute("errormessage", "Your account has been banned!");
                        request.getRequestDispatcher("AdminListServlet").forward(request, response);
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
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("detailMember.jsp").forward(request, response);
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
