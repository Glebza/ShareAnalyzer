package com.konganalitics.services.model;

import com.konganalitics.dao.Shares;
import com.konganalitics.dao.SharesHistory;
import com.konganalitics.services.repositories.SharesHistoryRepository;
import com.konganalitics.services.repositories.SharesRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.*;

@Component
public class SharesGenerator {

    public static final int SECID_POSITION = 3;
    private static Logger logger = Logger.getLogger(SharesGenerator.class);
    @Autowired
    private SharesHistoryRepository sharesHistoryRepository;
    @Autowired
    private SharesRepository sharesRepository;

    public void retreiveSharesListFromStock() throws IOException {
        String sharesRawData = updateSharesList();

        List<Shares> shares = null;
        try {
            shares = parseSharesList(sharesRawData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sharesRepository.saveAll(shares);


    }


    private String updateSharesList() throws IOException {
        String url = "http://iss.moex.com/iss/engines/stock/markets/shares/securities.json";
        return getFromWeb(url);
    }

    private String getFromWeb(String url) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    public void updateSharesHistory() throws IOException, ParseException {

        for (Shares share : sharesRepository.findAll()) {
            updateShareTradeHistory(share);

        }


    }

    public void updateSharesHistory(String secId) throws IOException, ParseException {

        Shares share = sharesRepository.findSharesBySecId(secId);
        updateShareTradeHistory(share);


    }

    private void updateShareTradeHistory(Shares share) throws IOException, ParseException {
        String historyPagesUrl = "http://iss.moex.com/iss/history/engines/stock/markets/shares/securities/";
        String shareHistoryPage = "?start=";
        historyPagesUrl += share.getSecId() + ".json";
        String historyData = getFromWeb(historyPagesUrl);

        JSONObject obj = new JSONObject(historyData);
        System.out.println(obj.getJSONObject("history.cursor"));
        //В возращаемом json есть массив в массиве
        int historyPagesRecordsCount = (int) obj.getJSONObject("history.cursor").getJSONArray("data").getJSONArray(0).get(1);
        int recordsCountPerHistoryPage = (int) obj.getJSONObject("history.cursor").getJSONArray("data").getJSONArray(0).get(2);
        int startPage = 0;
        if (historyPagesRecordsCount > 1000) {

            startPage = historyPagesRecordsCount - 1000;

        }
        for (int i = startPage; i < historyPagesRecordsCount; i += recordsCountPerHistoryPage) {
            String currentPage = shareHistoryPage + i;
            System.out.println("Page number:" + i);
            String data = getFromWeb(historyPagesUrl + currentPage);
            List<SharesHistory> sharesHistories = parseSharesHistory(data);
            sharesHistoryRepository.saveAll(sharesHistories);
        }
    }

    private List<Shares> parseSharesList(String sharesData) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject obj = new JSONObject(sharesData);
        List<Shares> shares = new ArrayList<>();
        JSONArray arr = obj.getJSONObject("securities").getJSONArray("data");
        for (int i = 0; i < arr.length(); i++) {
            JSONArray DataArray = arr.getJSONArray(i);
            String secID = (String) DataArray.get(0);
            String isin = (String) DataArray.get(19);
            String secType = (String) DataArray.get(25);
            String shortName = (String) DataArray.get(2);
            String status = (String) DataArray.get(6);
            String settleDate = (String) DataArray.get(27);
            String latName = (String) DataArray.get(20);
            String boardName = (String) DataArray.get(7);
            String boardId = (String) DataArray.get(1);
            shares.add(new Shares(secID, shortName, isin, latName, secType, status, simpleDateFormat.parse(settleDate), boardName, boardId));
        }

        return shares;

    }

    private List<SharesHistory> parseSharesHistory(String history) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SharesHistory> sharesHistories = new ArrayList<>();
        JSONObject obj = new JSONObject(history);

        JSONArray arr = obj.getJSONObject("history").getJSONArray("data");
        for (int i = 0; i < arr.length(); i++) {
            JSONArray dataArray = arr.getJSONArray(i);
            String secID = dataArray.get(3).toString().equals("null") ? null : (String) dataArray.get(3);
            String boardID = dataArray.get(0).toString().equals("null") ? null : (String) dataArray.get(0);
            Double legalClosePrice = dataArray.get(9).toString().equals("null") ? null : dataArray.getDouble(9);


            Date tradeDate = simpleDateFormat.parse((String) dataArray.get(1));
            Shares shares = sharesRepository.findSharesBySecId(secID);
            SharesHistory sharesHistory = new SharesHistory(shares, legalClosePrice, tradeDate, boardID);
            sharesHistories.add(sharesHistory);
        }


        return sharesHistories;
    }

    public void updateSharesHistoryByAllShares() throws IOException, ParseException {
        for (Shares share : sharesRepository.findAll()){
            updateSharesHistory(share.getSecId());
        }
    }
}
