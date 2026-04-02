package leetcode;

public class L_134_Gas_Station {
    public int canCompleteCircuit(int[] gas, int[] cost) {

        int start=0,currGain=0,tank=0,totalGain=0;

        for(int i=0;i<gas.length;i++){

            currGain = gas[i]-cost[i];

            totalGain+=currGain;
            tank+=currGain;

            if(tank<0){
                start=i+1;
                tank=0;
            }

        }

        return totalGain>=0?start:-1;

    }
}
