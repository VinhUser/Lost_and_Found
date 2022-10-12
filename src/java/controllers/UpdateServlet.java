/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ArticleDAO;
import daos.ArticleHashtagDAO;
import daos.ArticleTypeDAO;
import daos.HashtagDAO;
import daos.ItemTypeDAO;
import dtos.Article;
import dtos.ArticleType;
import dtos.Hashtag;
import dtos.Item;
import dtos.Member;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
@WebServlet(name = "UpdateServlet", urlPatterns = {"/UpdateServlet"})
public class UpdateServlet extends HttpServlet {

    private static final String SUCCESS_FIND = "paging";
    private static final String SUCCESS_RETURN = "paging1";
    private static final String ERROR = "error.jsp";
    private static final String INVALID = "UpdateFormServlet";
    private static final String ADMIN_PAGE = "paging2";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            } else if (session.getAttribute("userdata") != null) {
                Member memberLogin = (Member) session.getAttribute("userdata");
                if (memberLogin.getStatus() == 1) {
                    boolean valid = true;
                    String titleError = "";
                    String contentError = "";
                    String hashtagError = "";
                    String errorURL = "";
                    //String newId;
                    String idUpdate = request.getParameter("idUpdate");
                    String textURL = request.getParameter("articleURL");

                    // Xử lý title bài viết    
                    String titlePost = request.getParameter("txtTitle");
                    if (titlePost.trim().isEmpty() || titlePost.trim().length() < 10 || titlePost.trim().length() > 50) {
                        titleError = "Title must be at least 10 and at most 100 characters!";
                        valid = false;
                    }

                    // Xử lý nội dung bài viết  
                    String content = request.getParameter("txtContent");
                    if (content.isEmpty() || content.trim().length() < 20 || content.trim().length() > 4000) {
                        contentError = "Content must be at least 20 and at most 4000 characters!";
                        valid = false;
                    }
                    //Xử lý hashtag
                    String hashtagName = request.getParameter("txtHashtag");
                    ArrayList<Hashtag> lstHashtag = null;
                    HashtagDAO hDao = new HashtagDAO();
                    if (hashtagName != null) {
                        String regex = "#\\w*";
                        Pattern p = Pattern.compile(regex);
                        Matcher matcher = p.matcher(hashtagName);
                        while (matcher.find()) {
                            String hName = matcher.group();
                            if (hName.trim().length() > 21) {
                                hashtagError = "Hashtag name cannot exceed 20 characters!";
                                valid = false;
                            }
                        }
                        if (valid != false) {
                            // HashtagDAO hDao = new HashtagDAO();                   
                            // Tạo 1 mảng lưu các hashtag
                            lstHashtag = new ArrayList<>();
                            p = Pattern.compile(regex);
                            matcher = p.matcher(hashtagName);
                            while (matcher.find()) {
                                String hName = matcher.group();
                                //Kiểm tra xem tên hashtag đã tồn tại chưa

                                if (hDao.getHashtagByName(hName) != null) {

                                    Hashtag hashtag = hDao.getHashtagByName(hName);
                                    lstHashtag.add(hashtag);
                                } else if (hDao.getHashtagByName(hName) == null) {
                                    //Thêm mới hashtag zo DB
                                    Hashtag hashtag = new Hashtag(0, hName);
                                    //hDao.addNewHashtag(hashtag);
                                    lstHashtag.add(hashtag);
                                }
                            }
                        }
                    }
                    // Xử lý status bài viết:
                    String aStatus = request.getParameter("txtStatus");

                    // Xử lý loại đồ vật của bài viết
                    String itemId = request.getParameter("txtItem");
                    Item i = null;
                    if (itemId != null) {
                        ItemTypeDAO iDao = new ItemTypeDAO();
                        i = iDao.getItemByID(Integer.parseInt(itemId));
                    }

                    // Xử lý loại bài viết
                    String postTypeId = request.getParameter("txtArticleType");
                    ArticleTypeDAO aTDao = new ArticleTypeDAO();
                    ArticleType at = aTDao.getArticleTypeByID(Integer.parseInt(postTypeId));

                    ArticleDAO aDao = new ArticleDAO();
                    // Xử lý hashtag 
                    String regex = "#\\w*";
                    Pattern p = Pattern.compile(regex);
                    Matcher matcher = p.matcher(content);
                    while (matcher.find()) {
                        String hName = matcher.group();
                        if (hName.trim().length() > 21) {
                            contentError = "Tên của hashtag không được vượt quá 20 ký tự và chỉ bao gồm chữ cái và chữ số!";
                            valid = false;
                        }
                    }
                    if (valid) {
                        Article a = new Article(Integer.parseInt(idUpdate), titlePost.trim(), content.trim(), textURL, LocalDateTime.now().toString(), Integer.parseInt(aStatus), 0, i, memberLogin, at);
                        //Tạo bài viết và tạo lien ket cho hashtag và bài viết
                        if (lstHashtag != null) {
                            ArticleHashtagDAO ahDao = new ArticleHashtagDAO();
                            for (Hashtag hashtag : lstHashtag) {
                                if (hDao.getHashtagByName(hashtag.getHashtagName()) == null) {
                                    int idHashtag = hDao.addNewHashtag(hashtag);
                                    hashtag.setHashtagID(idHashtag);

                                }

                                ahDao.addNewArticleHashtag(a, hashtag);
                            }

                        }
                        aDao.updateContentArticle(a);
                        if (memberLogin.getMemberRole() == 1) {
                            if (a.getType().getTypeID() == 1) {
                                url = SUCCESS_FIND;
                            }
                            if (a.getType().getTypeID() == 2) {
                                url = SUCCESS_RETURN;
                            }
                        } else if (memberLogin.getMemberRole() == 0) {
                            url = ADMIN_PAGE;
                        }
                    } else {
                        url = INVALID;
                        request.setAttribute("idUpdate", idUpdate);
                        request.setAttribute("titlePost", titlePost);
                        request.setAttribute("titleError", titleError);
                        request.setAttribute("contentError", contentError);
                        request.setAttribute("hashtag", hashtagName);
                        request.setAttribute("hashtagError", hashtagError);
                        request.setAttribute("errorURL", errorURL);
                        request.setAttribute("content", content);
                        request.setAttribute("aStatus", aStatus);
                        request.setAttribute("postURL", textURL);
                        if (itemId != null) {
                            request.setAttribute("itemId", Integer.parseInt(itemId));
                        }
                        request.setAttribute("postTypeId", Integer.parseInt(postTypeId));
                    }
                } else {
                    request.setAttribute("errormessage", "Your account has been banned!");
                    request.getRequestDispatcher("paging").forward(request, response);
                }
            } else {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
