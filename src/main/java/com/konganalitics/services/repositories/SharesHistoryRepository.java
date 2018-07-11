package com.konganalitics.services.repositories;

import com.konganalitics.dao.SharesHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SharesHistoryRepository extends CrudRepository<SharesHistory,String> {

     List<SharesHistory> findAllByBoardIdAndSharesSecId(String boardId,String secId);
     List<SharesHistory> findAllByBoardId(String boardId);
     List<SharesHistory> findAllByBoardIdAndSharesSecIdAndTradeDateBetween(String boardId, String secId, Date beginDate,Date endDate);


     @Query(value = "SELECT min(trade_date) min_date , max(trade_date) max_date from shares_history join shares on shares_history.shares_sec_id = shares.sec_id where shares_history.board_id = ?1 and shares.sec_id = ?2", nativeQuery = true)
     Map<String,Date> findMinMaxDates(String boardId, String secId);

     SharesHistory findByBoardIdAndSharesSecIdAndTradeDate(String boardId, String secId, Date beginDate);
}
