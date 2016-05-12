package com.recommender.datagenerators;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by arajawat on 5/9/2016.
 */
public class EventsContentAttributesMapper extends Mapper<Text, Text, Text, Text> {

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {

        String[] tokens = key.toString().split(",");
        if(tokens.length == 110){
            String localKey = tokens[0];
            for(int i = 9; i< tokens.length-1; i++){
               localKey += "," + tokens[i];
            }
            context.write(new Text(localKey), new Text(""));
        }
    }
}
