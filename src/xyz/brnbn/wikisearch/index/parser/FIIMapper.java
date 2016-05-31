package xyz.brnbn.wikisearch.index.parser;

import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.io.StringReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.xml.sax.SAXException;

import xyz.brnbn.wikisearch.utils.Porter;

public class FIIMapper 
			extends Mapper<Object, Text, Text, Text> {
	
	 private static Set<String> stopWords;

	 protected void setup(Context context) throws IOException, InterruptedException {
	        Configuration conf = context.getConfiguration();

	        stopWords = new HashSet<String>();
	        for(String word : conf.get("stop.words").split(",")) {
	            stopWords.add(word);
	        }
	 	}

	static Context g_context = null;
	//static Porter stem = new Porter(); 
	static class ParserProcess implements IArticleFilter {
		
		WikiModel wikiModel = new WikiModel("${image}", "${title}");
		final static Pattern regex = Pattern.compile("[A-Z][\\p{L}\\w\\p{Blank},\\\"\\';\\[\\]\\(\\)-]+[\\.!]", 
				Pattern.CANON_EQ);
		
		@Override
		public void process(WikiArticle page, Siteinfo arg1)
				throws SAXException {
			
			if (page != null && page.getText() != null && !page.getText().startsWith("#REDIRECT ")){

				Text id = new Text(page.getId()+ ",1");
				String wikiText = page.getText().
									replaceAll("[=]+[A-Za-z+\\s-]+[=]+", " ").
									replaceAll("\\{\\{[A-Za-z0-9+\\s-]+\\}\\}"," ").
									replaceAll("(?m)<ref>.+</ref>"," ").
									replaceAll("(?m)<ref name=\"[A-Za-z0-9\\s-]+\">.+</ref>"," ").
									replaceAll("<ref>"," <ref>");

				String plainStr = wikiModel.render(new PlainTextConverter(), wikiText).
					replaceAll("\\{\\{[A-Za-z+\\s-]+\\}\\}"," ");
				
				Matcher regexMatcher = regex.matcher(plainStr);
				while (regexMatcher.find()) {
					
					String sentence = regexMatcher.group();						
					if (!sentence.isEmpty()) {

						StringTokenizer st = new StringTokenizer(sentence, " ");
						while(st.hasMoreTokens()) {
							
							String wt = st.nextToken();
							wt = wt.replaceAll("[^A-Za-z]", "").toLowerCase();
							try {
								if (wt.length()>2 && !stopWords.contains(wt))
									g_context.write(new Text(wt), id);
							} 
							catch (InterruptedException e) {
								e.printStackTrace();
							}
							catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}	
	}

	
	@Override
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {
		

		g_context = context;
		try {
			
			String value_str = value.toString();
			
			if (!value_str.isEmpty()) {
				
				StringReader sr = new StringReader(value_str);
				try {
                    IArticleFilter handler = new ParserProcess();
                    WikiXMLParser wxp = new WikiXMLParser(sr, handler);
                    wxp.parse();
		        }
				catch (Exception e) {
		                e.printStackTrace();
		        }
			}
		} catch(Exception ex) {
			
			ex.printStackTrace();
		}
	}

}
