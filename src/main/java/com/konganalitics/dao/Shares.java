package com.konganalitics.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Shares {


    @OneToMany(mappedBy = "shares")
    public Set<SharesHistory> sharesHistory = new HashSet<>();

    @Id
    private String secId;
    private String shortName;
    private String isin;
    private String latName;
    private String secType;
    private String status;
    private Date settleDate;
    private String boardName;
    private String boardId;

    public Shares() {
    }


    public Shares(String secId, String shortName, String isin, String latName, String secType, String status, Date settleDate, String boardName, String boardId) {
        this.secId = secId;
        this.status = status;
        this.settleDate = settleDate;
        this.boardName = boardName;
        this.boardId = boardId;
        this.shortName = shortName;
        this.isin = isin;
        this.latName = latName;
        this.secType = secType;
    }

    public String getSecId() {
        return secId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getIsin() {
        return isin;
    }

    public String getLatName() {
        return latName;
    }

    public String getSecType() {
        return secType;
    }

    public Set<SharesHistory> getSharesHistory() {
        return sharesHistory;
    }



    public String getStatus() {
        return status;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getBoardId() {
        return boardId;
    }

//    @Override
//    public String toString() {
//        return "Shares{" +
//                ", secId='" + secId + '\'' +
//                ", shortName='" + shortName + '\'' +
//                ", isin='" + isin + '\'' +
//                ", latName='" + latName + '\'' +
//                ", secType='" + secType + '\'' +
//                ", status='" + status + '\'' +
//                ", settleDate=" + settleDate +
//                ", boardName='" + boardName + '\'' +
//                ", boardId='" + boardId + '\'' +
//                '}';
//    }
}
