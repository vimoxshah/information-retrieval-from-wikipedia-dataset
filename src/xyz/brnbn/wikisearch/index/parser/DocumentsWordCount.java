package xyz.brnbn.wikisearch.index.parser;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DocumentsWordCount {
	
	public static void main(String args[]) 
			throws IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		conf.set("mapred.textoutputformat.separator", ",");
	
		Job job = new Job(conf);
		
		job.setJarByClass(DocumentsWordCount.class);
		job.setJobName("DocumentWordCount");
	
		job.setMapperClass(DocumentsWordCountMapper.class);
		job.setReducerClass(DocumentsWordCountReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
