package associate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Apriori {

	private Map<Integer, Set<String>> db;
	private Float minSup, minConf;
	private Integer num;

	private Map<Integer, Map<Set<String>, Float>> freqItemSets;
	private Map<Set<String>, Set<Map<Set<String>, Float>>> associationRules;

	public Apriori(Map<Integer, Set<String>> db, float minSup, float minConf) {
		this.db = db;
		this.minConf = minConf;
		this.minSup = minSup;
		this.num = db.size();
		freqItemSets = new TreeMap<Integer, Map<Set<String>, Float>>();
		associationRules = new HashMap<Set<String>, Set<Map<Set<String>, Float>>>();
	}

	public Map<Set<String>, Float> findFrequentOneItemsets() {
		Map<Set<String>, Float> L1 = new HashMap<Set<String>, Float>();
		Map<Set<String>, Integer> itemOneSet = new HashMap<Set<String>, Integer>();

		Iterator<Map.Entry<Integer, Set<String>>> it = db.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Integer, Set<String>> entry = it.next();
			Set<String> itemSet = entry.getValue();
			for (String item : itemSet) {
				Set<String> key = new HashSet<String>();
				key.add(item.trim());
				if (!itemOneSet.containsKey(key)) {
					itemOneSet.put(key, 1);
				} else {
					itemOneSet.put(key, itemOneSet.get(key) + 1);
				}
			}
		}

		Iterator<Map.Entry<Set<String>, Integer>> it1 = itemOneSet.entrySet()
				.iterator();
		while (it1.hasNext()) {
			Map.Entry<Set<String>, Integer> entry = it1.next();

			float support = new Float(entry.getValue().toString())
					/ new Float(num);
			if (support >= minSup) {
				L1.put(entry.getKey(), support);
			}
		}
		return L1;
	}

	public Set<Set<String>> genApriori(int k, Set<Set<String>> freqKItemSet) {
		Set<Set<String>> candiFreqKItemSet = new HashSet<Set<String>>();
		Iterator<Set<String>> it1 = freqKItemSet.iterator();
		while (it1.hasNext()) {
			Set<String> itemSet1 = it1.next();
			Iterator<Set<String>> it2 = freqKItemSet.iterator();
			while (it2.hasNext()) {
				Set<String> itemSet2 = it2.next();
				if (!itemSet1.equals(itemSet2)) {
					// connection
					Set<String> comItems = new HashSet<String>();
					comItems.addAll(itemSet1);
					comItems.retainAll(itemSet2);
					if (comItems.size() == k-2) {
						Set<String> candiItems = new HashSet<String>();
						candiItems.addAll(itemSet1);
						candiItems.removeAll(itemSet2);
						candiItems.addAll(itemSet2);
						// cut
						if (!hasInfrequentSubset(candiItems, freqKItemSet)) {
							candiFreqKItemSet.add(candiItems);
						}
					}
				}
			}
		}
		return candiFreqKItemSet;
	}
	
	private boolean hasInfrequentSubset(Set<String> itemSet, Set<Set<String>> freqKItemSet){
		// get k-1 subSet
		Set<Set<String>> subItemSet = new HashSet<Set<String>>();
		Iterator<String> it = itemSet.iterator();
		while (it.hasNext()) {
			Set<String> subItem = new HashSet<String>();
			Iterator<String> it1 = itemSet.iterator();
			while (it1.hasNext()) {
				subItem.add(it1.next());
			}
			// k-1 subSet
			subItem.remove(it.next());
			subItemSet.add(subItem);
		}
		Iterator<Set<String>> it2 = subItemSet.iterator();
		while (it2.hasNext()) {
			if (!freqKItemSet.contains(it2.next())) return true;
		}
		return false;
	}
	
	public Map<Set<String>, Float> getFreqKItemSet(int k, Set<Set<String>> candiFreqKItemSet) {
		Map<Set<String>, Integer> candiFreqKItemSetMap = new HashMap<Set<String>, Integer>();
		
		Iterator<Map.Entry<Integer, Set<String>>> it = db.entrySet().iterator();
		
		// count support 
		while (it.hasNext()) {
			Map.Entry<Integer, Set<String>> entry = it.next();
			Iterator<Set<String>> it1 = candiFreqKItemSet.iterator();
			while (it1.hasNext()) {
				Set<String> s = it1.next();
				if (entry.getValue().containsAll(s)) {
					if (!candiFreqKItemSetMap.containsKey(s)) {
						candiFreqKItemSetMap.put(s, 1);
					} else {
						candiFreqKItemSetMap.put(s, candiFreqKItemSetMap.get(s) + 1);
					}
				}
			}	
		}
		
		// generate final frequent K item set
		Map<Set<String>, Float> freqKItemSetMap = new HashMap<Set<String>, Float>();
		Iterator<Map.Entry<Set<String>, Integer>> it2 = candiFreqKItemSetMap.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry<Set<String>, Integer> entry = it2.next();
			
			float support = new Float(entry.getValue().toString()) / num;
			if (support < minSup) {
				it2.remove();
			} else {
				freqKItemSetMap.put(entry.getKey(), support);
			}
		}
		return freqKItemSetMap;	
	}

	public void findAllFreqItemSet() {
		// frequent 1 item set
		Map<Set<String>, Float> freqOneItemSet = this.findFrequentOneItemsets();
		freqItemSets.put(1, freqOneItemSet);
		System.out.println("频繁1项集: " + freqOneItemSet);
		
		// frequent k item set
		int k = 2;
		while (true) {
			Set<Set<String>> candiFreItemSets = genApriori(k, freqItemSets.get(k - 1).keySet());
			Map<Set<String>, Float> freqKItemSetMap = getFreqKItemSet(k, candiFreItemSets);
			if (!freqKItemSetMap.isEmpty()) {
				freqItemSets.put(k, freqKItemSetMap);
			} else {
				break;
			}
			System.out.println("频繁 " + k + " 项集:" + freqKItemSetMap);
			k++;			
		}	
	}
	
	public void findAssociationRules() {
		Iterator<Map.Entry<Integer, Map<Set<String>, Float>>> it = freqItemSets.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Map<Set<String>, Float>> entry = it.next();
			for (Set<String> itemSet: entry.getValue().keySet()) {
				int n = itemSet.size() / 2;
				for (int i = 1; i <= n; i++) {
					Set<Set<String>> subset = ProperSubsetCombination.getProperSubset(i, itemSet);
					
					for (Set<String> conditionSet : subset) {
						Set<String> conclusionSet = new HashSet<String>();
						conclusionSet.addAll(itemSet);
						conclusionSet.removeAll(conditionSet);
						
						int s1 = conditionSet.size();
						int s2 = conclusionSet.size();
						
						float sup1 = freqItemSets.get(s1).get(conditionSet);
						float sup2 = freqItemSets.get(s2).get(conclusionSet);
						float sup = freqItemSets.get(s1+s2).get(itemSet);
						
						float conf1 = sup/sup1;
						float conf2 = sup/sup2;
						if(conf1 >= minConf){
							if(associationRules.get(conditionSet) == null){
								Set<Map<Set<String>, Float>> conclusionSetSet = new HashSet<Map<Set<String>, Float>>();
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf1);
								conclusionSetSet.add(sets);
								associationRules.put(conclusionSet, conclusionSetSet);
							} else {
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf1);
								associationRules.get(conditionSet).add(sets);
							}
						}
						
						if (conf2 >= minConf) {
							if (associationRules.get(conclusionSet) == null) {
								Set<Map<Set<String>, Float>> conclusionSetSet = new HashSet<Map<Set<String>,Float>>();
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conclusionSet, conf2);
								conclusionSetSet.add(sets);
								associationRules.put(conclusionSet, conclusionSetSet);
							} else {
								Map<Set<String>, Float> sets = new HashMap<Set<String>, Float>();
								sets.put(conditionSet, conf2);
								associationRules.get(conclusionSet).add(sets);
							}
						}
						
					}
				}
			}
		}
		System.out.println("association rules: " + associationRules);
	}
	
}
