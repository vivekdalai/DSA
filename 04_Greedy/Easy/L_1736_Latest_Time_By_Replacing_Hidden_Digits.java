package leetcode;

public class L_1736_Latest_Time_By_Replacing_Hidden_Digits {
    public String maximumTime(String time) {
        char h1 = time.charAt(0);
        char h2 = time.charAt(1);
        char m1 = time.charAt(3);
        char m2 = time.charAt(4);

        if(h1=='?' && h2=='?'){
            h1='2';h2='3';
        }else{
            if(h1=='?'){
                if(h2=='0' || h2=='1' || h2=='2' || h2=='3'){
                    h1='2';
                }else{
                    h1='1';
                }
            }else if (h2=='?'){
                if(h1=='0' || h1=='1'){
                    h2='9';
                }else{
                    h2='3';
                }
            }
        }


        if(m1=='?') m1='5';
        if(m2=='?') m2='9';



        StringBuilder maxTime = new StringBuilder();

        maxTime.append(h1).append(h2).append(':').append(m1).append(m2);
        return maxTime.toString();
    }
}
