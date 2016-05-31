package xyz.brnbn.wikisearch.index.retrieve;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import xyz.brnbn.wikisearch.index.hbase.setup.CreateHBaseTables;
import xyz.brnbn.wikisearch.utils.Porter;

public class IndexRetrieval {

	public static String correct(String s){
		StringBuilder sb =new StringBuilder(s);
		sb.setCharAt(0, Character.toUpperCase(s.charAt(0)));
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)==' ' || s.charAt(i)==','){
				sb.setCharAt(i+1, Character.toUpperCase(s.charAt(i+1)));
			}
		}
		return sb.toString();
		
	}
	
	
	/**
	 * @param query
	 * @return
	 */
	
	public static ArrayList<Document> retrieve(String query) {
		
		try {
			
			Configuration conf = HBaseConfiguration.create();
			HTable table_post = new HTable(conf, CreateHBaseTables.HTABLE_POSTINGLIST);
			HTable  table_pagerank = new HTable(conf, CreateHBaseTables.HTABLE_PAGERANK);
			HTable  table_docid_title = new HTable(conf, CreateHBaseTables.HTABLE_DOCIDTITLE);
			HTable  table_title_docid = new HTable(conf, CreateHBaseTables.HTABLE_TITLEDOCID);
			ArrayList<Document> docs = new ArrayList<>();
			ArrayList<Document> docs1 = new ArrayList<>();
			
			if (query.startsWith("**")) {
			
				boolean flag = false;
				Get get = new Get(Bytes.toBytes(URLEncoder.encode(query.substring(2).toLowerCase())));
				Result rsTT = table_title_docid.get(get);
				
				for (KeyValue kvTT : rsTT.raw()) {
					
					flag = true;
					Document d = new Document();
					String t = URLDecoder.decode(new String(kvTT.getRow()));
					t=correct(t);
					d.setTitle(t);
					docs.add(d);
				}
				if (flag) 
					return docs;
			}
			
			//query = (new Porter()).stripAffixes(query);
			
			//System.out.println("************ " + query);
			
			String lquery1 = query.toLowerCase();
			String[] words = lquery1.split(" "); 
			String lquery = words[0];
			Get get1 = new Get(Bytes.toBytes(lquery));
			Result rs = table_post.get(get1);
			Result rs1 = table_post.get(get1);
		
				
			for(KeyValue kv : rs1.raw()) {
				
				String family = new String(kv.getFamily());
				String docId = new String(kv.getQualifier());
				String tf = new String(kv.getValue());
				double tf1 = Double.parseDouble(tf);
				tf1 =(Math.round(tf1 * 10000d) / 10000d);
				
			if (family.equals(CreateHBaseTables.COLFAMILY_IDF)) {
					
					IDFRetrieve idfr = new IDFRetrieve();
				//	String idf = new String(kv.getValue());
					
					//double idf1 = Double.parseDouble(idf);
					//idf1 =(Math.round(idf1 * 10000d) / 10000d);
					idfr.setIdf((tf1));
			}
				
			if (family.equals(CreateHBaseTables.COLFAMILY_DOCID_TF)) {
					
					Document doc = new Document();
					doc.setDocId(Integer.parseInt(docId));
					doc.setTf((tf1));
					
					Get getPR = new Get(Bytes.toBytes(docId));
					Result rsPR = table_pagerank.get(getPR);
					for (KeyValue kvPR : rsPR.raw()) {
						
						String PR = new String(kvPR.getValue());
						double PR1 = Double.parseDouble(PR);
						PR1 =(Math.round(PR1 * 10000d) / 10000d);
						doc.setPagerank((PR1));
					}
			
					Get getT = new Get(Bytes.toBytes(docId));
					Result rsT = table_docid_title.get(getT);
					for (KeyValue kvT : rsT.raw()) {
						
						@SuppressWarnings("deprecation")
						String title = URLDecoder.decode(new String(kvT.getValue()));
						doc.setTitle(title);
					}
					if (doc.getTitle().toLowerCase().contains(lquery))
						docs1.add(doc);
					else
						docs.add(doc);
				}
		    }
			Collections.sort(docs);
			/*System.out.println("List: ");
			for (Document d : docs) {
				
				System.out.println("->"+d.getTitle());
			}
			*/
			if (docs1.size()>0) {
				Collections.sort(docs1);
				for (int i=docs1.size()-1;i>=0;i--) {
					docs.add(0, docs1.get(i));
				}
			}
			return docs;
		} catch (Exception e) {
			
			return new ArrayList<Document>();
		}
	}
	
	/*public static void main(String args[]) {
		
		retrieve("computer");
	}*/
}
