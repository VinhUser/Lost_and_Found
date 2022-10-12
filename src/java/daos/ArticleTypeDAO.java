/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import utils.DBUtils;
import dtos.ArticleType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public class ArticleTypeDAO {
    private Connection con;
    private PreparedStatement preStm;
    private ResultSet rs;
    
    public ArticleTypeDAO(){       
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
    
    public ArticleType getArticleTypeByID(int id) throws Exception{
     ArticleType result = null;
        try {
            con = DBUtils.makeConnection();
            if(con!=null){
            String sql = "Select * From ArticleType Where ArticleTypeID=?";
            preStm = con.prepareStatement(sql);
            preStm.setInt(1, id);
            rs = preStm.executeQuery();
            if(rs.next()){
                int cid = rs.getInt("ArticleTypeID");
                String cName = rs.getString("ArticleTypeName");                           
                result = new ArticleType(cid, cName);
                }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    
    public List<ArticleType> getAllArticleType() throws Exception{
        List<ArticleType> result = null;
        try {         
            con = DBUtils.makeConnection();
            if(con!=null){
                String sql = "Select * From ArticleType";          
            preStm = con.prepareStatement(sql);
            rs = preStm.executeQuery(); 
            result = new ArrayList<>();
            while (rs.next()){
                int cid = rs.getInt("ArticleTypeID");
                String cName = rs.getString("ArticleTypeName");    
                ArticleType c = new ArticleType(cid, cName);
                result.add(c);
               }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
