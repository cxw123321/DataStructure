package daySix;

import java.util.regex.*;

public class RegexTest {
    public static void main(String args[]){
        String content = "x12B";
        String pattern = "[rR]\\d+[bB]";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println(isMatch);
    }
}
