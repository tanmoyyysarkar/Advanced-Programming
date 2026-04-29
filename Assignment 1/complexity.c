#include <stdio.h>
#include <time.h>

// O(1) - Constant: Always takes the same amount of steps
int get_constant(int n)
{
    return (n * n) / 10;
}

// O(n) - Linear: Time grows directly with n
long get_linear(int n)
{
    long sum = 0;
    for (int i = 0; i < n; i++)
    {
        sum += i;
    }
    return sum;
}

// O(n^2) - Quadratic: Time grows exponentially (n * n)
long get_quadratic(int n)
{
    long count = 0;
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            count++;
        }
    }
    return count;
}

int main()
{
    int test_sizes[] = {100, 500, 1000, 2000, 5000, 10000};
    int num_tests = sizeof(test_sizes) / sizeof(test_sizes[0]);

    // Constant time is very fast, so we run it a 1 Cr times to see real values
    int burst_constant = 10000000;
    // To see real data we run linear and quadratic algorithm 1000 times
    int burst_standard = 1000;

    printf("\nAlgorithm Performance Comparison (Time :- sec)\n");
    printf("%-8s | %-12s | %-12s | %-12s\n", "Input(n)", "O(1)", "O(n)", "O(n^2)");
    printf("------------------------------------------------------------\n");

    for (int i = 0; i < num_tests; i++)
    {
        int n = test_sizes[i];
        clock_t start, end;

        // Test O(1)
        start = clock();
        for (int r = 0; r < burst_constant; r++)
            get_constant(n);
        end = clock();
        double time_o1 = (double)(end - start) / CLOCKS_PER_SEC;

        // Test O(n)
        start = clock();
        for (int r = 0; r < burst_standard; r++)
            get_linear(n);
        end = clock();
        double time_on = (double)(end - start) / CLOCKS_PER_SEC;

        // Test O(n^2)
        start = clock();
        for (int r = 0; r < burst_standard; r++)
            get_quadratic(n);
        end = clock();
        double time_on2 = (double)(end - start) / CLOCKS_PER_SEC;

        printf("%-8d | %-12.5f | %-12.5f | %-12.5f\n", n, time_o1, time_on, time_on2);
    }

    printf("\nBenchmark complete.\n");
    return 0;
}
