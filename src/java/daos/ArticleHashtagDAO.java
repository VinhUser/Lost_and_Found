/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import utils.DBUtils;
import dtos.Article;
import dtos.ArticleHashTag;
import dtos.Hashtag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public class ArticleHashtagDAO {
    private Connection con;
    private PreparedStatement preStm;
    private ResultSet rs;


    public ArticleHashtagDAO() {
        
    }
    
    private void closeConnection() throws Exception{
        if(rs!=null){
            rs.close();
        }
        if(preStm!=null){
            preStm.close();
        }
        if(con!=null){
            con.close();
        }
    }
    public List<ArticleHashTag> getAllArticleHashtag() throws Exception{
        List<ArticleHashTag> result = null;
        try {         
            con = DBUtils.makeConnection();
            if(con!=null){
                String sql = "Select * From ArticleHashtag";          
            preStm = con.prepareStatement(sql);
            rs = preStm.executeQuery(); 
            result = new ArrayList<>();
            while (rs.next()){
                String hId = rs.getString("HashtagID");
                int aId = rs.getInt("ArticleID"); 
                ArticleDAO aDao = new ArticleDAO();
                HashtagDAO hDao = new HashtagDAO();
                Article a = aDao.find(aId);
                Hashtag h = hDao.getHashtagById(hId);
                ArticleHashTag ah = new ArticleHashTag(a, h);
                result.add(ah);
               }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    public boolean addNewArticleHashtag(Article a, Hashtag h) throws SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO ArticleHashtag "
                        + "VALUES (?, ?)";
                stm = con.prepareStatement(sql);
                stm.setInt(1, a.getArticleID());
                stm.setInt(2, h.getHashtagID());                
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
}
