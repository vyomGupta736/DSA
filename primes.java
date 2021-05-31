import java.util.*;
import java.io.*;
public class primes
{
	public static ArrayList<Integer> primeList(int n)
	{
		boolean isPrime[] = new boolean[n+1]; // sieve of eratosthenes.
		for(int i=0;i<n+1;i++)
		{
			isPrime[i] = true;
		}
        int start = 2;
        isPrime[2]=true;
        while(start*start <= n)
        {
        	if(isPrime[start])
        	{
	             int i=start*start;
	             while(i<=n)
	             {
	             	isPrime[i] = false;
	             	i=i+start;
	             }
        	}
            start++;
        }
        ArrayList<Integer> al = new ArrayList<>();

		for(int i=0;i<isPrime.length;i++)
		{
			if(!(i==0 || i==1))
			{
				if(isPrime[i] == true)
				{
					al.add(i);
				}
			}
		}
        return al;
	}

	public static int maxFactor(int n)
     {
         int max = 1;
         boolean isPrime=true;
         for(int i=2; i*i <= n;i++)
         {
             if(n%i == 0)
             {
                 isPrime=false;
                 max = Math.max(max, Math.max(i, n/i));
             }
         }
         if(isPrime)
         {
             return n;
         }
         else
         {
             return max;
         }
     }

	public static void main(String args[])throws Exception
	{
		
		System.out.println(maxFactor(966514));
		// PriorityQueue<Integer> prq = new PriorityQueue<>((a,b)->b-a);
		// InputStreamReader r=new InputStreamReader(System.in);
		// BufferedReader br=new BufferedReader(r);
		// System.out.println("Enter your name");    
  //       String name=br.readLine();
  //       System.out.println("Enter your age");    
  //       int age=Integer.parseInt(br.readLine());
  //       System.out.println(name + ", "+ (age));
		// Scanner in = new Scanner(System.in);
		// primes ob = new primes();

		// int n = in.nextInt();

		// ArrayList<Integer> al = primeList(n);

		// for(int i=0;i<al.size();i++)
		// {
		// 	System.out.print(al.get(i)+" ");
		// }
         // String s = "abcdefg";
         // System.out.println(s.substring(0, s.length()-5));
	}
}