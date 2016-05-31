package xyz.brnbn.wikisearch.index.core;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class PageRankReducer 
		extends Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable>{

	
	@Override
	public void reduce(IntWritable docID, Iterable<DoubleWritable> rank_values, Context context) 
			throws IOException, InterruptedException {
		
		Double new_rank = 0.0;
		for (DoubleWritable rank_i : rank_values) {
			
			new_rank+=rank_i.get();
		}
		context.write(docID, new DoubleWritable(new_rank));
	}
}
