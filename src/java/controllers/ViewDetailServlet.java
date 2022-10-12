/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ArticleDAO;
import daos.CommentDAO;
import daos.HashtagDAO;
import daos.MemberDAO;
import daos.NotificationDAO;
import daos.ReportDAO;
import dtos.Article;
import dtos.Hashtag;
import dtos.Member;
import dtos.Notification;
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
@WebServlet(name = "ViewDetailSevlet", urlPatterns = {"/ViewDetailServlet"})
public class ViewDetailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
           // if (session != null) {

                if (session.getAttribute("userdata") != null) {
                    Member member = (Member) session.getAttribute("userdata");
                    NotificationDAO ndao = new NotificationDAO();
                    if (member.getMemberRole() == 1) {
                        List<Notification> listNoti = ndao.getAllNotificationsByMember(member.getMemberID());
                        request.setAttribute("listNoti", listNoti);
                    }
                    String aId = request.getParameter("aId");
                    String memRpId = request.getParameter("memReportID");
                    String notiID = request.getParameter("notiID");
                    if (notiID != null) {
                        ndao.changeNotiStatus(Integer.parseInt(notiID));
                    }
                    ArticleDAO aDao = new ArticleDAO();
                    Article a = aDao.find(Integer.parseInt(aId));
                    request.setAttribute("postDetail", a);
                    CommentDAO cdao = new CommentDAO();
                    request.setAttribute("listCmt", cdao.getAllCommentsByArticles(a));
                    HashtagDAO hDao = new HashtagDAO();
                    List<Hashtag> listAH = hDao.getAllHashtagByArticleID(aId);
                    request.setAttribute("listAH", listAH);
                    MemberDAO mdao = new MemberDAO();
                    ReportDAO rdao = new ReportDAO();
                    Report r = rdao.checkReport(aId, member.getMemberID());
                    if (memRpId != null) {
                        Report rView = rdao.checkReport(aId, memRpId);
                        request.setAttribute("viewReport", rView);
                    }
                    String canConfirm = request.getParameter("canConfirm");
                    if (canConfirm != null) {
                        if (canConfirm.equals("yes")) {
                            request.setAttribute("confirmReport", "no");
                        }
                    }
                    request.setAttribute("checkReport", r);

                    request.getRequestDispatcher("detail.jsp").forward(request, response);
               // }
            } else {
                String aId = request.getParameter("aId");
                ArticleDAO aDao = new ArticleDAO();
                Article a = aDao.find(Integer.parseInt(aId));
                request.setAttribute("postDetail", a);
                CommentDAO cdao = new CommentDAO();
                request.setAttribute("listCmt", cdao.getAllCommentsByArticles(a));
                HashtagDAO hDao = new HashtagDAO();
                List<Hashtag> listAH = hDao.getAllHashtagByArticleID(aId);
                request.setAttribute("listAH", listAH);
                request.getRequestDispatcher("detail.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log("ERROR at ViewDetailServlet: " + e.getMessage());
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
