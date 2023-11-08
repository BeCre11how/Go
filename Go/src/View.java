import javax.swing.*;


class View extends JFrame {

    public void build(int z) {
        setSize(1000, 800);
        setContentPane(new Game(z));
        setVisible(true);

    }

}