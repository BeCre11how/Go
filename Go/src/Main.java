import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter an Integer");
        int k = scanner.nextInt();
        View view = new View();
        view.build(k);
    }
}
