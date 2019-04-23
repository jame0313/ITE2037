
public class Distance {
	private String name;
	private double lat;
	private double lng;
	Distance(String name, double lat, double lng){
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
	public String writeDistance() {
		return "Country : "+this.name+"\r\n"
				+ "latitude = "+this.lat+"\r\n"
				+ "longtitude = "+this.lng+"\r\n--------------------";
	}
	public static String getDistance(Distance a, Distance b) {
		double dist = Math.hypot((a.lng-b.lng), (a.lat-b.lat));
		return "Distance of\r\n"+a.writeDistance()+"\r\n"+b.writeDistance()+"\r\nis\r\n"+dist;
	}
}
