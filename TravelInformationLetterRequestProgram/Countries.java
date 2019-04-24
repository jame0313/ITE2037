import java.io.*;
import java.util.*;

public class Countries {
	private String country;
	private double lat;
	private double lng;
	Countries(String country) throws IOException{
		this.country = country;
		FileReader countries = null;
		countries = new FileReader("countries.csv");
		Scanner sc = new Scanner(countries);
		StringTokenizer st;
		String[] countries_name = new String[300], latitude = new String[300], longitude = new String[300];
		int count = 0;
		while(sc.hasNext()) {
			String nowline = sc.nextLine();
			st = new StringTokenizer(nowline);
			String now_country = st.nextToken(",");
			String lat_string = st.nextToken(",");
			String lng_string = st.nextToken(",");
			countries_name[count] = now_country;
			latitude[count] = lat_string;
			longitude[count] = lng_string;
			count++;
		}
		sc.close();
		countries.close();
		for(int i=0;i<count;i++) {
				if(countries_name[i].equals(this.country)) {
					this.lat = Double.parseDouble(latitude[i]);
					this.lng = Double.parseDouble(longitude[i]);
					break;
				}
		}
		
	}
	public String getCountry() {
		return this.country;
	}
	public double getLat() {
		return this.lat;
	}
	public double getLon() {
		return this.lng;
	}
}
