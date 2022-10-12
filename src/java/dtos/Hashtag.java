/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author LENOVO
 */
public class Hashtag {
    private int hashtagID;
    private String hashtagName;

    public Hashtag() {
    }

    public Hashtag(int hashtagID, String hashtagName) {
        this.hashtagID = hashtagID;
        this.hashtagName = hashtagName;
    }

    public int getHashtagID() {
        return hashtagID;
    }

    public void setHashtagID(int hashtagID) {
        this.hashtagID = hashtagID;
    }

    public String getHashtagName() {
        return hashtagName;
    }

    public void setHashtagName(String hashtagName) {
        this.hashtagName = hashtagName;
    }
    
    
}
