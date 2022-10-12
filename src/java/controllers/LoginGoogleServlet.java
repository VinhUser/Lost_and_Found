/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.GooglePojo;
import utils.GoogleUtils;
import daos.MemberDAO;
import dtos.Member;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "LoginGoogleServlet", urlPatterns = {"/login-google"})
public class LoginGoogleServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final String INDEX_PAGE = "paging";
    private static final String ADMIN_PAGE = "AdminListServlet";
    
    public LoginGoogleServlet() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = INDEX_PAGE;
        try {            
            HttpSession session = request.getSession();
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {                
                uri = INDEX_PAGE;
                request.setAttribute("errormessage", "Please login!");
            } else {
                String accessToken = GoogleUtils.getToken(code);                
                GooglePojo googlePojo = GoogleUtils.getUserInfo(accessToken);
                
                Member member = new Member(googlePojo.getId(), googlePojo.getName(), googlePojo.getEmail(), googlePojo.getPicture(), "Profile", 1, 1, 0);
                MemberDAO mdao = new MemberDAO();
                String[] email = member.getMemberEmail().split("@");
                //check form mail
                if (email[1].equals("fpt.edu.vn") || email[1].equals("fe.edu.vn")) {
                    boolean check = mdao.checkMemberById(member.getMemberID());
                    //Nếu user chưa có thì add
                    if (!check) {
                        mdao.addNewMember(member);
                        session.setAttribute("userdata", member);
                        //Nếu có rồi thì lấy data của user ra để phục vụ hiển thị trên các view khác
                    } else {
                        member = mdao.find(member.getMemberID());
                        if (member.getStatus() == 0) {
                            session.setAttribute("userdata", member);
                            request.setAttribute("errormessage", "Your account has been banned!");                            
                            uri = INDEX_PAGE;
                        } else if (member.getMemberRole() == 1) {
                            session.setAttribute("userdata", member);
                            uri = INDEX_PAGE;
                        } else if (member.getMemberRole() == 0) {
                            session.setAttribute("userdata", member);
                            uri = ADMIN_PAGE;                            
                        }
                    }
                } else {  // mail ko đúng form
                    uri = INDEX_PAGE;
                    request.setAttribute("errormessage", "Your email can not join this web!");
                }
            }
        } catch (Exception e) {
            System.out.println("error at LoginGoogleServlet");
            uri = INDEX_PAGE;
//           request.setAttribute("errormessage", "Something error here. Please login again!");
            request.setAttribute("errormessage", e);
            System.out.println(""+e);
        } finally {
            request.getRequestDispatcher(uri).forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
