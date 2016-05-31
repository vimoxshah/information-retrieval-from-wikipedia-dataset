package xyz.brnbn.wikisearch.index.parser;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DocumentsWordCountMapper extends
		Mapper<Object, Text, Text, IntWritable> {

	@Override
	protected void map(Object key, Text value, Context context) 
				throws InterruptedException, IOException{
		
		String str = value.toString();
		StringTokenizer st = new StringTokenizer(str, ",");
		st.nextToken();
		while(st.hasMoreTokens()) {
			
			context.write(new Text(st.nextToken()), new IntWritable(Integer.parseInt(st.nextToken())));
		}
	}
	
}
