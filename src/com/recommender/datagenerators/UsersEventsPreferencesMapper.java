package com.recommender.datagenerators;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by arajawat on 5/7/2016.
 */
public class UsersEventsPreferencesMapper extends Mapper<Text, Text, Text, Text> {

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        String[] tokens = key.toString().split(",");
        if(tokens.length == 6){
            String localkey = tokens[0]+","+tokens[1]+","+tokens[4];
            String localValue = "";
            context.write(new Text(localkey), new Text(localValue));
        }
    }
}
