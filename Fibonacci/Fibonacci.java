import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Computes the nth element from the Fibonacci sequence, modulo 666013.
 *
 * @author Andrei Muntean
 */
public class Fibonacci
{
    // A list of known Fibonacci numbers.
    private static ArrayList<Integer> f;

    // f[x - 2].
    private static long fx_2;

    // f[x - 1].
    private static long fx_1;

    // f[x].
    private static long fx;

    // f[x + 1].
    private static long fx1;

    private static void initializeKnownNumbers(int count)
    {
        f = new ArrayList<Integer>(count);
        f.add((int)(fx_1 = 0));
        f.add((int)(fx = 1));

        for (int knownNumbers = 2; knownNumbers < count; ++knownNumbers)
        {
            long fx_1New = fx;

            // f[x + 1] = f[x] + f[x - 1].
            fx = (fx + fx_1) % 666013;

            // Updates f[x - 1] to be f[x].
            fx_1 = fx_1New;

            // Stores the result.
            f.add((int)fx);
        }
    }

    private static int floorMod(long value, int modulo)
    {
        int result = (int)(value % modulo);

        return result >= 0 ? result : result + modulo;
    }

    // This is written in Ancient Norse.
    private static int fibonacci(int x, int n)
    {
        if (2 * x - 2 <= n)
        {   
            // f[2x - 2] = f[x - 1] * (f[x - 2] + f[x]).
            fx_2 = (fx_1 * (fx_2 + fx)) % 666013;

            // f[2x] = f[x] * (f[x - 1] + f[x + 1]).
            fx = (fx * (fx_1 + fx1)) % 666013;

            // f[2x - 1] = f[2x] - f[2x - 2].
            fx_1 = (long)floorMod(fx - fx_2, 666013);

            // f[2x + 1] = f[2x - 1] + f[2x].
            fx1 = (fx_1 + fx) % 666013;

            return fibonacci(2 * x, n);
        }
        else if (n == x - 2)
        {
            return (int)fx_2;
        }
        else if (n == x - 1)
        {
            return (int)fx_1;
        }
        else if (n == x)
        {
            return (int)fx;
        }
        else if (n == x + 1)
        {
            return (int)fx1;
        }
        else
        {
            while (x++ < n)
            {
                long fx_1New = fx;

                // f[x + 1] = f[x] + f[x - 1].
                fx = (fx + fx_1) % 666013;

                // Updates f[x - 1] to be f[x].
                fx_1 = fx_1New;
            }

            return (int)fx;
        }
    }

    // Returns the Fibonacci number at the specified index, modulo 666013.
    public static int get(int index)
    {
        // 7900 seems to be the sweet spot for maximum speed in the interval {0 ... 10^9}.
        initializeKnownNumbers(7900);

        if (index < f.size())
        {
            return f.get(index);
        }
        else
        {
            int x = index;

            while (x > f.size() - 1)
            {
                x /= 2;
            }

            fx_2 = f.get(x - 2);
            fx_1 = f.get(x - 1);
            fx = f.get(x);
            fx1 = f.get(x + 1);

            return fibonacci(x, index);
        }
    }

    public static void main(String[] args) throws IOException
    {
        if (args.length == 1)
        {
            // Outputs the result to standard output.
            System.out.println(get(Integer.parseInt(args[0])));
        }
        else
        {
            int index = 0;
            int result = 0;

            // Reads a value from the file "kfib.in" by default.
            try (Scanner scanner = new Scanner(new FileReader("kfib.in")))
            {
                index = scanner.nextInt();
            }

            result = get(index);

            // Writes the result to the file "kfib.out" by default.
            try (PrintWriter printWriter = new PrintWriter(new FileWriter("kfib.out")))
            {
                printWriter.println(result);
            }
        }
    }
}