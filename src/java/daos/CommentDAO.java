/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import utils.DBUtils;
import dtos.Article;
import dtos.Comment;
import dtos.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author LENOVO
 */
public class CommentDAO {


    public ArrayList<Comment> getAllCommentsByArticles(Article a) throws ClassNotFoundException, SQLException, Exception {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ArrayList<Comment> lb = new ArrayList<>();
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "Select * From Comment "
                        + "Where ArticleID Like ? and CommentStatus = 1 "
                        + "Order By CommentTime ASC";
                stm = con.prepareStatement(sql);
                stm.setInt(1, a.getArticleID());
                rs = stm.executeQuery();
                while (rs.next()) {
                    int cmtId = rs.getInt("CommentID");                    
                    String memPostCmt = rs.getString("MemberID");
                    String cmtContent = rs.getString("CommentContent");
                    String cmtTime = rs.getString("CommentTime");                    
                    int cmtStatus = rs.getInt("CommentStatus");
                    MemberDAO mdao = new MemberDAO();
                    Member m = mdao.find(memPostCmt);      
                    Comment c = new Comment(cmtId, a, m, cmtContent, cmtTime, cmtStatus);
                    lb.add(c);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return lb;
    }    

    public boolean addNewComment(Comment b) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO Comment (ArticleID, MemberID, CommentContent, CommentTime, CommentStatus) "
                        + "VALUES (?, ?, ?, ?, ?)";
                stm = con.prepareStatement(sql);
                stm.setInt(1, b.getArticle().getArticleID());
                stm.setString(2, b.getMember().getMemberID());
                stm.setString(3, b.getCommentContent());
                stm.setString(4, b.getCommentTime());
                stm.setInt(5, 1);               
                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean deleteComment(int cId)
            throws Exception, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "UPDATE Comment "
                        + "SET CommentStatus = 0 "
                        + "Where CommentID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, cId);
                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
    public Comment getCommentsById(String cId) throws ClassNotFoundException, SQLException, Exception {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Comment c = null;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "Select * From Comment "
                        + "Where CommentID Like ? ";                  
                stm = con.prepareStatement(sql);
                stm.setString(1, cId);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int cmtId = rs.getInt("CommentID");  
                    int aId = rs.getInt("ArticleID");
                    String memPostCmt = rs.getString("MemberID");
                    String cmtContent = rs.getString("CommentContent");
                    String cmtTime = rs.getString("CommentTime");                    
                    int cmtStatus = rs.getInt("CommentStatus");
                    MemberDAO mdao = new MemberDAO();
                    Member m = mdao.find(memPostCmt);   
                    ArticleDAO adao = new ArticleDAO();
                    Article art = adao.find(aId);
                    c = new Comment(cmtId, art, m, cmtContent, cmtTime, cmtStatus);                   
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return c;
    }    
}
