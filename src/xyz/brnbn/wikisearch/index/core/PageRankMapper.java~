package xyz.brnbn.wikisearch.index.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class PageRankMapper 
		extends Mapper<Object, Text, IntWritable, DoubleWritable> {
	
	//public static double NODOC = 6262;
	public static double INITIALRANK = 1;
	
	@Override
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException{
		
		ArrayList<Integer> OGLinks = new ArrayList<Integer>();
		StringTokenizer st = new StringTokenizer(value.toString(), ",");
		
		Integer c_doc = Integer.parseInt(st.nextToken());
		
		while(st.hasMoreElements()) {
			
			OGLinks.add(Integer.parseInt(st.nextToken()));
		}
		
		double pr_og = INITIALRANK/(OGLinks.size()+1);
		
		IntWritable ogIDIW = new IntWritable();
		DoubleWritable add_pr = new DoubleWritable(pr_og); 

		
		if (OGLinks.isEmpty()) {
			
			ogIDIW.set(c_doc);
			add_pr.set(INITIALRANK);
			context.write(ogIDIW, add_pr);
		}
				
		for (Integer ogID : OGLinks) {
			
			ogIDIW.set(ogID);
			context.write(ogIDIW, add_pr);
		}
		ogIDIW.set(c_doc);
		context.write(ogIDIW, add_pr);
	}
} 
