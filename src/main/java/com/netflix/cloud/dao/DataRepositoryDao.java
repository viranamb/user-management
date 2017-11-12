package com.netflix.cloud.dao;

import com.netflix.cloud.model.ContentDO;
import com.netflix.cloud.model.PreRollDO;
import org.springframework.stereotype.Repository;

/**
 * Interface to clients wanting to retrieve information about playlists
 */
@Repository
public interface DataRepositoryDao {

    ContentDO getContent(String contentIdentifier);

    PreRollDO getPreRoll(String preRoll);

}

