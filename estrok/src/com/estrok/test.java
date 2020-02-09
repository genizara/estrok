package com.estrok;

import java.io.IOException;
import java.util.Scanner;

class test {

	
	public static void main(String[] args) throws IOException {
		int a[][] = null;
        Scanner scan = new Scanner(System.in);
        
        int b = scan.nextInt();
        scan.close();
        a = new int[b][];
        String message = null;
        for(int i =0; i < b;i++) {
        	message = scan.nextLine();
        	a[i] = parser(message);
        }
        
        for(int[] k:a) {
        	for(int c:k) {
        		System.out.print(c);
        	}
        }
        System.out.println();
	}
	
	private static int[] parser(String p) {
		int l = p.length();
		int[] r = new int[l];
		for(int i = 0; i < l; i ++ ) {
			if('0'==p.charAt(i)) {
				r[i] = 0;
			} else {
				r[i] = 1;
			}
		}
		return  r;
	}
}
