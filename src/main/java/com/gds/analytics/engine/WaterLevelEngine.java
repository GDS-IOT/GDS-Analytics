package com.gds.analytics.engine;

import com.gds.analytics.domain.WaterLevelEvent;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Sujith Ramanathan
 */
@Component
public class WaterLevelEngine extends BackgroundEngine<WaterLevelEvent> {

    private List<Integer> wlList;

    /** Default value will be 1 which will check for increase to stable **/
    private volatile int ruleType = 0;
    private static final int THRESHOLD = 3;

    private static final Logger LOGGER = Logger.getLogger(WaterLevelEngine.class);

    public WaterLevelEngine() {
        this.wlList = new ArrayList<Integer>();
    }

    @Override
    public void analyze(WaterLevelEvent waterLevelEvent) {
        this.threadName="WaterLevelEngineThread";
        addAndAnalyze(waterLevelEvent.getWaterLevelPercentage());
    }

    private void addAndAnalyze(int waterLevel) {
        if(wlList.size()<10) {
            wlList.add(waterLevel);
        }else {
            for(int i=0;i<10;i++ ) {
                if(i < 9) {
                    wlList.set(i, wlList.get(i+1));
                }else {
                    wlList.set(i, waterLevel);
                }
            }
        }
        triggerOutput();
    }

    public void triggerOutput() {
        int size = wlList.size();
        if(size > THRESHOLD) {
            int startIndex = (size-THRESHOLD);
            int prevWtrLvl = wlList.get(startIndex-1);
            int []pts = {prevWtrLvl, 0};
            for(int i=startIndex;  i< size; i++) {
                System.out.println("i = "+i+" value = "+wlList.get(i)+" prevWaterLevel = "+pts[0]);
                pts = analyzeOutput(wlList.get(i), pts);
            }
        }
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
}


