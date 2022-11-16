package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {

    /* ticket pricing policy: if the cinema hall has 60 seats or fewer, every ticket costs TICKET_PRICE_EXPENSIVE,
    otherwise only seats in the first half of rows have this price and the other tickets cost TICKET_PRICE_CHEAP */
    private static final int TICKET_PRICE_EXPENSIVE = 10;
    private static final int TICKET_PRICE_CHEAP = 8;

    // takes a cinema hall (as 2-dim char array) as input, then prints it with row and seat numbers
    private static void prettyPrint(char[][] cinemaHall) {
        System.out.print("Cinema:\n  ");
        for (int j = 0; j < cinemaHall[0].length; j++) {
            System.out.printf("%d ", j + 1);
        }
        System.out.print("\n");
        for (int i = 0; i < cinemaHall.length; i++) {
            System.out.printf("%d ", i + 1);
            for (int j = 0; j < cinemaHall[0].length; j++) {
                System.out.printf("%c ", cinemaHall[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // obtain number of rows and seats in cinema hall from user
        System.out.println("Enter the number of rows:");
        int rows = sc.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seats = sc.nextInt();
        if (rows < 0 || seats < 0) {
            throw new IllegalArgumentException("Numbers of rows and seats cannot be negative.");
        }

        // create cinema hall in the according size, seats are empty ('S') at first
        char[][] cinemaHall = new char[rows][seats];
        for (char[] chars : cinemaHall) {
            Arrays.fill(chars, 'S');
        }

        // some values for statistics
        int ticketsSold = 0;
        int income = 0;
        int firstHalf = rows / 2; // number of rows in the first half
        int maxIncome = rows * seats <= 60 ? rows * seats * TICKET_PRICE_EXPENSIVE :
                (firstHalf * TICKET_PRICE_EXPENSIVE + (rows - firstHalf) * TICKET_PRICE_CHEAP) * seats;

        boolean mainLoopActive = true;
        while (mainLoopActive) {
            // show list of possible action and ask for the next action
            System.out.println("1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit");
            int action = sc.nextInt();

            switch (action) {
                case 1:  // "1. Show the seats"
                    prettyPrint(cinemaHall);
                    break;
                case 2:  // "2. Buy a ticket"
                    if (ticketsSold == rows * seats) {
                        System.out.println("Sorry, all seats have already been sold.");
                        break;
                    }

                    int desiredRow = 0;
                    int desiredSeat = 0;
                    boolean promptSeatLoopActive = true;
                    while (promptSeatLoopActive) {
                        // ask user for desired row and seat number
                        System.out.println("Enter a row number:");
                        desiredRow = sc.nextInt();
                        System.out.println("Enter a seat number in that row:");
                        desiredSeat = sc.nextInt();

                        // repeat prompt if input values out of bounds
                        if (desiredRow <= 0 || desiredRow > rows || desiredSeat <= 0 || desiredSeat > seats) {
                            System.out.println("Wrong input!");
                            continue;
                        }

                        // repeat prompt if desired seat is already taken
                        if (cinemaHall[desiredRow-1][desiredSeat-1] != 'S') {
                            System.out.println("That ticket has already been purchased!");
                            continue;
                        }

                        promptSeatLoopActive = false;
                    }

                    cinemaHall[desiredRow-1][desiredSeat-1] = 'B';  // mark seat as reserved
                    ticketsSold++;  // increment counter

                    // determine ticket price according to rules above
                    int ticketPrice;
                    if (rows * seats <= 60) {
                        ticketPrice = TICKET_PRICE_EXPENSIVE;
                    } else {
                        ticketPrice = desiredRow <= firstHalf ? TICKET_PRICE_EXPENSIVE : TICKET_PRICE_CHEAP;
                    }

                    System.out.printf("Ticket price: $%d\n\n", ticketPrice);  // print ticket price
                    income += ticketPrice;  // add ticket price to income
                    break;
                case 3:
                    System.out.printf("Number of purchased tickets: %d\n", ticketsSold);
                    System.out.printf("Percentage: %1.2f%%\n", ticketsSold * 100.0 / (seats * rows));
                    System.out.printf("Current income: $%d\n", income);
                    System.out.printf("Total income: $%d\n", maxIncome);
                    break;
                case 0:  // "0. Exit"
                    mainLoopActive = false;
                    break;
                default:
                    System.err.println("Unknown action!");
            }
        }
    }
}