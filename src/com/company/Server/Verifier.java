package com.company.Server;

public class Verifier {
    public static boolean validLogin(String str){
        return str.matches("[a-z]{1,4}");
    }
}
