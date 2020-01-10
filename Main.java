import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        try {
            System.out.println("Please enter the port number to run the server: ");
            int port = in.nextInt();
            System.out.println("Please enter the filename to store the logs:");
            String file = in.next();

            new UDPServer(port, file).start();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input entered, please enter a valid port number.");
        }

        in.close();
    }
}
