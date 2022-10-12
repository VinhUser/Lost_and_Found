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
public class Article implements Serializable {

    private int articleID;
    private String title;
    private String articleContent;
    private String imgUrl;
    private String postTime;
    private int articleStatus;
    private int warningStatus;
    private Item item;
    private Member member;
    private ArticleType type;

    public Article() {
        articleID = 0;
        title = "";
        articleContent = "";
        imgUrl = "";
        postTime = null;
        articleStatus = 1;
        warningStatus = 0;
        item = null;
        member = null;
        type = null;
    }

    public Article(int articleID, String title, String articleContent, String imgUrl, String postTime, int articleStatus, int warningStatus, Item item, Member member, ArticleType type) {
        this.articleID = articleID;
        this.title = title;
        this.articleContent = articleContent;
        this.imgUrl = imgUrl;
        this.postTime = postTime;
        this.articleStatus = articleStatus;
        this.warningStatus = warningStatus;
        this.item = item;
        this.member = member;
        this.type = type;
    }

    public int getWarningStatus() {
        return warningStatus;
    }

    public void setWarningStatus(int warningStatus) {
        this.warningStatus = warningStatus;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public int getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(int articleStatus) {
        this.articleStatus = articleStatus;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
