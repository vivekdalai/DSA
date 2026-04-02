package leetcode;

public class L_781_Rabbits_In_Forest {
    public int numRabbits(int[] answers) {

        int[] freq = new int[1001];

        for(int i=0;i< answers.length;i++){
            freq[answers[i]]++;
        }

        int ans=0;
        for(int i=0;i<1001;i++){
            if(freq[i]>0){
                ans+= (int) Math.ceil(freq[i]/(double) (i+1)) * (i+1);
            }
        }

       return ans;
    }
}
