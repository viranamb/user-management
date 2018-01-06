package com.cloud.dao;

import com.cloud.model.PreRollDO;
import com.cloud.model.ContentDO;
import org.springframework.stereotype.Repository;

/**
 * Interface to clients wanting to retrieve information about playlists
 */
@Repository
public interface DataRepositoryDao {

    ContentDO getContent(String contentIdentifier);

    PreRollDO getPreRoll(String preRoll);

}

