/**
 * 
 */
package org.yooreeka.util.parsing.vcf;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class VCFToken {
	String val;
	String entry;
	boolean isToken;
	int index=-1;
	
	/**
	 * @return the val
	 */
	public String getVal() {
		return val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(String val) {
		this.val = val;
	}
	/**
	 * @return the isToken
	 */
	public boolean isToken() {
		return isToken;
	}
	/**
	 * @param isToken the isToken to set
	 */
	public void setToken(boolean isToken) {
		this.isToken = isToken;
	}
	public void setIndex(int i) {
		index = i;
	}
	public int getIndex() {
		return index;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer("VCFToken[");
		b.append(index).append("]: ").append(val).append("\n");
		return b.toString();
	}
	/**
	 * @return the entry
	 */
	public String getEntry() {
		return entry;
	}
	/**
	 * @param entry the entry to set
	 */
	public void setEntry(String entry) {
		this.entry = entry;
	}
}
