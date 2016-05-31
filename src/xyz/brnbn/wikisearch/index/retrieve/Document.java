package xyz.brnbn.wikisearch.index.retrieve;

public class Document implements Comparable<Document>{

	private int docId;
	private double tf;
	private double idf;
	private double pagerank;
	private String  title;
	
	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public double getTf() {
		return tf;
	}

	public void setTf(double tf) {
		this.tf = tf;
	}

	public double getPagerank() {
		return pagerank;
	}

	public void setPagerank(double pagerank) {
		this.pagerank = pagerank;
	}

	@Override
	public int compareTo(Document o) {
		
		Double rank1 = this.tf*this.pagerank;
		Double rank2 = o.tf*o.pagerank;
		return rank2.compareTo(rank1);
	}
	
	public String toString() {
		
		return this.title + " : " + String.valueOf(docId);
	}
}
