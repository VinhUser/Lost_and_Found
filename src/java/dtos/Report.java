/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Report implements Serializable {

    private int reportID;
    private String reportContent;
    private String reportTime;
    private String confirmTime;
    private int status;
    private Article article;
    private Member member;

    public Report() {
        reportID = 0;
        reportContent = "";
        reportTime = "";
        confirmTime = "";
        status = 1;
        article = null;
        member = null;
    }

    public Report(int reportID, String reportConntet, String reportTime, String confirmTime, int status, Article article, Member member) {
        this.reportID = reportID;
        this.reportContent = reportConntet;
        this.reportTime = reportTime;
        this.confirmTime = confirmTime;
        this.status = status;
        this.article = article;
        this.member = member;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void setMember(Member member) {
        this.member = member;
    }

}
