package com.konganalitics.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity()
public class SharesHistory {

    @JsonIgnore
    @ManyToOne
    public Shares shares;

    @Id
    @GeneratedValue
    private int recordId;

    private Double legalClosePrice;
    private Date tradeDate;
    private String boardId;

    public SharesHistory(final Shares shares, Double legalClosePrice, Date tradeDate, String boardId) {
        this.shares = shares;
        this.legalClosePrice = legalClosePrice;
        this.tradeDate = tradeDate;
        this.boardId = boardId;
    }

    public SharesHistory() {
    }

    public Double getLegalClosePrice() {
        return legalClosePrice;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public String getBoardId() {
        return boardId;
    }

    public int getRecordId() {
        return recordId;
    }


//    @Override
//    public String toString() {
//        return "SharesHistory{" +
//                "shares=" + shares +
//                ", recordId=" + recordId +
//                ", legalClosePrice=" + legalClosePrice +
//                ", tradeDate=" + tradeDate +
//                ", boardId='" + boardId + '\'' +
//                '}';
//    }
}
