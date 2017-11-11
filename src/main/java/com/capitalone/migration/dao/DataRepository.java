package com.capitalone.migration.dao;

import com.capitalone.migration.constants.Constants;
import com.capitalone.migration.model.Aggregate;
import com.capitalone.migration.model.Content;
import com.capitalone.migration.model.PreRoll;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class acts as the data repository for all of the content and pre-roll information. The data is loaded into memory during application startup.
 * To prevent running out of memory, we load this information from a JSON document in a sequence of tokens.
 * Since the streams operate on one token at a time, this ensures that the memory overhead is minimal.
 *
 * Created by msz519 on 11/9/17.
 */
@Component
public class DataRepository {

    private static final Logger LOGGER = LogManager.getLogger(DataRepository.class);

    @Value("${json_file_path}")
    private String filePath;

    private Map<String, Content> contentListMap;

    private Map<String, PreRoll> preRollListMap;

    @EventListener(ContextRefreshedEvent.class)
    void contextRefreshedEvent() {
        Aggregate aggregate = new Aggregate();
        List<Content> contentList = new ArrayList<>();
        List<PreRoll> prerollList = new ArrayList<>();

        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath)), "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // Read file in stream mode to parse the json file by stepping through array elements as a stream to avoid loading the complete document into memory.
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String objectName = jsonReader.nextName();
                if (Constants.CONTENT.equals(objectName)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        Content content = gson.fromJson(jsonReader, Content.class);
                        contentList.add(content);
                    }
                    jsonReader.endArray();
                } else if (Constants.PREROLL.equals(objectName)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        PreRoll preRoll = gson.fromJson(jsonReader, PreRoll.class);
                        prerollList.add(preRoll);
                    }
                    jsonReader.endArray();
                }
            }
        }
        catch (IOException ex) {
            LOGGER.error("Error parsing JSON data to memory : " + ex.getMessage());
            ex.printStackTrace();
        }
        finally {
            try {
                jsonReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        aggregate.setContent(contentList);
        aggregate.setPreroll(prerollList);
        // Create 2 maps with key --> Content in one and key --> Preroll in the other. Value --> list of videos for both maps.

        // Using a map will ensure that the time complexity of the retrieve operation for content is O(1) and hence performance of the API will improve greatly.
        // The trade-off with this approach is longer server startup time to generate the required maps, but using blue-green deployments on AWS, we can safely ensure that,
        // in the event of having to relaunch a new server, we could ensure zero downtime and switch traffic to the server only after the server is fully started.

        // Using a ParallelStream instead of Stream so that the mapping is faster. Even though Parallel stream has some potential performance issues, since the mapping below
        // is a one-time activity occurring during only server startup, it's worth the trade-off
        contentListMap = aggregate.getContent().parallelStream().collect(Collectors.toConcurrentMap(content -> content.getName(), content -> content));
        preRollListMap = aggregate.getPreroll().parallelStream().collect(Collectors.toConcurrentMap(preRoll -> preRoll.getName(), preRoll -> preRoll));
    }

    public Content getContent(String contentIdentifier){
        return contentListMap.get(contentIdentifier);
    }

    public PreRoll getPreRoll(String preRoll){
        return preRollListMap.get(preRoll);
    }

}

