package com.recommender.datagenerators;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by arajawat on 5/8/2016.
 */
public class UsersEventsAttendedMapper extends Mapper<Text, Text, Text, Text> {

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        String[] tokens = key.toString().split(",");
        if(tokens.length == 5){
            String localKey = tokens[0] + "," + tokens[1];
            context.write(new Text(localKey), new Text(""));
        }
    }
}
