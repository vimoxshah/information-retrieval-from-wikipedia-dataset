package xyz.brnbn.wikisearch.index.hbase.bulkload;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import xyz.brnbn.wikisearch.conf.Configurations;
import xyz.brnbn.wikisearch.index.hbase.setup.CreateHBaseTables;

public class PageRankBulkLoaderMapper 
		extends Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue> {
    
    public void map(LongWritable key, Text value, Context context)
    		throws IOException,InterruptedException {
    	
    	if (!value.toString().isEmpty()) {
    		
    		StringTokenizer st = new StringTokenizer(value.toString(), Configurations.DATA_DELIMITER);
    		byte[] rowKey = st.nextToken().getBytes();			
			KeyValue kv = new KeyValue(rowKey, CreateHBaseTables.COLFAMILY_PAGERANK.getBytes(), "PAGERANK".getBytes(), st.nextToken().getBytes());
			context.write(new ImmutableBytesWritable(rowKey), kv);
    	}
    }
}
