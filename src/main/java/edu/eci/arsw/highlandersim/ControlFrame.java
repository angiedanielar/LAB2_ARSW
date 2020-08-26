package edu.eci.arsw.highlandersim;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JScrollBar;

public class ControlFrame extends JFrame {

    private static final int DEFAULT_IMMORTAL_HEALTH = 100;
    private static final int DEFAULT_DAMAGE_VALUE = 10;
    private JPanel contentPane;
    private List<Immortal> immortals;
    private JTextArea output;
    private JLabel statisticsLabel;
    private JScrollPane scrollPane;
    private JTextField numOfImmortals;

    public void start(){
        immortals = setupInmortals();
        if (immortals != null) {
            for (Immortal i : immortals) {
                i.start();
            }
        }
    }

    public void pause(){
        for (Immortal i : immortals) {
            i.pauseImmortal();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void reiniciar(){
        for (Immortal im : immortals) {
            im.resumeImmortal();
        }
    }

    public void detener(){
        for (Immortal im: immortals){
            im.stopImmortal();
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ControlFrame frame = new ControlFrame();
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
    public ControlFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 647, 248);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JToolBar toolBar = new JToolBar();
        contentPane.add(toolBar, BorderLayout.NORTH);

        final JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                immortals = setupInmortals();

                if (immortals != null) {
                    for (Immortal im : immortals) {
                        im.start();
                    }
                }                
                btnStart.setEnabled(false);

            }
        });
        toolBar.add(btnStart);

        JButton btnPauseAndCheck = new JButton("Pause and check");
        btnPauseAndCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                for(Immortal im: immortals) {
                    im.pauseImmortal();
                }
                
                int sum = 0;
                for (Immortal im : immortals) {
                    sum += im.getHealth();
                }

                statisticsLabel.setText("<html>"+immortals.toString()+"<br>Health sum:"+ sum);
                
                

            }
        });
        toolBar.add(btnPauseAndCheck);

        JButton btnResume = new JButton("Resume");

        btnResume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized (immortals){
                    immortals.notifyAll();
                }
                for(Immortal im: immortals){
                    im.resumeImmortal();
                }

            }
        });

        toolBar.add(btnResume);

        JLabel lblNumOfImmortals = new JLabel("num. of immortals:");
        toolBar.add(lblNumOfImmortals);

        numOfImmortals = new JTextField();
        numOfImmortals.setText("3");
        toolBar.add(numOfImmortals);
        numOfImmortals.setColumns(10);

        JButton btnStop = new JButton("STOP");
        btnStop.setForeground(Color.RED);
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                for(Immortal im: immortals){
                    im.stopImmortal();
                }                
            }
        
        });
        toolBar.add(btnStop);

        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        output = new JTextArea();
        output.setEditable(false);
        scrollPane.setViewportView(output);
        
        
        statisticsLabel = new JLabel("Immortals total health:");
        contentPane.add(statisticsLabel, BorderLayout.SOUTH);

    }

    public List<Immortal> setupInmortals() {

        ImmortalUpdateReportCallback ucb=new TextAreaUpdateReportCallback(output,scrollPane);
        
        try {
            int ni = Integer.parseInt(numOfImmortals.getText());

            List<Immortal> il = new LinkedList<Immortal>();

            for (int i = 0; i < ni; i++) {
                Immortal i1 = new Immortal("im" + i, il, DEFAULT_IMMORTAL_HEALTH, DEFAULT_DAMAGE_VALUE,ucb);
                il.add(i1);
            }
            return il;
        } catch (NumberFormatException e) {
            JOptionPane.showConfirmDialog(null, "Número inválido.");
            return null;
        }

    }

    public int getDefaultImmortalHealth() {
        return DEFAULT_IMMORTAL_HEALTH;
    }

    public int getDefaultDamageValue() {
        return DEFAULT_DAMAGE_VALUE;
    }

    public List<Immortal> getImmortals() {
        return immortals;
    }

    public void setImmortals(List<Immortal> immortals) {
        this.immortals = immortals;
    }

    public int getNumOfImmortals() {
        return Integer.parseInt(numOfImmortals.getText());
    }

    public void setNumOfImmortals(JTextField numOfImmortals) {
        this.numOfImmortals = numOfImmortals;
    }

    public boolean isStop() {
        return immortals.get(0).isStop();
    }
}

class TextAreaUpdateReportCallback implements ImmortalUpdateReportCallback{

    JTextArea ta;
    JScrollPane jsp;
    private List<Immortal> immortals;

    public TextAreaUpdateReportCallback(JTextArea ta,JScrollPane jsp) {
        this.ta = ta;
        this.jsp=jsp;
    }       
    
    @Override
    public void processReport(String report) {
        ta.append(report);

        //move scrollbar to the bottom
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JScrollBar bar = jsp.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            }
        }
        );

    }

}