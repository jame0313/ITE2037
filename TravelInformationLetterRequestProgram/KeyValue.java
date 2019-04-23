import java.util.*;

public class KeyValue {
	private String key;
	private String value;
	KeyValue(String input){
		StringTokenizer st = new StringTokenizer(input);
		this.key = st.nextToken("=");
		this.value = st.nextToken();
	}
	KeyValue(String key, String value){
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return this.key;
	}
	public String getValue() {
		return this.value;
	}
}
