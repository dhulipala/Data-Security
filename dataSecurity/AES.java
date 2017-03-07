package dataSecurity;

public class AES {

	static final int[] roundConstant = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36 };

	static final int[][] mixColumnConstant = new int[][] {
			//
			{ 0x02, 0x03, 0x01, 0x01 }, //
			{ 0x01, 0x02, 0x03, 0x01 }, //
			{ 0x01, 0x01, 0x02, 0x03 }, //
			{ 0x03, 0x01, 0x01, 0x02 } //
	};

	static int[][] toMatrix(int[] input) {
		int[][] result = new int[4][4];
		for (int i = 0; i < 16; i++) {
			result[i % 4][i / 4] = input[i];
		}
		return result;
	}

	static void print(int[][] input) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(String.format("%02X", input[i][j]) + " ");
			}
			System.out.println();
		}
	}

	static int[][] subBytes(int[][] input) {
		int[][] result = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result[i][j] = SBOX(input[i][j]);
			}
		}
		return result;
	}

	static int SBOX(int n) {
		return AESConstants.sbox[n % 0xFF];
	}

	static int[][] shiftRows(int[][] input) {
		int[][] result = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int shift = (j + i) % 4;
				result[i][j] = input[i][shift];
			}
		}
		return result;
	}

	static int[][] mixColums(int[][] input) {
		int[][] result = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int sum = 0;
				for (int k = 0; k < 4; k++) {
					int product = multiplygf(mixColumnConstant[i][k], input[k][j]);
					sum = sum ^ product;
				}
				result[i][j] = sum;
			}
		}
		return result;
	}

	static int multiplygf(int i, int j) {
		if (i == 1) {
			return j;
		}
		if (i == 2) {
			return AESConstants.gf2[j];
		}
		if (i == 3) {
			return AESConstants.gf3[j];
		}
		return 0;
	}

	static int[][] roundkey(int[][] key, int roundConstant) {
		int[][] newkey = new int[4][4];
		// Calculate T
		int[] T = new int[] { //
		SBOX(key[1][3]) ^ roundConstant, //
				SBOX(key[2][3]), //
				SBOX(key[3][3]), //
				SBOX(key[0][3]) //
		};
		// Apply T
		for (int col = 0; col < 4; col++) {
			for (int row = 0; row < 4; row++) {
				newkey[row][col] = key[row][col] ^ T[row];
			}
			// Column will become new T
			for (int row = 0; row < 4; row++) {
				T[row] = newkey[row][col];
			}
		}
		return newkey;
	}
	
	//static int[][] invRoundKey(int[][] roundkey, int )

	private static int[][] addroundkey(int[][] input1, int[][] input2) {
		int[][] result = new int[4][4];
		for (int col = 0; col < 4; col++) {
			for (int row = 0; row < 4; row++) {
				result[col][row] = input1[col][row] ^ input2[col][row];
			}
		}
		return result;
	}

	public static void main(String[] args) {

		 int[] textArray = new int[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
//		int[] textArray = new int[] { 0x54, 0x77, 0x6F, 0x20, 0x4F, 0x6E, 0x65, 0x20, 0x4E, 0x69, 0x6E, 0x65, 0x20, 0x54, 0x77, 0x6F };

		 int[] keyArray = new int[] { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 };
//		int[] keyArray = new int[] { 0x54, 0x68, 0x61, 0x74, 0x73, 0x20, 0x6D, 0x79, 0x20, 0x4B, 0x75, 0x6E, 0x67, 0x20, 0x46, 0x75 };

		int[][][] roundkey = new int[11][][];
		int y = SBOX(1);
		roundkey[0] = toMatrix(keyArray);
		System.out.println("Round key 0");
		print(roundkey[0]);

		for (int i = 1; i < 11; i++) {
			roundkey[i] = roundkey(roundkey[i - 1], roundConstant[i - 1]);
			System.out.println("Round key " + i);
			print(roundkey[i]);
		}

		int[][] stateMatrix = toMatrix(textArray);
		System.out.println("State matrix");
		print(stateMatrix);

		stateMatrix = addroundkey(stateMatrix, roundkey[0]);
		System.out.println("After add roundkey " + 0);
		print(stateMatrix);

		for (int i = 1; i < 11; i++) {

			stateMatrix = subBytes(stateMatrix);
			System.out.println("After subbyte");
			print(stateMatrix);

			stateMatrix = shiftRows(stateMatrix);
			System.out.println("After shiftRows");
			print(stateMatrix);

			if (i < 10) {
				// No mix columns in last round
				stateMatrix = mixColums(stateMatrix);
				System.out.println("After mixColums");
				print(stateMatrix);
			}

			stateMatrix = addroundkey(stateMatrix, roundkey[i]);
			System.out.println("After add roundkey " + i);
			print(stateMatrix);

		}

	}

}
