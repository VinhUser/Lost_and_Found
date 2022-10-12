/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ArticleTypeDAO;
import daos.ItemTypeDAO;
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
@WebServlet(name = "CreateFormServlet", urlPatterns = {"/CreateFormServlet"})
public class CreateFormServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            }else if (session.getAttribute("userdata") != null) {
                    Member member = (Member) session.getAttribute("userdata");
                    if (member.getStatus() == 1) {
                        ArticleTypeDAO atDao = new ArticleTypeDAO();
                        List<ArticleType> listAT = atDao.getAllArticleType();
                        request.setAttribute("ListArticleType", listAT);
                        ItemTypeDAO itDao = new ItemTypeDAO();
                        List<Item> listI = itDao.getAllItems();
                        request.setAttribute("ListItemType", listI);
                        request.setAttribute("action", "create");
                        request.setAttribute("titlePost", request.getAttribute("titlePost"));
                        request.setAttribute("titleError", request.getAttribute("titleError"));

                        request.setAttribute("content", request.getAttribute("content"));
                        request.setAttribute("contentError", request.getAttribute("contentError"));

                        request.setAttribute("hashtag", request.getAttribute("hashtag"));
                        request.setAttribute("hashtagError", request.getAttribute("hashtagError"));

                        request.setAttribute("errorURL", request.getAttribute("errorURL"));
                        request.setAttribute("postURL", request.getAttribute("postURL"));

                        request.setAttribute("itemId", request.getAttribute("itemId"));

                        request.setAttribute("postTypeId", request.getAttribute("postTypeId"));

                        request.getRequestDispatcher("form.jsp").forward(request, response);

                    } else {
                        request.setAttribute("errormessage", "Your account has been banned! You cannot use this function!");
                        request.getRequestDispatcher("paging").forward(request, response);
                    }
                } else {
                    request.setAttribute("errormessage", "Please login!");
                    request.getRequestDispatcher("paging").forward(request, response);

                }           

        } catch (Exception e) {
            e.printStackTrace();
            log("ERROR at CreateFormServlet: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
