package PrimeraEtapa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class xd {
   
    public static void main() {
        String regex = ".";
        Pattern p = Pattern.compile(regex);
        System.out.println(p.pattern());
        Matcher matcher = p.matcher("\n");
        System.out.println(matcher.find());
    }
}

