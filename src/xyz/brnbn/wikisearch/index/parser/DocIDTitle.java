package xyz.brnbn.wikisearch.index.parser;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import xyz.brnbn.wikisearch.utils.*;

public class DocIDTitle {

	public static void main(String args[]) 
			throws IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		conf.set("mapred.textoutputformat.separator", ",");
		conf.set(XmlInputFormat1.START_TAG_KEY, "<page>");
		conf.set(XmlInputFormat1.END_TAG_KEY, "</page>");
		conf.set("io.serializations","org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
		
		Job job = new Job(conf);
		
		job.setJarByClass(DocIDTitle.class);
		job.setJobName("XmlParser - <TitleID:Title>");
		job.setInputFormatClass(XmlInputFormat1.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(DocIDTitleMapper.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
				
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
