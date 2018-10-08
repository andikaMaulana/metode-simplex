package simplex;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
public class Simplex{
	public static void main(String[] args) {
            SimplexGui sg=new SimplexGui();  
            sg.addTable();
            sg.setResizable(false);
            sg.setVisible(true);
            
	}
}
