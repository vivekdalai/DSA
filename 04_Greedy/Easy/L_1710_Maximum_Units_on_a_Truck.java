package leetcode;

import java.util.Arrays;

public class L_1710_Maximum_Units_on_a_Truck {

    public static int maximumUnits(int[][] boxTypes, int truckSize) {

        Arrays.sort(boxTypes,(a,b)-> b[1]-a[1]);
        int weight=0;

        for(int i=0;i<boxTypes.length;i++){
            if(truckSize>0){
                if(truckSize>boxTypes[i][0]){
                    weight+=boxTypes[i][0]*boxTypes[i][1];
                    truckSize-=boxTypes[i][0];
                }else{
                    weight+=truckSize*boxTypes[i][1];
                    truckSize=0;
                }
            }
        }

        return weight;

    }
}
