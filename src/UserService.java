import java.sql.Connection;
import java.util.Scanner;
public class UserService {
    private final Scanner sc = new Scanner(System.in);
    private ValidationService vs = null;
    private int id=0;
    private BookService bs=null;
    public UserService(Connection con) {
        bs=new BookService(con,"userdata");
        vs = new ValidationService(con, "userdata");
    }
    private void userpage() {
        System.out.println("Enter your choice :");
        System.out.println("1)Check Books\n2)Order book\n3)Submit book\n4)Search book\n5)your books\n6)Reset Password\n7)Exit");
        int num = sc.nextInt();
        switch (num) {
            case 1:
                bs.printBook(vs.getQuery());
                break;
            case 2:
                bs.orderBook(this.id);
                break;
            case 3:
                bs.submitbook(this.id);
                break;
            case 4:
                bs.searchBook();
                break;
            case 5:
                bs.yourbooks(this.id);
                break;
            case 6:
                vs.resetp(this.id);
                break;
            case 7:
                System.exit(1);
            default:
                System.out.println("Enter the proper number :");
        }

    }

    public void userLogin() {
        System.out.println("Enter the ID number :");
        int id = sc.nextInt();
        if(vs.doesIdExist(id)) {
            System.out.println("Enter the Pin number :");
            int pin = sc.nextInt();
            if (vs.pinCheck(id, pin)) {
                this.id = id;
                while (true) {
                    userpage();
                }
            } else {
                System.out.println("Wrong password ,please try again");
                userLogin();
            }
        }else{
            System.out.println("this id number is not exist please enter the correct ID number !!!");
        }
    }
    public void printUser(){
        bs.printUser();
    }
    public boolean removeUser(){
        System.out.println("Enter the id no for user you going to remove ");
        int id=sc.nextInt();
        if(vs.doesIdExist(id)){
        System.out.println("Are you sure to delete ? (y/n)");
        char c=sc.next().charAt(0);
        if(c=='y'||c=='Y'){
            System.out.println("Deleting.......");
            vs.RemoveOperation(id);
            return true;
        }
        else{
            System.out.println("Enter the proper choice");
            return false;
        }
        }
        else{
            System.out.println("Id is not exist");
            return false;
        }
    }
}
