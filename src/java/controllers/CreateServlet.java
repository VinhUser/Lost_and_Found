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
import daos.MemberDAO;
import daos.NotificationDAO;
import dtos.Article;
import dtos.ArticleType;
import dtos.Hashtag;
import dtos.Item;
import dtos.Member;
import dtos.Notification;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.annotation.MultipartConfig;
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
import javax.servlet.http.Part;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "CreateServlet", urlPatterns = {"/CreateServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100
)
public class CreateServlet extends HttpServlet {

    private static final String SUCCESS_FIND = "paging";
    private static final String SUCCESS_RETURN = "paging1";
    private static final String ADMIN_PAGE = "paging2";
    private static final String ERROR = "error.jsp";
    private static final String INVALID = "CreateFormServlet";
    private static final String UPLOAD_DIR = "images";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
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
                    String hashtagError = "";
                    String contentError = "";
                    String errorURL = "";
                    //String newId;
                    String textURL = request.getParameter("articleURL");
                    ArticleDAO aDao = new ArticleDAO();
                    // Xử lý title bài viết    
                    String titlePost = request.getParameter("txtTitle");
                    if (titlePost.trim().isEmpty() || titlePost.trim().length() < 10 || titlePost.trim().length() > 50) {
                        titleError = "Title must be at least 10 and at most 50 characters!";
                        valid = false;
                    }
                    // Xử lý nội dung bài viết 
                    String content = request.getParameter("txtContent");
                    if (content.trim().isEmpty() || content.trim().length() < 10 || content.trim().length() > 2000) {
                        contentError = "Content must be at least 10 and at most 2000 characters!";
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

                                    lstHashtag.add(hashtag);
                                }
                            }
                        }
                    }
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

                    //Xử lý ảnh của bài viết
                    Part filePart = request.getPart("photo");
                    String postURL = getFileName(filePart);
                    if (!postURL.equals("")) {
                    if (postURL.length() > 50) {
                            errorURL = "Image name must be at most 50";
                            valid = false;
                        }
                    if (!(postURL.endsWith(".png") || postURL.endsWith(".jpg"))) {
                            errorURL = "Image type must be a PNG or JPG file";
                            valid = false;
                        }
                    }

                    if (valid) {
                        // Xử lý ảnh để thêm vô DB
                        String articleURl;
                        if (postURL.equals("")) {
                            if (textURL != null && !textURL.equals("")) {
                                articleURl = textURL;
                            } else {
                                articleURl = null;
                            }
                        } else {
                            uploadFileToBuild(request);
                            articleURl = uploadFile(request);
                        }
                        // uploadFileToBuild(request);
                        Article a = new Article(0, titlePost.trim(), content.trim(), articleURl, LocalDateTime.now().toString(), 1, 0, i, memberLogin, at);

                        //Tạo bài viết và tạo lien ket cho hashtag và bài viết
                        int idPost = aDao.createNewArticle(a);
                        a.setArticleID(idPost);
                        if (lstHashtag != null) {
                            ArticleHashtagDAO ahDao = new ArticleHashtagDAO();
                            for (Hashtag hashtag : lstHashtag) {
                                if (hDao.getHashtagByName(hashtag.getHashtagName()) == null) {
                                    int idHashtag = hDao.addNewHashtag(hashtag);
                                    hashtag.setHashtagID(idHashtag);
                                }

                                ahDao.addNewArticleHashtag(a, hashtag);
                            }
                            // Tạo thông báo cho các member khác
                            //B1: Lấy ra các memberID có bài viết có type khớp với bài viết này
                            //B2: đưa zo bảng noti
                            MemberDAO mdao = new MemberDAO();
                            // Trường hợp bài viết của member đăng
                            NotificationDAO ndao = new NotificationDAO();
                            if (itemId != null && at.getTypeID() == 1) {
                                ArrayList<Member> listMembers = mdao.getAllMemberHavePostWithArticleTypeCorresponding(2, itemId, memberLogin.getMemberID());

                                for (Member listMember : listMembers) {
                                    String notiContent = "posted articles related to " + i.getItemName();
                                    ndao.addNewNotifications(new Notification(0, memberLogin, listMember, a, notiContent, LocalDateTime.now().toString(), 1));
                                }
                            } else if (itemId != null && at.getTypeID() == 2) {
                                ArrayList<Member> listMembers = mdao.getAllMemberHavePostWithArticleTypeCorresponding(1, itemId, memberLogin.getMemberID());

                                for (Member memReceive : listMembers) {
                                    String notiContent = "posted articles related to " + i.getItemName();
                                    ndao.addNewNotifications(new Notification(0, memberLogin, memReceive, a, notiContent, LocalDateTime.now().toString(), 1));
                                }
                            } else { // Trường hợp bài viết của ADMIN
                                ArrayList<Member> listMembers = mdao.getAllMembersReceiveNotiFromAdmin();
                                for (Member listMember : listMembers) {
                                    String notiContent = "The administrator has posted a notice about the system";
                                    ndao.addNewNotifications(new Notification(0, memberLogin, listMember, a, notiContent, LocalDateTime.now().toString(), 1));
                                }
                            }

                        }
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
//                    } else {
//                        request.setAttribute("errMessage", "Add failed");
//                    }
                    } else {
                        url = INVALID;
                        request.setAttribute("titlePost", titlePost);
                        request.setAttribute("titleError", titleError);
                        request.setAttribute("content", content);
                        request.setAttribute("contentError", contentError);
                        request.setAttribute("hashtag", hashtagName);
                        request.setAttribute("hashtagError", hashtagError);
                        request.setAttribute("errorURL", errorURL);
                        request.setAttribute("postURL", postURL);
                        if (itemId != null) {
                            request.setAttribute("itemId", Integer.parseInt(itemId));
                        }
                        request.setAttribute("postTypeId", Integer.parseInt(postTypeId));
                    }
                } else {
                    request.setAttribute("errormessage", "Your account has been banned! Cannot use this function!");
                    if(memberLogin.getMemberRole()==0){
                    request.getRequestDispatcher("AdminListServlet").forward(request, response);
                    }else if(memberLogin.getMemberRole()==1){
                    request.getRequestDispatcher("paging").forward(request, response);   
                    }else{
                    request.getRequestDispatcher("paging").forward(request, response);       
                    }
                }
            } else {
                request.setAttribute("errormessage", "Please login!");
                request.getRequestDispatcher("paging").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //System.out.println(url);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    //hàm này để lưu ảnh vào folder images
    private String uploadFile(HttpServletRequest request) throws IOException, ServletException {
        String fileName = "";
        try {
            Part filePart = request.getPart("photo");
            fileName = (String) getFileName(filePart);

            String applicationPath = request.getServletContext().getRealPath("").replace("build\\", "");
            String basePath = applicationPath + File.separator + UPLOAD_DIR + File.separator;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                File outputFilePath = new File(basePath + fileName);
                inputStream = filePart.getInputStream();
                outputStream = new FileOutputStream(outputFilePath);
                int read = 0;
                final byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fileName = "";
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            fileName = "";
        }
        return fileName;
    }

    //hàm này để lưu ảnh vào folder images trong build để khi hoàn thành việc thêm sách,
    //ảnh sách đó sẽ có mặt ngay lập tức để hiển thị trên library
    private void uploadFileToBuild(HttpServletRequest request) throws IOException, ServletException {
        String fileName = "";
        try {
            Part filePart = request.getPart("photo");
            fileName = (String) getFileName(filePart);

            String applicationPath = request.getServletContext().getRealPath("");
            String basePath = applicationPath + File.separator + UPLOAD_DIR + File.separator;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                File outputFilePath = new File(basePath + fileName);
                inputStream = filePart.getInputStream();
                outputStream = new FileOutputStream(outputFilePath);
                int read = 0;
                final byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fileName = "";
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            fileName = "";
        }
    }

    private String getFileName(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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
