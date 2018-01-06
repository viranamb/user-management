/**
 * TODO - Describe purpose and operation of class.
 * <p>
 * <table border="1" cellpadding="0" cellspacing="0" width="100%">
 * <caption align="center">Edit and Version History</caption>
 * <tr><th>Version</th><th>Date</th><th>Author</th><th>Description</th></tr>
 * <tr><td>1.0</td><td>Aug 12, 2015</td><td>swx334</td><td>Initial creation.</td></tr>
 * </table>
 */
package com.cloud;

import com.cloud.exception.CustomException;
import com.cloud.model.PlaylistDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayListService {

    List<PlaylistDO> retrievePlaylist(String contentIdentifier, String countryCode) throws CustomException;
}