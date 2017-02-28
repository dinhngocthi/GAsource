package pso.fitnessfunction;

import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Pattern;

public class CheckAllParams {

    public static void main(String[] args) {
        String content = FileUtil.readData("exprs.txt");
        String[] lines = content.split("\n");
        Hashtable<String, String> hash = new Hashtable<String, String>();
        for (String line : lines) {
            line = line.trim();
            line = line.replaceAll(Pattern.quote("["), "@@@");
            line = line.replaceAll(Pattern.quote("]F"), "@@@");
            line = line.replaceAll(Pattern.quote("]T"), "@@@");
            line = line.replaceAll(Pattern.quote("&&"), "@@@");
            line = line.replaceAll(Pattern.quote("||"), "@@@");
            
            line = line.replaceAll(Pattern.quote(">="), "@@@");
            line = line.replaceAll(Pattern.quote("<="), "@@@");
            line = line.replaceAll(Pattern.quote("!="), "@@@");
            line = line.replaceAll(Pattern.quote("=="), "@@@");
            line = line.replaceAll(Pattern.quote(">"), "@@@");
            line = line.replaceAll(Pattern.quote("<"), "@@@");
            
            line = line.replaceAll(Pattern.quote("("), "@@@");
            line = line.replaceAll(Pattern.quote(")"), "@@@");
            
            line = line.replaceAll(Pattern.quote("*"), "@@@");
            line = line.replaceAll(Pattern.quote("/"), "@@@");
            line = line.replaceAll(Pattern.quote("%"), "@@@");
            line = line.replaceAll(Pattern.quote("+"), "@@@");
            line = line.replaceAll(Pattern.quote("-"), "@@@");
            
            String[] parts = line.split("@@@");
            for (String part : parts) {
                part = part.trim();
                if (!part.isEmpty()) {
                    hash.put(part, part);
                }
            }
        }
        Set<String> keys = hash.keySet();
        for (String key : keys) {
            System.out.println(key);
        }
        /*
        xl2
        xl1
        xr2
        xr1
        y
        x
        year
        yl2
        yl1
        yr2
        status
        yr1
        income
        c
        b
        month
        a
        */
    }

}
