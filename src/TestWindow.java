import javax.swing.*;
import java.awt.*;
public class TestWindow extends JWindow{
    public TestWindow(){
        super();
        this.setSize(500, 400);
//        this.setLocation(50, 50);
        this.setLocationRelativeTo(null);

        setBackground(new Color(0,0,0,0)); // this is the critical line - that fourth 0 represents alpha (or opacity)

        setAlwaysOnTop( true );  // keeps it in the foreground so you don't click away from it - note that clicks on the transparent part DO pass through to the desktop, at least on Lion

        JLabel testLabel = new JLabel("Floating text hah");
        this.add(testLabel);

    }
    public static void main(String[] args){
        System.out.println("Sup");
        TestWindow t = new TestWindow();
        
        t.setVisible(true);
    }
}