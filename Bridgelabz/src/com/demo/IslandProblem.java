package com.demo;

public class IslandProblem {
    static int[][] map = { { 0, 0, 0, 0, 0 }
    ,			   { 1, 0, 0, 1, 0 },
    			   { 0, 0, 0, 0, 0 },
    			   { 1, 0, 0, 1, 1 }  };
    static int count = 0;

    public static void main(String[] args) {

	int m = map.length;
	int n = map[0].length;

	System.out.println("M = " + map.length);
	System.out.println("N = " + map[0].length);

	if (m < 3 || n < 3) {
	    System.out.println("0");
	    return;
	}
for (int i = 0; i < map.length; i++) {
    
    
    for (int j = 0; j <map[i].length; j++) {
	
	System.out.print(map[i][j]+" ");
	
    }
    
    System.out.println();
}
	for (int row = 1; row < map.length - 1; row++) {

	    for (int col = 1; col < map[row].length - 1; col++) {

		int item = map[row][col];
		if (item == map[row][col - 1] || item == map[row - 1][col - 1] || item == map[row - 1][col] || item == map[row + 1][col-1]) {
		}

		else {
			count++;

		    
		}		
		
	    }
	  

	}

	System.out.println("Total is : " + count);

    }

}
