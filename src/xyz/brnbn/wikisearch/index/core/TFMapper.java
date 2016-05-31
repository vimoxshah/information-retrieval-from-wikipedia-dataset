package xyz.brnbn.wikisearch.index.core;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TFMapper extends Mapper<Object, Text, Text, Text> {
/*	
	static Configuration conf;
	static HTable table;
	static {
		conf = HBaseConfiguration.create();
		
		try {
			table = new HTable(conf, "pgid_totalword");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/	
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {
	
			String s = value.toString();
			StringTokenizer st= new StringTokenizer(s, ",");
			String word = st.nextToken();
			
			//Map<Integer, Double> docID_TW = new HashMap<>();
            String ovalue = "";
            
			while(st.hasMoreTokens()) {
				
				String docID = st.nextToken();
				Double frq = Double.parseDouble(st.nextToken());//, totalWords = 0.0;
				//int docid = Integer.parseInt(docID);
				/*if ((totalWords = docID_TW.get(docid))==null) {
					
					Get get = new Get(Bytes.toBytes(docID));
		            Result rs = table.get(get);
		            for(KeyValue kv : rs.raw()) {
		            	
		            	String tmps = new String(kv.getValue());
		            	totalWords = Double.parseDouble(tmps);
		            }
		            docID_TW.put(docid, totalWords);
				}*/	
					
	            double tf = 1 + Math.log10(frq);
	            ovalue+=(docID+","+String.valueOf(tf)+",");
	            
			}
			ovalue=ovalue.substring(0,ovalue.length()-1);
			//System.out.println(word+"\t"+ovalue);
			context.write(new Text(word), new Text(ovalue));
	
	}
}


