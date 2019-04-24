import java.io.*;
import java.util.*;

public class TravelInfoRequest {
	public static void main(String[] args) throws IOException{
		FileReader templatefile = null;
		FileReader properties = null;
		FileWriter outputfile = null;
		templatefile = new FileReader("template_file.txt");
		properties = new FileReader("properties.txt");
		outputfile = new FileWriter("output_file.txt");
		
		Scanner sc;
		sc = new Scanner(properties);
		KeyValue[] kv = new KeyValue[100];
		int idx = 0;
		Countries startcountry=null, departcountry=null;
		while(sc.hasNext()) {
			kv[idx++] = new KeyValue(sc.nextLine());
			if(idx>=kv.length) {
				KeyValue[] tmp = new KeyValue[kv.length*2];
				for(int i=0;i<kv.length;i++) tmp[i] = kv[i];
				kv = tmp;
			}
			if(kv[idx-1].getKey().equals("startcountry")) {
				startcountry = new Countries(kv[idx-1].getValue());
			}
			if(kv[idx-1].getKey().equals("departcountry")) {
				departcountry = new Countries(kv[idx-1].getValue());
			}
		}
		Calendar c = Calendar.getInstance();
		kv[idx] = new KeyValue("date",c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE));
		sc.close();
		properties.close();
		sc = new Scanner(templatefile);
		while(sc.hasNext()) {
			String nowline = sc.nextLine();
			for(int i=0;i<=idx;i++) {
				nowline = nowline.replace("{"+kv[i].getKey()+"}", kv[i].getValue());
			}
			
			nowline = nowline.replace("<add info>", Distance.getDistance(
					new Distance(startcountry.getCountry(),startcountry.getLat(),startcountry.getLon()),
					new Distance(departcountry.getCountry(),departcountry.getLat(),departcountry.getLon())));
			outputfile.write(nowline+"\r\n");
		}
		sc.close();
		templatefile.close();
		outputfile.close();
	}
}
