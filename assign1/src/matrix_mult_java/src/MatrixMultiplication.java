public class MatrixMultiplication {
    public void onMult(int size) {
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];
        double[][] c = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = 1.0;
                b[i][j] = i + 1.0;
            }
        }

        long startTime = System.nanoTime();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double temp = 0;
                for (int k = 0; k < size; k++) {
                    temp += a[i][k] * b[k][j];
                }
                c[i][j] = temp;
            }
        }

        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1.0e9;
        System.out.printf("Time: %.3f seconds%n", elapsedTime);

        System.out.print("Result matrix (first 10 elements): ");
        for (int j = 0; j < Math.min(10, size); j++) {
            System.out.print(c[0][j] + " ");
        }
        System.out.println();
    }

    public void onMultLine(int size) {
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];
        double[][] c = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = 1.0;
                b[i][j] = i + 1.0;
            }
        }

        long startTime = System.nanoTime();

        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                double temp = a[i][k];
                for (int j = 0; j < size; j++) {
                    c[i][j] += temp * b[k][j];
                }
            }
        }

        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1.0e9;
        System.out.printf("Time: %.3f seconds%n", elapsedTime);

        System.out.print("Result matrix (first 10 elements): ");
        for (int j = 0; j < Math.min(10, size); j++) {
            System.out.print(c[0][j] + " ");
        }
        System.out.println();
    }
}
