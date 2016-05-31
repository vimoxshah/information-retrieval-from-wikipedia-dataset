package xyz.brnbn.wikisearch.index.parser;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DocumentsWordCountReducer 
		extends Reducer<Text, IntWritable, Text, IntWritable>{

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
			throws IOException, InterruptedException{
		
		Integer sum = 0;
		for (IntWritable value : values) {
			
			sum+=value.get();
		}
		context.write(key, new IntWritable(sum));
	}
}
