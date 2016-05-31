package xyz.brnbn.wikisearch.index.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FIIReducer 
			extends Reducer<Text, Text, Text, Text> {
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
		
		HashMap<String, Integer> docMap = new HashMap<String, Integer>();
		
		for(Text docFrq : values) {
			
			StringTokenizer st = new StringTokenizer(docFrq.toString(), ",");
			
			String docID = st.nextToken();
			int frq = Integer.parseInt(st.nextToken());
			
			if (docMap.containsKey(docID)) {
				
				int currentValue = docMap.get(docID);
				currentValue+=frq;
				docMap.put(docID, currentValue);
			}
			else {
				
				docMap.put(docID, frq);
			}
		}
		
		String postingList = "";
		for (String doc : docMap.keySet()) {
			
			//System.out.println(key.toString() + " : " + doc + " : " + docMap.get(doc));
			
			postingList += (doc + ",");
			postingList += String.valueOf(1+Math.log10(docMap.get(doc))) + ",";
		}
		postingList = postingList.substring(0, postingList.length()-1);
		context.write(key, new Text(postingList));
	}
}
