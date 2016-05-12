package com.recommender.datagenerators;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by arajawat on 5/9/2016.
 */
public class EventsContentAttributesReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String[] tokens = key.toString().split(",");
        if(tokens.length == 101){
            String eventId = tokens[0];
            int numOfPrefs = tokens.length - 1; // as first token contains the event Id

           // PreferenceArray preferenceArray = new GenericUserPreferenceArray(numOfPrefs);
            for(int i=0; i<numOfPrefs; i++){
                String localKey = eventId +"," + (i+1) + "," + tokens[i+1];
                context.write(new Text(localKey), new Text("") );
               // preferenceArray.set(i, new GenericPreference(Long.parseLong(eventId), i+1, Float.parseFloat(tokens[i+1])));
            }

           // context.write(preferenceArray, new Text(""));
        }
    }
}
