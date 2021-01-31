# GDFS-Analytics

```
package com.analytics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sujith Ramanathan
 *
 */
public class Analytics {
	
	private List<Integer> wlList;
	
	
	/** Default value will be 1 which will check for increase to stable **/
	private volatile int ruleType = 0;
	private static final int THRESHOLD = 3; 
	
	
	public Analytics() {
		this.wlList = new ArrayList<Integer>();
	}
	
	public void add(int waterLevel)throws Exception {
		if(wlList.size()<10) {
//			System.out.println("If");
			wlList.add(waterLevel);
		}else {
//			System.out.println("Else");
			for(int i=0;i<10;i++ ) {
				if(i < 9) {
//					System.out.println("Adding wlList.get("+(i+1)+") = "+wlList.get(i+1));
					wlList.set(i, wlList.get(i+1));
				}else {
//					System.out.println("Adding wlList.get("+(i+1)+") = ");
					wlList.set(i, waterLevel);
				}
			}
		}
//		System.out.println(wlList.toString()+"  - "+wlList.size());
		triggerOutput();
		Thread.sleep(1000);
	}
	
	public void triggerOutput() {
		int size = wlList.size();
		System.out.println("Size "+size);
//		if(0 == ruleType && size > THRESHOLD) {
		if(size > THRESHOLD) {
			int startIndex = (size-THRESHOLD);
			int prevWtrLvl = wlList.get(startIndex-1);
			int []pts = {prevWtrLvl, 0}; 
			for(int i=startIndex;  i< size; i++) {
				System.out.println("i = "+i+" value = "+wlList.get(i)+" prevWaterLevel = "+pts[0]);
//				if(wlList.get(i) > prevWtrLvl) {
//					prevWtrLvl=wlList.get(i);
//					triggerCount++;
//					if(THRESHOLD == triggerCount) {
//						System.out.println("Trigger motor");
//						triggerCount = 0;
//						waterStarted = 1;
//					}
//				}
				pts = analyzeOutput(wlList.get(i), pts);
			}
		}
//		}else if (1 == ruleType) {
//			int triggerCount = 0;
//			int startIndex = (size-THRESHOLD);
//			int temp = wlList.get(startIndex-1);
//			for(int i=startIndex;  i< size; i++) {
//				System.out.println("i = "+i+" value = "+wlList.get(i)+" temp = "+temp);
//				if(wlList.get(i) == temp) {
//					triggerCount++;
//					if(THRESHOLD == triggerCount) {
//						System.out.println("Water stopped");
//						triggerCount = 0;
//						ruleType = 3;
//					}
//				}
//			}
//		}
	}
	
	private int[] analyzeOutput(int waterLevel, int[] pts) {
		switch(ruleType) {
			case 0:
				return stableToIncrease(waterLevel, pts);
			case 1:
				return increaseToStable(waterLevel, pts);
			default:
				return pts;
		}
	}
	
	private int[] stableToIncrease(int waterLevel, int[] pts) {
		int prevWtrLvl = pts[0];
		int triggerCount = pts[1];
		if(waterLevel > prevWtrLvl) {
			prevWtrLvl = waterLevel;
			triggerCount++;
			if(THRESHOLD == triggerCount) {
				System.out.println("Trigger motor || stableToIncrease");
				triggerCount = 0;
				this.ruleType = 1;
			}
		}else {
			triggerCount++;
			if(THRESHOLD == triggerCount) {
				triggerCount = 0;
				System.out.println("stable to stable");
			}
		}
		pts[0] = prevWtrLvl;
		pts[1] = triggerCount; 
		return pts;
	}
	
	private int[] increaseToStable(int waterLevel, int[] pts) {
		int prevWtrLvl = pts[0];
		int triggerCount = pts[1];
		if(waterLevel == prevWtrLvl) {
			triggerCount++;
			if(THRESHOLD == triggerCount) {
				System.out.println("Increase to stopped");
				triggerCount = 0;
				this.ruleType = 0;
			}
		}
		pts[0] = prevWtrLvl;
		pts[1] = triggerCount; 
		return pts;
	}
	
	private int[] stableToStable(int waterLevel, int[] pts) {
		int prevWtrLvl = pts[0];
		int triggerCount = pts[1];
		if(this.ruleType == 3 && waterLevel == prevWtrLvl) {
			triggerCount++;
			if(THRESHOLD == triggerCount) {
				System.out.println("stable to stable");
				triggerCount = 0;
				this.ruleType = 3;
			}
		}
		pts[0] = prevWtrLvl;
		pts[1] = triggerCount; 
		return pts;
	}
//	if(wlList.get(i) > temp) {
//		temp=wlList.get(i);
//		triggerCount++;
//		if(THRESHOLD == triggerCount) {
//			System.out.println("Trigger motor");
//			triggerCount = 0;
//			waterStarted = true;
//		}
//	}
	
	
	public static void main(String []args)throws Exception {
		Analytics obj = new Analytics();
		obj.add(10);
		obj.add(100);
		obj.add(110);
		obj.add(120);
		obj.add(10);
		obj.add(10);
		obj.add(10);
		obj.add(10);
		obj.add(10);
		obj.add(10);
		obj.add(10);
		obj.add(10);
		System.out.println(obj.wlList.toString()+"  - "+obj.wlList.size());
	}

}


```
