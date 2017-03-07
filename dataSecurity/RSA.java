package cs_project;
import java.math.BigInteger;
import java.security.SecureRandom;
 
public class RSA
{
    public static void main(String[] args)
    {
        // variables for timing
        long start, end;
 
        // RSA variables
        BigInteger p = null;
        BigInteger q = null;
        BigInteger phi = null;
        BigInteger e = null;
        BigInteger n = null;
        BigInteger d = null;
 
        // Random
        SecureRandom secure_random = new SecureRandom();
 
        // Start timer
        start = System.nanoTime();
 
        boolean pq_selected = false;
        while (pq_selected == false)
        {
 
            boolean primes_generated = false;
            while (primes_generated == false)
            {
 
                // BigInteger(int bitLength, int certainty, Random rnd)
                // Create random p and q that are certainly prime using secure_random
                p = new BigInteger(1024, 1, secure_random);
                q = new BigInteger(1024, 1, secure_random);
 
                // Find difference between p and q
                BigInteger difference = p.subtract(q).abs();
                BigInteger minimum_difference = BigInteger.valueOf(2512);
 
                // Compare the difference with minimum difference
                if (difference.compareTo(minimum_difference) == 1)
                {
                    // Select p and q only if their difference is large enough
                    primes_generated = true;
                }
                else
                {
                    // Repeat loop
                	// Generate different p, q
                }
            }
 
            // Primes are generated
            // Calculate n, phi and e
            n = p.multiply(q);
            phi = n.subtract(p.add(q).subtract(BigInteger.ONE));
            e = BigInteger.valueOf(65537);
 
            // Check if gcd of e and phi is 1
            BigInteger gcd = e.gcd(phi);
            if (gcd.compareTo(BigInteger.ONE) == 0)
            {
                // Select p and q only if gcd is 1
                pq_selected = true;
 
                // Calculate d
                d = e.modInverse(phi);
            }
            else
            {
                // If difference is not 1, repeat
                // Select different p, q
            }
 
        }
 
        // End timer
        end = System.nanoTime();
        System.out.println("primes selected in : " + (end - start));
        System.out.println("-------------------------------------");
 
        // Random message
        BigInteger m = new BigInteger(1024, 0, secure_random);
        System.out.println("message_original = " + m);
        System.out.println("-------------------------------------");
 
        // Encryption
        start = System.nanoTime();
        BigInteger c = m.modPow(e, n);
        end = System.nanoTime();
 
        System.out.println("message_encrypted = " + c);
        System.out.println("message encrypted in : " + (end - start));
        System.out.println("-------------------------------------");
 
        // Decryption
        start = System.nanoTime();
        BigInteger m_decrypted = c.modPow(d, n);
        end = System.nanoTime();
 
        System.out.println("message_decrypted = " + m_decrypted);
        System.out.println("message decrypted in : " + (end - start));
        System.out.println("-------------------------------------");
    }
 
}
 