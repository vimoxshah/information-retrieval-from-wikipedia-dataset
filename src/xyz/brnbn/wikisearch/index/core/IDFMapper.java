package xyz.brnbn.wikisearch.index.core;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import xyz.brnbn.wikisearch.conf.Configurations;

public class IDFMapper 
		extends Mapper<Object, Text, Text, Text> {

	@Override
	protected void map(Object key, Text value, Context context) 
				throws IOException, InterruptedException {
		
		String str = value.toString();
		if (!str.isEmpty()) {
			
			StringTokenizer st = new StringTokenizer(str, ",");
			String word = st.nextToken();
			int count = 0;
			while(st.hasMoreTokens()) {
				st.nextToken();
				count++;
			}
			System.out.println(count);
			count/=2;
			double ans = Math.log10(Configurations.TOTALDOCS/count);
			String ovalue = String.valueOf(ans) + str.substring(word.length());
			context.write(new Text(word), new Text(ovalue));
		}
	}
}
