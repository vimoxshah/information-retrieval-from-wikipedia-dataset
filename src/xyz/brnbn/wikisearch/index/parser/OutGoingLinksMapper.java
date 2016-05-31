package xyz.brnbn.wikisearch.index.parser;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import xyz.brnbn.wikisearch.index.hbase.setup.CreateHBaseTables;

public class OutGoingLinksMapper 
			extends Mapper<Object, Text, Text, Text> {
	
	static Configuration conf = null;
	static HTable table = null;
	
	static {
		
		try {
			
			conf = HBaseConfiguration.create();
			table = new HTable(conf, CreateHBaseTables.HTABLE_TITLEDOCID);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {

		try {
			
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder =  dFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(
						new ByteArrayInputStream(value.toString().getBytes())));
			
			String docID = doc.getDocumentElement().
									getElementsByTagName("id").item(0).getTextContent();
			
			String text = doc.getDocumentElement().
								getElementsByTagName("text").item(0).getTextContent();
			
			Pattern p = Pattern.compile("\\[\\[(.*?)\\]\\]");
			Matcher m = p.matcher(text);

			String OGL = "", OGTitleID = "";
			
			
			while(m.find()) {
				
				@SuppressWarnings("deprecation")
			    String OGTitle = URLEncoder.encode(m.group(1));
			    
				Get get = new Get(Bytes.toBytes(OGTitle));
				Result rs = table.get(get);
			
				for(KeyValue kv : rs.raw()){
					OGTitleID = new String(kv.getValue());
					OGL+=(OGTitleID+","); 
			    }
			}
			
			if (OGL.isEmpty()) {
				
				context.write(new Text(docID), new Text(""));
			}
			else {
				
				OGL = OGL.substring(0, OGL.length()-1);
				context.write(new Text(docID), new Text(OGL));
			}
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
	}
}
