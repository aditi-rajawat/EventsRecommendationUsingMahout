package com.recommender.datafilters;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by arajawat on 5/8/2016.
 */
public class UsersRecentAttendedEventsReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String localValue = "";
        int i=0;

        for(Text eventId: values){
            if(i != 0){
                localValue += ",";
            }
            localValue += eventId.toString();
            i++;
        }

        context.write(key, new Text(localValue));
    }
}
