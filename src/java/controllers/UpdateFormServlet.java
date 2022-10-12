/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ArticleDAO;
import daos.ArticleTypeDAO;
import daos.HashtagDAO;
import daos.ItemTypeDAO;
import dtos.Article;
import dtos.ArticleType;
import dtos.Item;
import dtos.Member;
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
@WebServlet(name = "UpdateFormServlet", urlPatterns = {"/UpdateFormServlet"})
public class UpdateFormServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            } else if (session.getAttribute("userdata") != null) {
                Member member = (Member) session.getAttribute("userdata");
                if (member.getStatus() == 1) {
                    String aId = request.getParameter("aId");
                    ArticleDAO aDao = new ArticleDAO();
                    ItemTypeDAO itDao = new ItemTypeDAO();
                    ArticleTypeDAO atDao = new ArticleTypeDAO();
                    if (aId != null) {
                        Article a = aDao.find(Integer.parseInt(aId));
                        List<ArticleType> listAT = atDao.getAllArticleType();
                        request.setAttribute("ListArticleType", listAT);

                        List<Item> listI = itDao.getAllItems();
                        request.setAttribute("ListItemType", listI);

                        HashtagDAO hDao = new HashtagDAO();
//                    List<Hashtag> listAH = hDao.getAllHashtagByArticleID(aId);
//                    String hashtagList = "";
//                    for (Hashtag hashtag : listAH) {
//                        hashtagList = hashtagList.concat(hashtag.getHashtagName()+" ");
//                    }
//                    request.setAttribute("hashtag", hashtagList);

                        request.setAttribute("isFlag", a.getWarningStatus());
                        request.setAttribute("titlePost", a.getTitle());
                        request.setAttribute("content", a.getArticleContent());
                        request.setAttribute("postURL", a.getImgUrl());
                        request.setAttribute("aStatus", a.getArticleStatus());
                        if (a.getItem() != null) {
                            request.setAttribute("itemId", a.getItem().getItemID());
                        }
                        request.setAttribute("postTypeId", a.getType().getTypeID());
                        request.setAttribute("action", "update");
                        request.setAttribute("idUpdate", aId);
                        request.getRequestDispatcher("form.jsp").forward(request, response);

                    } else {
                        List<ArticleType> listAT = atDao.getAllArticleType();
                        request.setAttribute("ListArticleType", listAT);
                        List<Item> listI = itDao.getAllItems();
                        request.setAttribute("ListItemType", listI);
                        request.setAttribute("idUpdate", request.getAttribute("idUpdate"));
                        request.setAttribute("titlePost", request.getAttribute("titlePost"));
                        request.setAttribute("titleError", request.getAttribute("titleError"));
                        request.setAttribute("contentError", request.getAttribute("contentError"));
                        request.setAttribute("content", request.getAttribute("content"));
                        request.setAttribute("hashtag", request.getAttribute("hashtag"));
                        request.setAttribute("hashtagError", request.getAttribute("hashtagError"));
                        request.setAttribute("aStatus", request.getAttribute("aStatus"));
                        request.setAttribute("postURL", request.getAttribute("postURL"));
                        request.setAttribute("itemId", request.getAttribute("itemId"));
                        request.setAttribute("postTypeId", request.getAttribute("postTypeId"));
                        request.setAttribute("action", "update");
                        request.getRequestDispatcher("form.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errormessage", "Your account has been banned! Cannot use this function!");
                    if(member.getMemberRole()==0){
                    request.getRequestDispatcher("AdminListServlet").forward(request, response);
                    }else if(member.getMemberRole()==1){
                    request.getRequestDispatcher("paging").forward(request, response);   
                    }else{
                    request.getRequestDispatcher("paging").forward(request, response);       
                    }
                }
            } else {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
