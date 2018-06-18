package semantic;

public class ErrorTokenS extends TokenS {
	
	public ErrorTokenS(String name, String source){
		super(name,source);
	}
	
	public ErrorTokenS(String name, String value, String source){
		super(name,source);
		this.value=value;
	}
}
