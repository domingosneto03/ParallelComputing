import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MatrixMultiplication matrixMultiplication = new MatrixMultiplication();
        int choice;

        do {
            System.out.println("\n1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.println("0. Quit");
            System.out.print("Selection?: ");
            choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            System.out.print("Dimensions (lins=cols)? ");
            int size = scanner.nextInt();

            switch (choice) {
                case 1:
                    matrixMultiplication.onMult(size);
                    break;
                case 2:
                    matrixMultiplication.onMultLine(size);
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }
}