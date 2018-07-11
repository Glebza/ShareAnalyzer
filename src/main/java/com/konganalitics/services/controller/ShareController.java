package com.konganalitics.services.controller;

import com.konganalitics.dao.SharesHistory;
import com.konganalitics.dao.SharesRateObject;
import com.konganalitics.services.model.AnaliticsDataProvider;
import com.konganalitics.services.model.SharesGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/shares")
public class ShareController {

    @Autowired
    private SharesGenerator shareGenerator;

    @Autowired
    private AnaliticsDataProvider analiticsDataProvider;

    @PutMapping
    public void putShares() throws IOException {
        shareGenerator.retreiveSharesListFromStock();
    }

    @PostMapping("/updateSecurityTradeHistory")
    public void updateHistory(@RequestParam String secId) throws IOException, ParseException {
        shareGenerator.updateSharesHistory(secId);
    }

    @GetMapping("/all/update")
    public void updateHistoryByAllShares() throws IOException, ParseException {
        shareGenerator.updateSharesHistoryByAllShares();
    }

    @GetMapping("/{secId}")
    public List<SharesHistory> getSharesHistoryData(@PathVariable String secId) {

        return analiticsDataProvider.getTradeHistory(secId);
    }

    @GetMapping("/date/{secId}")
    public List<SharesHistory> getSharesHistoryByDateRange(@PathVariable String secId, @RequestParam String startDate, @RequestParam String endDate ) throws ParseException {


        return analiticsDataProvider.getTradeHistoryByDateRange(secId,startDate,endDate);
    }

    @GetMapping("/growth/max/{count}")
    public List<SharesRateObject> getSharesHistoryByMaxDateRange(@PathVariable Integer count)  {


        return analiticsDataProvider.getSharesWithMaxGrowth(count);
    }
}
