/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import utils.DBUtils;
import dtos.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public class ItemTypeDAO {
    private Connection con;
    private PreparedStatement preStm;
    private ResultSet rs;
    
    public ItemTypeDAO(){       
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
    
    public Item getItemByID(int id) throws Exception{
     Item result = null;
        try {
            con = DBUtils.makeConnection();
            if(con!=null){
            String sql = "Select * From ItemType Where ItemID=?";
            preStm = con.prepareStatement(sql);
            preStm.setInt(1, id);
            rs = preStm.executeQuery();
            if(rs.next()){
                int cid = rs.getInt("ItemID");
                String cName = rs.getString("ItemName");                           
                result = new Item(cid, cName);
                }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
    
    public List<Item> getAllItems() throws Exception{
        List<Item> result = null;
        try {         
            con = DBUtils.makeConnection();
            if(con!=null){
                String sql = "Select * From ItemType";          
            preStm = con.prepareStatement(sql);
            rs = preStm.executeQuery(); 
            result = new ArrayList<>();
            while (rs.next()){
                int cid = rs.getInt("ItemID");
                String cName = rs.getString("ItemName");    
                Item c = new Item(cid, cName);
                result.add(c);
               }
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
