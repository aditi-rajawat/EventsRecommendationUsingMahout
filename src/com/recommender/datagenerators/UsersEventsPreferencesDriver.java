package com.recommender.datagenerators;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 * Created by arajawat on 5/7/2016.
 */
public class UsersEventsPreferencesDriver extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        System.out.println("UsersEventsPreferencesDriver class invoked..");
        System.out.println("Input data file path -- > "+ strings[0]);
        System.out.println("Output data file path ---> "+ strings[1]);
        Job job = new Job(getConf(),"users-events-preferences training dataset");
        job.setJarByClass(UsersEventsPreferencesDriver.class);
        job.setMapperClass(UsersEventsPreferencesMapper.class);
        job.setReducerClass(UsersEventsPreferencesReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));
        return job.waitForCompletion(true) ? 0:1;
    }

}
