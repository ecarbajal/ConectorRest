package model;

public class AtributoModel {

	private String serieCFD;
	
	private String folioCFD;
	
	public AtributoModel(String serieCFD, String folioCFD) {
		super();
		this.serieCFD = serieCFD;
		this.folioCFD = folioCFD;		
	}
	
	public String getSerieCFD() {
		return serieCFD;
	}
	
	public void setSerieCFD(String serieCFD) {
		this.serieCFD = serieCFD;
	}
	
	public String getFolioCFD() {
		return folioCFD;
	}
	
	public void setFolioCFD(String folioCFD) {
		this.folioCFD = folioCFD;
	}
	
}
