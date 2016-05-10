package com.recommender.datafilters;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by arajawat on 5/8/2016.
 */
public class UsersRecentAttendedEventsMapper extends Mapper<Text, Text, Text, Text> {

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        String inputUserId = conf.get("userId");

        String[] tokens = key.toString().split(",");
        if(tokens.length == 5){
            String[] userIds = tokens[1].split("\\s");
            for(String id: userIds){
                if(id.equals(inputUserId)){
                    context.write(new Text(inputUserId), new Text(tokens[0]));
                }
            }
        }
    }
}
