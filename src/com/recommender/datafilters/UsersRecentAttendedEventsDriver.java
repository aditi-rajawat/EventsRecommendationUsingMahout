package com.recommender.datafilters;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 * Created by arajawat on 5/8/2016.
 */
public class UsersRecentAttendedEventsDriver extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        System.out.println("UsersRecentAttendedEventsDriver class invoked..");
        System.out.println("Input data file path -- > "+ strings[0]);
        System.out.println("Output data file path ---> "+ strings[1]);

        Configuration conf = getConf();
        conf.set("userId", strings[2]);
        System.out.println("DEBUG  :  User ID = " + conf.get("userId"));
        Job job = new Job(conf,"users recently attended events");
        job.setJarByClass(UsersRecentAttendedEventsDriver.class);
        job.setMapperClass(UsersRecentAttendedEventsMapper.class);
        job.setReducerClass(UsersRecentAttendedEventsReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        //job.setNumReduceTasks(0);
        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));
        return job.waitForCompletion(true) ? 0:1;
    }
}
