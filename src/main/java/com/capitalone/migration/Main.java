//package com.capitalone.migration;
//
//import com.capitalone.migration.model.Aggregate;
//import com.capitalone.migration.model.Content;
//import com.capitalone.migration.model.PreRoll;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.stream.JsonReader;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by msz519 on 11/9/17.
// */
//public class Main {
//
//    public static void main(String[] args) throws IOException {
//
//        String fileName = "src//main//resources//sample.json";
//        Aggregate aggregate = new Aggregate();
//
//        // Since the input json could be a large file, we run the risk of running out of memory during the parsing and loading of in-memory data objects.
//        // To avoid it, we follow a streaming strategy to parse the json file so that the data is loaded into memory in a staggered fashion.
//        JsonReader jsonReader = new JsonReader(new InputStreamReader(Files.newInputStream(Paths.get(fileName)), "UTF-8"));
//        Gson gson = new GsonBuilder().create();
//
////            aggregate = gson.fromJson(jsonReader, Aggregate.class);
//
//        List<Content> contentList = new ArrayList<>();
//        List<PreRoll> prerollList = new ArrayList<>();
//
//        // Read file in stream mode
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            // Read data into object model
//            String objectName = jsonReader.nextName();
//            if ("content".equals(objectName)) {
//                jsonReader.beginArray();
//                while (jsonReader.hasNext()) {
//                    Content content = gson.fromJson(jsonReader, Content.class);
//                    contentList.add(content);
//                }
//                jsonReader.endArray();
//            } else if ("preroll".equals(objectName)) {
//                jsonReader.beginArray();
//                while (jsonReader.hasNext()) {
//                    PreRoll preRoll = gson.fromJson(jsonReader, PreRoll.class);
//                    prerollList.add(preRoll);
//                }
//                jsonReader.endArray();
//            }
////                aggregate = gson.fromJson(jsonReader, Aggregate.class);
//        }
//        jsonReader.close();
//
//        // Create 2 maps with key --> Content in one and key --> Preroll in the other. Value --> list of videos for both maps.
//
//        // Using a map will ensure that the time complexity of the retrieve operation is O(1) and hence performance of the API will improve greatly. The trade-off with this
//        // approach is longer server startup time to generate the required maps, but using blue-green deployments on AWS, we can safely ensure that, in the event of having to
//        // launch a new server, we could ensure zero downtime and switch traffic to the server only after the server is fully started.
//
//        // Using a ParallelStream instead of Stream so that the mapping is faster. Even though Parallel stream has some potential performance issues, since the mapping below
//        // is a one-time activity occurring during only server startup, it's worth the trade-off
////            Map<Content, List<Video>> contentListMap = aggregate.getContent().parallelStream().collect(Collectors.toConcurrentMap(content -> content, content -> content.getVideos()));
////            Map<PreRoll, Set<Video>> preRollListMap = aggregate.getPreroll().parallelStream().collect(Collectors.toConcurrentMap(preRoll -> preRoll, preRoll -> preRoll.getVideos()));
//        aggregate.setContent(contentList);
//        aggregate.setPreroll(prerollList);
//        aggregate.toString();
//    }
//}
