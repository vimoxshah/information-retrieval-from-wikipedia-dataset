package xyz.brnbn.wikisearch.index.hbase.setup;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class CreateHBaseTables {

	public final static String HTABLE_POSTINGLIST = "post_idf_tf_1";
	public final static String HTABLE_PAGERANK = "pagerank_1";
	public final static String HTABLE_DOCWORDCOUNT = "doc_word_count_1";
	public final static String HTABLE_DOCIDTITLE = "docid_title_1";
	public final static String HTABLE_TITLEDOCID = "title_docid_1";
	
	/*public final static String HTABLE_POSTINGLIST = "post_idf_tf";
	public final static String HTABLE_PAGERANK = "pagerank";
	public final static String HTABLE_DOCWORDCOUNT = "doc_word_count";
	public final static String HTABLE_DOCIDTITLE = "docid_title";
	public final static String HTABLE_TITLEDOCID = "title_docid";*/
	
	public final static String COLFAMILY_DOCID = "DOCID_COLFAMILY";
	public final static String COLFAMILY_TITLE = "TITLE_COLFAMILY";
	public final static String COLFAMILY_WORDCOUNT = "WORDCOUNT_COLFAMILY";
	public final static String COLFAMILY_PAGERANK = "RANK_COLFAMILY";
	public final static String COLFAMILY_DOCID_TF = "DOCID_TF_COLFAMILY";	
	public final static String COLFAMILY_IDF = "IDF_COLFAMILY";
	
	public static void main(String[] args) 
			throws IOException {

		@SuppressWarnings("deprecation")
		HBaseConfiguration hconfig = new HBaseConfiguration(new Configuration());
		HTableDescriptor htable = null;
		
		System.out.println("Conecting to HBase..");
		HBaseAdmin admin = new HBaseAdmin(hconfig);		

//		 * creating PostingList Tf_Idf table                                    *

		htable = new HTableDescriptor(HTABLE_POSTINGLIST);
		htable.addFamily(new HColumnDescriptor(COLFAMILY_IDF));
		htable.addFamily(new HColumnDescriptor(COLFAMILY_DOCID_TF));
		
		System.out.printf( "Creating Table(%s)...\n", HTABLE_POSTINGLIST );
		admin.createTable(htable) ;
		System.out.println("Done " + HTABLE_POSTINGLIST);
		
	
//		 * creating Document PageRank table                                     *
	
		htable = new HTableDescriptor(HTABLE_PAGERANK);
		htable.addFamily(new HColumnDescriptor(COLFAMILY_PAGERANK));
		
		System.out.printf("Creating Table(%s)...\n", HTABLE_PAGERANK);
		admin.createTable(htable) ;
		System.out.println("Done " + HTABLE_PAGERANK);
		
//		 * creating Document Word Count table                                   *		
		htable = new HTableDescriptor(HTABLE_DOCWORDCOUNT);
		htable.addFamily(new HColumnDescriptor(COLFAMILY_WORDCOUNT));
		
		System.out.printf("Creating Table(%s)...\n", HTABLE_DOCWORDCOUNT);
		admin.createTable(htable) ;
		System.out.println("Done " + HTABLE_DOCWORDCOUNT);
		
//		 * creating Document ID Title table                                     *
		htable = new HTableDescriptor(HTABLE_DOCIDTITLE);
		htable.addFamily(new HColumnDescriptor(COLFAMILY_TITLE));
		
		System.out.printf("Creating Table(%s)...\n", HTABLE_DOCIDTITLE);
		admin.createTable(htable) ;
		System.out.println("Done " + HTABLE_DOCIDTITLE);
	
//		 * creating Title Document Id table                                     *
		htable = new HTableDescriptor(HTABLE_TITLEDOCID);
		htable.addFamily(new HColumnDescriptor(COLFAMILY_DOCID));
		
		System.out.printf("Creating Table(%s)...\n", HTABLE_TITLEDOCID);
		admin.createTable(htable) ;
		System.out.println("Done " + HTABLE_TITLEDOCID);

		System.out.println("Succesfully created all tables!!");
	}
}
