# GDFS-Analytics

```
package com.analytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Sujith Ramanathan
 *
 */

class WaterLevelData {

	private final int waterLevel;
	private final long ts;

	public WaterLevelData(int waterLevel, long ts) {
		this.waterLevel = waterLevel;
		this.ts = ts;
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	public long getTs() {
		return ts;
	}

	@Override
	public String toString() {
		return "WaterLevelData [waterLevel=" + waterLevel + ", ts=" + ts + "]";
	}

}

class WaterLevelSeries {

	private List<WaterLevelData> waterLevelSeries;
	private int ruleType;

	private static final int DEFAULT_PACKET_SIZE = 10;
	private static final int ROTATIONAL_PACKET_SIZE = DEFAULT_PACKET_SIZE - 1;

	public WaterLevelSeries() {
		this.waterLevelSeries = new ArrayList<WaterLevelData>();
	}

	public void add(WaterLevelData data) {
		if (waterLevelSeries.size() < DEFAULT_PACKET_SIZE) {
			waterLevelSeries.add(data);
		} else {
			for (int i = 0; i < DEFAULT_PACKET_SIZE; i++) {
				if (i < ROTATIONAL_PACKET_SIZE) {
					waterLevelSeries.set(i, waterLevelSeries.get(i + 1));
				} else {
					waterLevelSeries.set(i, data);
				}
			}
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<WaterLevelData> getWaterLevelSeries() {
		return waterLevelSeries;
	}

	public int getRuleType() {
		return ruleType;
	}

	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}

}

public class Analytics_Copy {

	/** Default value will be 1 which will check for increase to stable **/

	private static final int TRIGGER_THRESHOLD_SIZE = 3;
	private static final int THRESHOLD_TIME = -15;
	private static final int DEFAULT_PACKET_SIZE = 10;
	private static final int ROTATIONAL_PACKET_SIZE = DEFAULT_PACKET_SIZE - 1;

	public void analyzeWaterLevelSeries(WaterLevelSeries waterLevelSeries, long latestTs) {
		long thresholdTime = getThresholdTime(latestTs);
		List<WaterLevelData> filteredData = waterLevelSeries.getWaterLevelSeries().stream()
				.filter(wls -> wls.getTs() >= thresholdTime && wls.getTs() <= latestTs).collect(Collectors.toList());

		trigger(filteredData, waterLevelSeries);
	}

	private void trigger(List<WaterLevelData> data, WaterLevelSeries waterLevelSeries) {
		int size = data.size();
		System.out.println("Size " + size);
		if (size > TRIGGER_THRESHOLD_SIZE) {
			int startIndex = (size - TRIGGER_THRESHOLD_SIZE);
			int prevWtrLvl = data.get(startIndex - 1).getWaterLevel();
			int[] pts = { prevWtrLvl, 0 };
			for (int i = startIndex; i < size; i++) {
				System.out.println("i = " + i + " value = " + data.get(i) + " prevWaterLevel = " + pts[0]);
				pts = analyzeOutput(data.get(i), pts, data, waterLevelSeries);
			}
		}
	}

	private int[] analyzeOutput(WaterLevelData waterLevelData, int[] pts, List<WaterLevelData> dataList,
			WaterLevelSeries waterLevelSeries) {
		switch (waterLevelSeries.getRuleType()) {
		case 0:
			return stableToIncrease(waterLevelData.getWaterLevel(), pts, dataList, waterLevelSeries);
		case 1:
			return increaseToDecrease(waterLevelData.getWaterLevel(), pts, dataList, waterLevelSeries);
		default:
			return pts;
		}
	}

	private int[] stableToIncrease(int waterLevel, int[] pts, List<WaterLevelData> waterLevelSeriesList,
			WaterLevelSeries waterLevelSeries) {
		int prevWtrLvl = pts[0];
		int triggerCount = pts[1];
		if (waterLevel > prevWtrLvl) {
			prevWtrLvl = waterLevel;
			triggerCount++;
			if (TRIGGER_THRESHOLD_SIZE == triggerCount) {
				System.out.println("3 Packets got increased");
				int size = TRIGGER_THRESHOLD_SIZE * 2; // Checking previous packets to confirm
				int wtrLvlListSize = waterLevelSeriesList.size();
//				System.out.println("Sizes size :: "+size+" wtrLvlListSize :: "+wtrLvlListSize);
				if (wtrLvlListSize >= size) {
					int defaultWaterLvl = waterLevelSeriesList.get(0).getWaterLevel();
					int waterLvl = 0;
					for (int i = 1; i < wtrLvlListSize; i++) {
						waterLvl = waterLevelSeriesList.get(i).getWaterLevel();
						System.out.println("Trigger count :: " + triggerCount + " size :: " + size
								+ ", DefaultWaterLvl :: " + defaultWaterLvl + " waterLvl :: " + waterLvl);
						if (waterLvl > defaultWaterLvl) {
							defaultWaterLvl = waterLvl;
							triggerCount++;
							if (size == triggerCount) {
								System.out.println("Trigger motor || stableToIncrease");
								triggerCount = 0;
								waterLevelSeries.setRuleType(1);
							}
						}
					}
					triggerCount = 0;
				}
			}
		} else {
			triggerCount++;
			if (TRIGGER_THRESHOLD_SIZE == triggerCount) {
				triggerCount = 0;
				System.out.println("stable to stable");
			}
		}
		pts[0] = prevWtrLvl;
		pts[1] = triggerCount;
		return pts;
	}

	private int[] increaseToDecrease(int waterLevel, int[] pts, List<WaterLevelData> waterLevelSeriesList,
			WaterLevelSeries waterLevelSeries) {
		int prevWtrLvl = pts[0];
		int triggerCount = pts[1];
		if (waterLevel == prevWtrLvl) {
			triggerCount++;
			if (TRIGGER_THRESHOLD_SIZE == triggerCount) {
				int size = TRIGGER_THRESHOLD_SIZE * 2; // Double check with previous packets
				int wtrLvlListSize = waterLevelSeriesList.size();
				System.out.println("waterLvlListSize :: " + wtrLvlListSize + " size :: " + size);
				if (wtrLvlListSize >= size) {
					int defaultWtrLvl = waterLevelSeriesList.get(wtrLvlListSize - 1).getWaterLevel();
					int waterLvl = 0;
					for (int i = 1; i < wtrLvlListSize; i++) {
						waterLvl = waterLevelSeriesList.get(i).getWaterLevel();
						if (waterLvl <= defaultWtrLvl) {
							triggerCount++;
							if (triggerCount == size) {
								System.out.println("Increase to decrease");
								waterLevelSeries.setRuleType(2);
							}
						}
					}
				}
				triggerCount = 0;
			}
		}
		pts[0] = prevWtrLvl;
		pts[1] = triggerCount;
		return pts;
	}

//	private int[] stableToStable(int waterLevel, int[] pts, List<WaterLevelData> waterLevelSeriesList,
//			WaterLevelSeries waterLevelSeries) {
//		int prevWtrLvl = pts[0];
//		int triggerCount = pts[1];
//		if (waterLevel == prevWtrLvl) {
//			triggerCount++;
//			if (TRIGGER_THRESHOLD_SIZE == triggerCount++) {
//				int size = TRIGGER_THRESHOLD_SIZE * 2; // Double check with previous packets.
//				int wtrLvlListSize = waterLevelSeriesList.size();
//				System.out.println("waterLvlListSize :: " + wtrLvlListSize + " size :: " + size);
//				if (wtrLvlListSize >= size) {
//					int defaultWtrLvl = waterLevelSeriesList.get(wtrLvlListSize - 1).getWaterLevel();
//					int waterLvl = 0;
//					for (int i = 1; i < wtrLvlListSize; i++) {
//						waterLvl = waterLevelSeriesList.get(i).getWaterLevel();
//						if (waterLvl <= defaultWtrLvl) {
//							triggerCount++;
//							if (triggerCount == size) {
//								System.out.println("Stable to stable");
//								waterLevelSeries.setRuleType(0);
//							}
//						}
//					}
//				}
//			}
//		}
//		pts[0] = prevWtrLvl;
//		pts[1] = triggerCount;
//		return pts;
//	}

//	private int[] stableToStable(int waterLevel, int[] pts) {
//		int prevWtrLvl = pts[0];
//		int triggerCount = pts[1];
//		if (this.ruleType == 3 && waterLevel == prevWtrLvl) {
//			triggerCount++;
//			if (TRIGGER_THRESHOLD_SIZE == triggerCount) {
//				System.out.println("stable to stable");
//				triggerCount = 0;
//				this.ruleType = 3;
//			}
//		}
//		pts[0] = prevWtrLvl;
//		pts[1] = triggerCount;
//		return pts;
//	}

//	if(wlList.get(i) > temp) {
//		temp=wlList.get(i);
//		triggerCount++;
//		if(THRESHOLD == triggerCount) {
//			System.out.println("Trigger motor");
//			triggerCount = 0;
//			waterStarted = true;
//		}
//	}

	private long getThresholdTime(long ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts);
		cal.add(Calendar.MINUTE, THRESHOLD_TIME);
		return cal.getTimeInMillis();
	}

	private void testIncrease(WaterLevelSeries waterLevelSeries, Analytics_Copy obj) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -40);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -39);
		waterLevelSeries.add(new WaterLevelData(110, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -38);
		waterLevelSeries.add(new WaterLevelData(120, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -37);
		waterLevelSeries.add(new WaterLevelData(130, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -36);
		waterLevelSeries.add(new WaterLevelData(140, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -35);
		waterLevelSeries.add(new WaterLevelData(150, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -34);
		waterLevelSeries.add(new WaterLevelData(160, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -33);
		waterLevelSeries.add(new WaterLevelData(170, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -32);
		waterLevelSeries.add(new WaterLevelData(180, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -31);
		waterLevelSeries.add(new WaterLevelData(190, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		waterLevelSeries.add(new WaterLevelData(200, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -29);
		waterLevelSeries.add(new WaterLevelData(210, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -28);
		waterLevelSeries.add(new WaterLevelData(220, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -27);
		waterLevelSeries.add(new WaterLevelData(230, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());
	}

	private void testIncreaseToDecrease(WaterLevelSeries waterLevelSeries, Analytics_Copy obj) throws Exception {
		Calendar cal = Calendar.getInstance();
		System.out.println(waterLevelSeries.getWaterLevelSeries().toString());
		System.out.println(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, -26);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -25);
		System.out.println(cal.getTimeInMillis());
		waterLevelSeries.add(new WaterLevelData(110, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -24);
		waterLevelSeries.add(new WaterLevelData(120, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -23);
		waterLevelSeries.add(new WaterLevelData(130, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -22);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -21);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -20);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -19);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -18);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -17);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -16);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -15);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -14);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());

		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -13);
		waterLevelSeries.add(new WaterLevelData(10, cal.getTimeInMillis()));
		obj.analyzeWaterLevelSeries(waterLevelSeries, cal.getTimeInMillis());
	}

	public static void main(String[] args) throws Exception {
		Analytics_Copy obj = new Analytics_Copy();
		WaterLevelSeries ws = new WaterLevelSeries();
		ws.setRuleType(0);

		obj.testIncrease(ws, obj);

		System.out.println("\n\n---- The End of Water level rise ----\n\n ");

		obj.testIncreaseToDecrease(ws, obj);

//	TT 1612731399679
//	Last Packet 1612732596945
//	Latest TS 1612732299679

//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(1612732299679L);
//		cal.add(Calendar.MINUTE, -15);
//		System.out.println(cal.getTimeInMillis());

	}

}


```
