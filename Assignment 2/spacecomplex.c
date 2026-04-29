#include <stdio.h>
#include <stdlib.h>

/* ---------------------------------------------------
   O(1) AUXILIARY SPACE
   Uses only a few variables regardless of input size
--------------------------------------------------- */
int constantSpaceSum(int arr[], int n, size_t *auxMem)
{
    int sum = 0;
    *auxMem = 0;   // No extra memory allocated

    for (int i = 0; i < n; i++)
        sum += arr[i];

    return sum;
}


/* ---------------------------------------------------
   O(n) AUXILIARY SPACE
   Creates a copy of the array → extra memory grows
   linearly with input size
--------------------------------------------------- */
int linearSpaceCopy(int arr[], int n, size_t *auxMem)
{
    int *copy = malloc(n * sizeof(int));
    if (!copy) {
        printf("Memory allocation failed!\n");
        return -1;
    }

    *auxMem = n * sizeof(int);

    for (int i = 0; i < n; i++)
        copy[i] = arr[i];

    int sum = 0;
    for (int i = 0; i < n; i++)
        sum += copy[i];

    free(copy);
    return sum;
}


/* ---------------------------------------------------
   O(n²) AUXILIARY SPACE
   Creates an n × n matrix in heap memory
--------------------------------------------------- */
int quadraticSpaceMatrix(int n, size_t *auxMem)
{
    int **matrix = malloc(n * sizeof(int *));
    if (!matrix) {
        printf("Memory allocation failed!\n");
        return -1;
    }

    for (int i = 0; i < n; i++) {
        matrix[i] = malloc(n * sizeof(int));
        if (!matrix[i]) {
            printf("Memory allocation failed!\n");
            return -1;
        }
    }

    *auxMem = (n * sizeof(int *)) + (n * n * sizeof(int));

    int count = 0;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            matrix[i][j] = count++;

    for (int i = 0; i < n; i++)
        free(matrix[i]);
    free(matrix);

    return count;
}


/* ---------------------------------------------------
   Helper function for clean output
--------------------------------------------------- */
void printReport(const char *title, size_t memory, const char *complexity)
{
    printf("=== %s ===\n", title);
    printf("Auxiliary Memory Used : %zu bytes\n", memory);
    printf("Space Complexity      : %s\n\n", complexity);
}


/* ---------------------------------------------------
   MAIN DRIVER
--------------------------------------------------- */
int main()
{
    int n = 10000;
    size_t auxMem;

    int *arr = malloc(n * sizeof(int));
    if (!arr) return 1;

    for (int i = 0; i < n; i++)
        arr[i] = i;

    /* O(1) */
    constantSpaceSum(arr, n, &auxMem);
    printReport("Constant Space Example", auxMem, "O(1)");

    /* O(n) */
    linearSpaceCopy(arr, n, &auxMem);
    printReport("Linear Space Example", auxMem, "O(n)");

    /* O(n²) */
    quadraticSpaceMatrix(1000, &auxMem);
    printReport("Quadratic Space Example (n = 1000)", auxMem, "O(n^2)");

    free(arr);
    return 0;
}
