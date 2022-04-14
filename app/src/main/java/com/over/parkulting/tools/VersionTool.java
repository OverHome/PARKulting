package com.over.parkulting.tools;


import java.util.Scanner;

public class VersionTool {
    public static int comparisonVersion(String ver1, String ver2){
        Scanner s1 = new Scanner(ver1);
        Scanner s2 = new Scanner(ver2);
        s1.useDelimiter("\\.");
        s2.useDelimiter("\\.");

        while(s1.hasNextInt() && s2.hasNextInt()) {
            int v1 = s1.nextInt();
            int v2 = s2.nextInt();
            if(v1 < v2) {
                return -1;
            } else if(v1 > v2) {
                return 1;
            }
        }

        if(s1.hasNextInt()) return 1;
        return 0;
    }
}
