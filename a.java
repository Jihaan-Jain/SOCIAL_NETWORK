import javax.swing.*;

public class a {

    public static void main(String[] args) {

        JFrame f = new JFrame("Login Form");

        JLabel l1 = new JLabel("Username:");
        JLabel l2 = new JLabel("Password:"); //sfhliuefhwusfhw

        JTextField t1 = new JTextField();
        JPasswordField t2 = new JPasswordField();  //fjheiuf
 
        JButton b = new JButton("Login");

        l1.setBounds(50,50,100,30
        t1.setBounds(150,50,150,30)

        l2.setBounds(50,100,100,30);
        t2.setBounds(150,100,150,30);

        b.setBounds(120,160,100,40);

        f.add(l1);
        f.add(t1);
        f.add(l2);
        f.add(t2);
        f.add(b)

        f.setSize(400,300);
        f.setLayout(null);
        f.setVisible(true);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}