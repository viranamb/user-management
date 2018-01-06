package com.cloud.dao.impl;

import com.cloud.constants.Constants;
import com.cloud.dao.DataRepositoryDao;
import com.cloud.model.ContentDO;
import com.cloud.model.PreRollDO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class acts as the data repository for all of the content and pre-roll information. The data is loaded into memory
 * during application startup.
 * <p>
 * To prevent running out of memory in the event the JSON file is large, we load this information from a JSON document in a sequence of tokens.
 * Since the streams operate on one token at a time, this ensures that the memory overhead is minimal.
 */
@Repository
public class DataRepositoryDaoImpl implements DataRepositoryDao {

    private static final Logger LOGGER = LogManager.getLogger(DataRepositoryDaoImpl.class);

    @Value("${json_file_path}")
    private String filePath;

    private Map<String, ContentDO> contentListMap;

    private Map<String, PreRollDO> preRollListMap;

    // Load the data at application startup
    @EventListener(ContextRefreshedEvent.class)
    void contextRefreshedEvent() {
        List<ContentDO> contentDOList = new ArrayList<>();
        List<PreRollDO> prerollList = new ArrayList<>();

        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath)), "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // Read file in stream mode to parse the json file by stepping through array elements as a stream to avoid
            // loading the complete document into memory.
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String objectName = jsonReader.nextName();
                if (Constants.CONTENT.equals(objectName)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        ContentDO contentDO = gson.fromJson(jsonReader, ContentDO.class);
                        contentDOList.add(contentDO);
                    }
                    jsonReader.endArray();
                } else if (Constants.PREROLL.equals(objectName)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        PreRollDO preRollDO = gson.fromJson(jsonReader, PreRollDO.class);
                        prerollList.add(preRollDO);
                    }
                    jsonReader.endArray();
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error parsing JSON data to memory : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                jsonReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        constructInMemoryDataMaps(contentDOList, prerollList);
    }

    /**
     * Create 2 maps, one having 'key --> Content name', 'value --> Content videos' and the other having 'key --> Preroll name', 'value --> Preroll videos'.
     * <p>
     * Using a map will ensure that the time complexity of the retrieve operation for content is O(1) and hence performance of the API will improve greatly.
     * The trade-off with this approach is longer server startup time to generate the required maps, but using appropriate deployments strategies (e.g blue-green on AWS),
     * we can safely ensure that, in the event of having to relaunch a new server, we could switch traffic to the server only after the server is fully started.
     * <p>
     * Using a ParallelStream instead of Stream so that the mapping is faster. Even though Parallel stream has some potential performance issues, since the mapping below
     * is a one-time activity occurring during only server startup, it's worth the trade-off
     *
     * Both maps are implementations of ConcurrentMap to ensure optimized performance along with thread-safety (Note: Since the current use-case has only read operations, even a regular
     * Hashmap without the overhead of synchronization would suffice, but using a ConcurrentMap allows for extensibility and flexibility if we need to add write operations in future)
     *
     * @param contentDOList
     * @param prerollList
     */
    private void constructInMemoryDataMaps(List<ContentDO> contentDOList, List<PreRollDO> prerollList) {
        contentListMap = contentDOList.parallelStream().collect(Collectors.toConcurrentMap(content -> content.getName(), content -> content));
        preRollListMap = prerollList.parallelStream().collect(Collectors.toConcurrentMap(preRoll -> preRoll.getName(), preRoll -> preRoll));
    }

    public ContentDO getContent(String contentIdentifier) {
        return contentListMap.get(contentIdentifier);
    }

    public PreRollDO getPreRoll(String preRoll) {
        return preRollListMap.get(preRoll);
    }

}

