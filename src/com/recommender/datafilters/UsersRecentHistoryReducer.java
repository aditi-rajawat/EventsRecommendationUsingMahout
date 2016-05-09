package com.recommender.datafilters;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by arajawat on 5/7/2016.
 */
public class UsersRecentHistoryReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        Configuration conf = context.getConfiguration();
        String userId = conf.get("userId");

        if(key.toString().equals(userId)){
            String localValue = "";
            int i = 0;
            for(Text event: values){
                if(i!=0){
                    localValue += ",";
                }
                localValue += event.toString();
                i++;
            }
            context.write(key, new Text(localValue));
        }
    }
}
