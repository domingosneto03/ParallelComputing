package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MatrixMultiplication matrixMultiplication = new MatrixMultiplication();
        int choice;

        do {
            System.out.println("\n1. Multiplication");
            System.out.println("2. Line Multiplication (Not Implemented)");
            System.out.println("3. Block Multiplication (Not Implemented)");
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
                    System.out.println("Line Multiplication is not implemented.");
                    break;
                case 3:
                    System.out.println("Block Multiplication is not implemented.");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }
}