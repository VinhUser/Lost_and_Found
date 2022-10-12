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
public class Notification {

    private int notiId;
    private Member sender;
    private Member receiver;
    private Article article;
    private String content;
    private String notiTime;
    private int status;

    public Notification() {
        notiId = 0;
        sender = null;
        receiver = null;
        article=null;
        content = "";
        notiTime = "";
        status = 1;
    }

    public Notification(int notiId, Member sender, Member receiver, Article article, String content, String notiTime, int status) {
        this.notiId = notiId;
        this.sender = sender;
        this.receiver = receiver;
        this.article = article;
        this.content = content;
        this.notiTime = notiTime;
        this.status = status;
    }

    public int getNotiId() {
        return notiId;
    }

    public void setNotiId(int notiId) {
        this.notiId = notiId;
    }

    public Member getSender() {
        return sender;
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }

    public Member getReceiver() {
        return receiver;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotiTime() {
        return notiTime;
    }

    public void setNotiTime(String notiTime) {
        this.notiTime = notiTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
}
