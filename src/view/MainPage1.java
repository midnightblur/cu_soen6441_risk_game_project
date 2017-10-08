package view;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class MainPage1 extends JFrame implements ActionListener {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainPage1 frame = new MainPage1();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainPage1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnplay = new JButton("Play");
        btnplay.setBounds(163, 33, 97, 25);

        contentPane.add(btnplay);

        JButton btneditmap = new JButton("Edit Map");
        btneditmap.setBounds(163, 93, 97, 25);
        btneditmap.addActionListener(this);
        btneditmap.setActionCommand("Open");


        contentPane.add(btneditmap);

        JButton btnquit = new JButton("Quit");
        btnquit.setBounds(163, 158, 97, 25);
        contentPane.add(btnquit);
        btnquit.addActionListener(new CloseListener());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String cmd = e.getActionCommand();
        if(cmd.equals("Open")) {
            dispose();
            new MapEditor1();
        }

    }

    private class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
