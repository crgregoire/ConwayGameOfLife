import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The GameOfLifeGUI class. This is a graphical user driven program created to
 * test my programming skills for a job opening.
 * 
 * @author Colin Gregoire
 */
public class GameOfLifeGUI extends WindowAdapter {

    //for randomization
    public static Random random = new Random();

    // executor for updating our petriDish
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    // actual petriDish job / task
    private Future<?> lifeFuture;

    // whether or not to keep running
    private AtomicBoolean doRun = new AtomicBoolean(false);

    private int size = 100;

    private GameOfLifeCell[][] petriDish = new GameOfLifeCell[size][size];

    private JPanel lifePane;

    /**
     * Create the GUI and show it. For thread safety, this will be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Use the Java look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // no-op
        }
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true); 
        // Instantiate the controlling class.
        JFrame frame = new JFrame("Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         // Create and set up the content pane.
        GameOfLifeGUI window = new GameOfLifeGUI();
         // Add components to it.
        Container contentPane = frame.getContentPane();
        contentPane.add(window.createLifePane(), BorderLayout.CENTER);
        contentPane.add(window.createButtonPane(), BorderLayout.PAGE_END);
         // Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setVisible(true);
    }
    
    protected JPanel createLifePane() {
        lifePane = new JPanel(new GridLayout(size, size, 2, 2));
        // create and initialize the petriDish
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                petriDish[i][j] = new GameOfLifeCell(lifePane, i, j);
            }
        }
        return lifePane;
    }

    //Create the button that goes in the main window.
    protected JComponent createButtonPane() {
        // Center the button in a panel with some space around it.
        JPanel pane = new JPanel(); //use default FlowLayout
        pane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        // Create some buttons
        JButton startStopButton = new JButton("Start/Stop");
        startStopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                // if we're started, stop
                if (doRun.get()) {
                    endLife();
                } else {
                    // if we're stopped, start
                    createLife();
                }
            }

        });
        pane.add(startStopButton);
        //I was going to add a next gen button, but it seems sort of
        //pointless. It's more fun to just watch
//        JButton nextGenButton = new JButton("Next Generation");
//        nextGenButton.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent evt) {
//                // next gen!
//
//            }
//
//        });
//        pane.add(nextGenButton);
        JButton randomizeButton = new JButton("Randomize");
        randomizeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                // perform random routine
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        petriDish[i][j].setAlive(random.nextBoolean());
                    }
                }
            }

        });
        pane.add(randomizeButton);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                // perform the clear action
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        petriDish[i][j].setAlive(false);
                    }
                }
            }

        });
        pane.add(clearButton);
        JButton closeButton = new JButton("Exit");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                executor.shutdownNow();
                System.exit(0);
            }

        });
        pane.add(closeButton); 
        return pane;
    }

    protected void createLife() {
        if (doRun.compareAndSet(false, true)) {
            // start or stop
            lifeFuture = executor.submit(new Runnable() {
                public void run() {
                    do {
                        boolean[][] living = new boolean[size][size];
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                int top = (j > 0 ? j - 1 : size - 1);
                                int btm = (j < size - 1 ? j + 1 : 0);
                                int lft = (i > 0 ? i - 1 : size - 1);
                                int rgt = (i < size - 1 ? i + 1 : 0);
                                int neighbors = 0;
                                if (petriDish[i][top].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[i][btm].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[lft][top].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[lft][btm].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[lft][j].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[rgt][j].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[rgt][top].isLiving()) {
                                    neighbors++;
                                }
                                if (petriDish[rgt][btm].isLiving()) {
                                    neighbors++;
                                }
                                living[i][j] = petriDish[i][j].isAlive(neighbors);
                            }
                        }
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                petriDish[i][j].setAlive(living[i][j]);
                            }
                        }
                        lifePane.repaint();
                        try {
                            Thread.sleep(100L);
                        } catch (Exception e) {
                            break;  
                        }
                    } while (doRun.get());
                }
            });
        }
    }

    protected void endLife() {
        if (doRun.compareAndSet(true, false)) {
            if (lifeFuture != null) {
                // allow interrupt if sleeping
                lifeFuture.cancel(true);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //Schedule a job for the event-dispatching thread: creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }

        });
    }

    // represents a visual cell
    class GameOfLifeCell extends JPanel {

        final int row;

        final int col;

        boolean isLiving;

        /**
         * Creates new form GameOfLifeGUI
         */
        public GameOfLifeCell(JPanel pane, int r, int c) {
            this.row = r;
            this.col = c;
            this.isLiving = random.nextBoolean();
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    isLiving = !isLiving;
                    repaint();
                }

            });
            this.setPreferredSize(new Dimension(10, 10));
            // add ourself to the pane
            pane.add(this);
        }

        public boolean isAlive(int neighbors) {
            boolean alive = false;
            if (isLiving) {
                if (neighbors < 2) {
                    alive = false;
                } else if (neighbors == 2 || neighbors == 3) {
                    alive = true;
                } else if (neighbors > 3) {
                    alive = false;
                }
            } else if (neighbors == 3) {
                alive = true;
            }
            return alive;
        }

        public void setAlive(boolean alive) {
            isLiving = alive;
        }

        public boolean isLiving() {
            return isLiving;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isLiving) {
                g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            } else {
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }

    }

}
