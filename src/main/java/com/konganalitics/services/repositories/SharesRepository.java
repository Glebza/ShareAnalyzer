package com.konganalitics.services.repositories;

import com.konganalitics.dao.Shares;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharesRepository extends JpaRepository<Shares, String> {


    Shares findSharesBySecId(String secId);

    List<Shares> findAllByBoardId(String boardId);
}
