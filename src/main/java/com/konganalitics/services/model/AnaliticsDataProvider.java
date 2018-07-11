package com.konganalitics.services.model;


import com.konganalitics.dao.Shares;
import com.konganalitics.dao.SharesHistory;
import com.konganalitics.dao.SharesRateObject;
import com.konganalitics.services.repositories.SharesHistoryRepository;
import com.konganalitics.services.repositories.SharesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AnaliticsDataProvider {
    public static final String BOARD_ID = "TQBR";
    @Autowired
    private SharesHistoryRepository sharesHistoryRepository;

    @Autowired
    private SharesRepository sharesRepository;

    public List<SharesHistory> getTradeHistory(String secId) {
        List<SharesHistory> histories = sharesHistoryRepository.findAllByBoardIdAndSharesSecId("TQBR", secId);
        System.out.println("After request = \n" + histories);
        return histories;

    }

    public List<SharesHistory> getTradeHistoryByDateRange(String secId, String start, String end) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-mm-dd");
        Date startDate = simpleDateFormat.parse(start);
        Date endDate = simpleDateFormat.parse(end);
        List<SharesHistory> histories = sharesHistoryRepository.findAllByBoardIdAndSharesSecIdAndTradeDateBetween(BOARD_ID, secId, startDate, endDate);
        System.out.println("After request = \n" + histories);
        return histories;

    }

    public List<SharesHistory> getTradeHistoryByMaxDate(String secId) {

        List<SharesHistory> histories = new LinkedList<>();
        Map<String, Date> tqbr = sharesHistoryRepository.findMinMaxDates(BOARD_ID, secId);
        Date startDate = null;
        Date endDate = null;
        if (tqbr.size() < 1) {
            System.out.println("fuck");
        } else {
            System.out.println(tqbr.get("min_date"));
            System.out.println(tqbr.get("max_date"));
            startDate = (Date) tqbr.get("min_date");
            endDate = (Date) tqbr.get("max_date");
        }

        SharesHistory startHistory = sharesHistoryRepository.findByBoardIdAndSharesSecIdAndTradeDate(BOARD_ID, secId, startDate);
        SharesHistory endHistories = sharesHistoryRepository.findByBoardIdAndSharesSecIdAndTradeDate(BOARD_ID, secId, endDate);
        histories.add(startHistory);
        histories.add(endHistories);
        System.out.println("After request = " + histories.size());
        return histories;

    }

    public List<SharesRateObject> getSharesWithMaxGrowth( Integer count) {
        List<SharesRateObject> sharesRateObjects = new ArrayList<>();
        List<Shares> allByBoard = sharesRepository.findAllByBoardId(BOARD_ID);
        System.out.println("Size = " + allByBoard.size());
        for (Shares share : allByBoard) {

            String secId = share.getSecId();
            System.out.println("SecId = " + secId);
            List<SharesHistory> tradeHistoryByMaxDate = getTradeHistoryByMaxDate(secId);

            SharesHistory startHistory = tradeHistoryByMaxDate.get(0);
            SharesHistory endHistory = tradeHistoryByMaxDate.get(1);
            Double startCloseCost = startHistory.getLegalClosePrice();
            Double endCloseCost = endHistory.getLegalClosePrice();
            if (startCloseCost == null || endCloseCost == null){
                continue;
            }
            Double rate = 1.0 * endCloseCost / startCloseCost;

            SharesRateObject sharesRateObject = new SharesRateObject(share, startHistory.getTradeDate(), endHistory.getTradeDate(), rate, startCloseCost, endCloseCost);
            sharesRateObjects.add(sharesRateObject);
        }
        sharesRateObjects.sort(Comparator.comparing(SharesRateObject::getTradeRate)
        );

        if (count == null || count >sharesRateObjects.size() ){
            count = 10;
        }
        for (int i = sharesRateObjects.size(); i > sharesRateObjects.size() - count; i--){
            SharesRateObject sharesRateObject = sharesRateObjects.get(i-1);
            System.out.println("Place " + i);
            System.out.println(sharesRateObject.getSecId());
            System.out.println(sharesRateObject.getStartCloseCost() + ". " + sharesRateObject.getEndCloseCost());
            System.out.println(sharesRateObject.getTradeRate());
            System.out.println(sharesRateObject.getStartDate());
            System.out.println(sharesRateObject.getEndDate());
            System.out.println(sharesRateObject.getTradeRate());
            System.out.println(sharesRateObject.getLatName());

        }

        return sharesRateObjects.subList(sharesRateObjects.size() - count,sharesRateObjects.size() );
    }
}
