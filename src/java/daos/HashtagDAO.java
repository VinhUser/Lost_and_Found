/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import utils.DBUtils;
import dtos.Hashtag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public class HashtagDAO {
    private Connection con;
    private PreparedStatement preStm;
    private ResultSet rs;


    public HashtagDAO() {
        
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
    public Hashtag getHashtagByName(String name) throws Exception{
     Hashtag result = null;
        try {
            con = DBUtils.makeConnection();
            if(con!=null){
            String sql = "Select * From Hashtag Where HashtagName like ?";
            preStm = con.prepareStatement(sql);
            preStm.setString(1, name);
            rs = preStm.executeQuery();
            if(rs.next()){
                int hId = rs.getInt("HashtagID");
                String hName = rs.getString("HashtagName");                           
                result = new Hashtag(hId, hName);
                }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    public Hashtag getHashtagById(String id) throws Exception{
     Hashtag result = null;
        try {
            con = DBUtils.makeConnection();
            if(con!=null){
            String sql = "Select * From Hashtag Where HashtagID like ?";
            preStm = con.prepareStatement(sql);
            preStm.setString(1, id);
            rs = preStm.executeQuery();
            if(rs.next()){
                int hId = rs.getInt("HashtagID");
                String hName = rs.getString("HashtagName");                           
                result = new Hashtag(hId, hName);
                }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    
    public List<Hashtag> getAllHashtag() throws Exception{
        List<Hashtag> result = null;
        try {         
            con = DBUtils.makeConnection();
            if(con!=null){
                String sql = "Select * From Hashtag";          
            preStm = con.prepareStatement(sql);
            rs = preStm.executeQuery(); 
            result = new ArrayList<>();
            while (rs.next()){
                int hId = rs.getInt("HashtagID");
                String hName = rs.getString("HashtagName");    
                Hashtag h = new Hashtag(hId, hName);
                result.add(h);
               }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    public List<Hashtag> getAllHashtagByArticleID(String aId) throws Exception{
        List<Hashtag> result = null;
        try {         
            con = DBUtils.makeConnection();
            if(con!=null){
                String sql = "select H.HashtagID, H.HashtagName \n" +
                            "from Article A inner join ArticleHashtag AH on A.ArticleID = AH.ArticleID\n" +
                            "inner join Hashtag H on AH.HashtagID = H.HashtagID\n" +
                            "where A.ArticleID = ?";          
            preStm = con.prepareStatement(sql);
            preStm.setString(1, aId);
            rs = preStm.executeQuery(); 
            result = new ArrayList<>();
            while (rs.next()){
                int hId = rs.getInt("HashtagID");
                String hName = rs.getString("HashtagName");    
                Hashtag h = new Hashtag(hId, hName);
                result.add(h);
               }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    public int addNewHashtag(Hashtag h) throws SQLException, Exception {
        int idPost = 0;
        try {
            con = DBUtils.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO Hashtag (HashtagName) "
                        + "VALUES (?)";
                preStm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preStm.setString(1, h.getHashtagName());                
                int row = preStm.executeUpdate();
                if (row > 0) {
                    rs = preStm.getGeneratedKeys();
                    if (rs.next()) {
                        idPost = rs.getInt(1);
                    }                
                    return idPost;
                }
            }
        } finally {
            closeConnection();
        }
        return idPost;
    }
}
