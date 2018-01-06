package com.cloud.service;

import com.cloud.model.PlaylistDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaylistService {

    List<PlaylistDO> retrievePlaylist(String contentIdentifier, String countryCode);
}

