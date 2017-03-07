package dataSecurity;

import java.util.Random;

public class RC4 {

	public static int[] keyScheduler(int[] key) {
		int[] seed = new int[256];
		for (int i = 0; i < 256; i++) {
			seed[i] = i;
		}
		int j = 0;
		for (int i = 0; i < 256; i++) {
			j = (j + seed[i] + key[i % key.length]) % 256;
			// swapping
			int temp = seed[i];
			seed[i] = seed[j];
			seed[j] = temp;
		}
		return seed;
	}

	public static int[] pseudoRgenerator(int[] seed, int numOfRandoms) {
		int[] randoms = new int[numOfRandoms];
		int count = 0;
		int i = 0;
		int j = 0;
		while (count < numOfRandoms) {
			i = (i + 1) % 256;
			j = (j + seed[i]) % 256;
			int temp = seed[i];
			seed[i] = seed[j];
			seed[j] = temp;
			int k = seed[(seed[i] + seed[j]) % 256];
			randoms[count] = k;
			count++;
		}
		return randoms;
	}
	

	public static void main(String[] args) {
		int[] counts = new int[256];
		for (int i = 0; i < 10000; i++) {
			int[] randomKey = new Random().ints(32, 0, 256).toArray();
			int[] seed = keyScheduler(randomKey);
			int[] randoms = pseudoRgenerator(seed, 20);
			counts[randoms[1]] = counts[randoms[1]] + 1;
		}
		for (int i = 0; i < counts.length; i++) {
			System.out.println(String.format("%2X", i) + ":" + counts[i]);
		}
		

	}
}
