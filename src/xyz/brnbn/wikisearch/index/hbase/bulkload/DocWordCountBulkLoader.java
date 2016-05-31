package xyz.brnbn.wikisearch.index.hbase.bulkload;

import xyz.brnbn.wikisearch.index.hbase.setup.CreateHBaseTables;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DocWordCountBulkLoader extends Configured implements Tool {
 	
    public static void main(String[] args) {
    	
		try {
		        int response = ToolRunner.run(HBaseConfiguration.create(), new DocWordCountBulkLoader(), args);
		        if(response == 0) {
		        	
		            System.out.println("Job is successfully completed..." + CreateHBaseTables.HTABLE_DOCWORDCOUNT);
		        } 
		        else {
		        	
		        	System.out.println("Job failed...");
		        }
		} 
		catch(Exception exception) {
			
		    exception.printStackTrace();
		}
    }


    public int run(String[] args) throws Exception {
       
        String outputPath = args[1];		
        Configuration configuration = getConf();		
        
        Job job = new Job(configuration);		
        job.setJarByClass(DocWordCountBulkLoader.class);		
        job.setJobName("Bulk Loading HBase Table::" + CreateHBaseTables.HTABLE_DOCWORDCOUNT);
        
        job.setMapperClass(DocWordCountBulkLoaderMapper.class);
        
        job.setInputFormatClass(TextInputFormat.class);		
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);	
        job.setMapOutputValueClass(KeyValue.class);	
        
        
        FileInputFormat.addInputPaths(job, args[0]);		
        FileSystem.getLocal(getConf()).delete(new Path(outputPath), true);		
        FileOutputFormat.setOutputPath(job, new Path(outputPath));		
        
        HFileOutputFormat.configureIncrementalLoad(job, new HTable(configuration, CreateHBaseTables.HTABLE_DOCWORDCOUNT));		
        job.waitForCompletion(true);		
   
        return 0;
    }

}
