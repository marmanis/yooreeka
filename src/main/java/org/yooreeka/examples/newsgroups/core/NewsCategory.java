package org.yooreeka.examples.newsgroups.core;
/**
 * News topic (category).
 */
public class NewsCategory implements java.io.Serializable {

	private static final long serialVersionUID = 1045139658403479594L;

	public static final String WORLD = "WORLD";
    public static final String US = "US";
    public static final String BUSINESS = "BUSINESS";
    public static final String SPORTS = "SPORTS";
    public static final String HEALTH = "HEALTH";
    public static final String TECHNOLOGY = "TECHNOLOGY";

    private String name;

    @Override
	public String toString() {
        return name;
    }
    public NewsCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (! (obj instanceof NewsCategory) )
            return false;
        final NewsCategory other = (NewsCategory) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}