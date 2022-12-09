public class test {
    public static void main(String arg[]) {

        String sentence="1.90888383828298838+1.99";
        double num= convert(sentence,0);
        System.out.print(num+11);


    }

    private static double convert(String s,int p) {
        double num=0.0;
        String number="";
        for (int i=p;i<s.length(); i++){
            if (Character.isDigit(s.charAt(i))||s.charAt(i)=='.'){
                number=number+s.charAt(i);
            }
            else if (Character.isDigit(s.charAt(i))==false){
                num=Double.parseDouble(number);
                return num;
                }
            }
            return num;
    }


}
