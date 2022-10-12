/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class Comment implements Serializable{
    private int commentId;
    private Article article;
    private Member member;
    private String commentContent;
    private String commentTime;
    private int status;

    public Comment() {
        commentId=0;
        article=null;
        member=null;
        commentContent="";
        commentTime=null;
        status=0;
    }

    public Comment(int commentId, Article article, Member member, String commentContent, String commentTime, int status) {
        this.commentId = commentId;
        this.article = article;
        this.member = member;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.status = status;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member memberID) {
        this.member = memberID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    
    
}
