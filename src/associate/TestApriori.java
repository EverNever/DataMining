package associate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TestApriori {
	public static void main(String[] args) throws IOException {
		String f = "data/data.txt";
		float minSup = 0.05f;
		float minConf = 0.5f;
		File file = new File(f);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		Map<Integer, Set<String>> db = new HashMap<Integer, Set<String>>();
		
		String line;
		String spit = ",";
		int num = 0;
		
		while ((line = br.readLine()) != null) {
			String[] temp = line.trim().split(spit);
			Set<String> set = new TreeSet<String>();
			for (int i = 1; i < temp.length; i++) {
				set.add(temp[i].trim());
			}
			num++;
			db.put(num, set);
		}
		br.close();
		fr.close();
		
		Apriori ap = new Apriori(db, minSup, minConf);
		ap.findAllFreqItemSet();
		ap.findAssociationRules();
	}
}
