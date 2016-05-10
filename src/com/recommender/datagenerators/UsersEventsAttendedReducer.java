package com.recommender.datagenerators;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by arajawat on 5/8/2016.
 */
public class UsersEventsAttendedReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String[] tokens = key.toString().split(",");
        if(tokens.length == 2){
            String eventId = tokens[0];
            String[] userIds = tokens[1].split("\\s");
            for(String id: userIds){
                String localKey = id + "," + eventId + ",1";        // 1 for showing that they attended the event
                context.write(new Text(localKey), new Text(""));
            }
        }
    }
}
