package com.konganalitics.dao;

import java.util.Date;

public class SharesRateObject extends Shares {

    private Date startDate;
    private Date endDate;
    private Double tradeRate;
    private Double startCloseCost;
    private Double endCloseCost;

    public SharesRateObject(Shares shares, Date startDate, Date endDate, Double tradeRate, Double startCloseCost, Double endCloseCost) {
        super(shares.getSecId(), shares.getShortName(), shares.getIsin(), shares.getLatName(), shares.getSecType(),shares.getStatus(), shares.getSettleDate(), shares.getBoardName(), shares.getBoardId());
        this.startDate = startDate;
        this.endDate = endDate;
        this.tradeRate = tradeRate;
        this.startCloseCost = startCloseCost;
        this.endCloseCost = endCloseCost;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Double getTradeRate() {
        return tradeRate;
    }

    public Double getStartCloseCost() {
        return startCloseCost;
    }

    public Double getEndCloseCost() {
        return endCloseCost;
    }
//
//    @Override
//    public String toString() {
//        return "SharesRateObject{" +
//                "startDate=" + startDate +
//                ", endDate=" + endDate +
//                ", tradeRate=" + tradeRate +
//                ", startCloseCost=" + startCloseCost +
//                ", endCloseCost=" + endCloseCost +
//                ", sharesHistory=" + sharesHistory +
//                '}';
//    }
}
