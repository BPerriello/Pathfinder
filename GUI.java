import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

/**
 *  A two-dimensional array holds integer values that correspond to the different kinds of nodes represented in the GUI.
 * The integer values are as follows:
 * 0 - Unvisited node - White
 * 1 - Start node - Blue
 * 2 - End node - Blue
 * 3 - Barrier - Black
 * 4 - Visited node - Red
 * 5 - Shortest-path node - Green
 */
public class GUI extends JFrame implements ActionListener {

    private int mouseX;
    private int mouseY;
    private int[][] state = makeArray(new int[30][63]);
    private int count = 0;
    private JButton bfsButton;
    private int startX;
    private int startY;

    /**
     * Creates the window and button on the graphical user interface.
     */
    public GUI() {
        this.setTitle("Pathfinder");
        this.setSize(1276, 659);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        Board board = new Board();
        this.setContentPane(board);
        bfsButton = new JButton("Start BFS");
        this.add(bfsButton);
        bfsButton.addActionListener(this);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        Click click = new Click();
        this.addMouseListener(click);
        Move move = new Move();
        this.addMouseMotionListener(move);
        this.setVisible(true);
    }

    /**
     * Initiates the breadth first search when the button on the GUI is clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String endString = "";
        String pred[][] = new String[30][63];
        int height = state.length;
        int length = state[0].length;
        Queue<String> queue = new LinkedList<>();
        queue.add(startX + "," + startY);
        String startString = startX + "," + startY;
        //Breadth-First Traversal
        int col = 0;
        int row = 0;
        while (state[col][row] != 2) {
            String x = queue.remove();
            row = Integer.parseInt(x.split(",")[0]);
            col = Integer.parseInt(x.split(",")[1]);
            if (state[col][row] == 2) {
                endString = x;
                break;
            }
            if (state[col][row] == 3 || state[col][row] == 4 || row > 63 || col > 30 || row < 0 || col < 0) {
                continue;
            }
            if (!(row < 0 || col < 0 || row >= length || col >= height || state[col][row] == 1)) {
                state[col][row] = 4; //marks cell as visited
            }
            if ((col - 1) >= 0 && state[col - 1][row] != 3 && state[col - 1][row] != 4) {
                pred[col - 1][row] = x;
                queue.add(row + "," + (col - 1)); //go up
            }
            if ((col + 1) < height && state[col + 1][row] != 3 && state[col + 1][row] != 4) {
                pred[col + 1][row] = x;
                queue.add(row + "," + (col + 1)); //go down
            }
            if ((row - 1) >= 0 && state[col][row - 1] != 3 && state[col][row - 1] != 4) {
                pred[col][row - 1] = x;
                queue.add((row - 1) + "," + col); //go left
            }
            if ((row + 1) < length && state[col][row + 1] != 3 && state[col][row + 1] != 4) {
                pred[col][row + 1] = x;
                queue.add((row + 1) + "," + col); //go right
            }
        }
        String y = endString;
        int row1 = Integer.parseInt(y.split(",")[0]);
        int col1 = Integer.parseInt(y.split(",")[1]);
        while (!pred[col1][row1].equals(startString)) {
            row1 = Integer.parseInt((pred[col1][row1]).split(",")[0]);
            col1 = Integer.parseInt((pred[col1][row1]).split(",")[1]);
            System.out.println("Pred row:" + row1 + " Pred col:" + col1);
            state[col1][row1] = 5;
        }
    }

    public class Board extends JPanel {

        /**
         * Colors the squares on the grid corresponding to the integer values in the 'state' array.
         */
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 40, 1275, 658);
            for (int i = 0; i < 63; i++) {
                for (int j = 1; j < 30; j++) {
                    g.setColor(Color.white);
                    if (state[j - 1][i] == 1 || state[j - 1][i] == 2) {
                        g.setColor(Color.blue);
                    } else if (state[j - 1][i] == 3) {
                        g.setColor(Color.BLACK);
                    } else if (state[j - 1][i] == 4) {
                        g.setColor(Color.RED);
                    } else if (state[j - 1][i] == 5) {
                        g.setColor(Color.GREEN);
                    }
                    if (mouseX >= i * 20 + 7 && mouseX < i * 20 + 20 + 7 && mouseY >= (j + 1) * 20 + 30 && mouseY < (j + 1) * 20 + 30 + 20) {
                        g.setColor(Color.yellow);
                    }
                    int spacing = 1;
                    g.fillRect(spacing + i * 20, spacing + j * 20 + 20, 20 - 2, 20 - 2);
                }
            }
        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    public class Click implements MouseListener {
        /**
         * Stores the x and y coordinates of the mouse when it is clicked.
         * Stores the appropriate value in 'state' depending on the total number of clicks performed.
         * First click - defines start node
         * Second click - defines end node
         * Third click and beyond - defines barrier nodes
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            count++;
            mouseX = e.getX();
            mouseY = e.getY();
            if (inBoxX() != -1 && inBoxY() != -1) {
                if (count == 1) {
                    state[inBoxY()][inBoxX()] = 1;
                    startY = inBoxY();
                    startX = inBoxX();
                } else if (count == 2) {
                    state[inBoxY()][inBoxX()] = 2;
                } else if (count > 2) {
                    state[inBoxY()][inBoxX()] = 3;
                }
            } else {
                System.out.println("The mouse isn't inside of a box");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    /**
     * Determines which column the mouse is in.
     */
    public int inBoxX() {
        for (int i = 0; i < 63; i++) {
            for (int j = 1; j < 30; j++) {
                if (mouseX >= i * 20 + 7 && mouseX < i * 20 + 20 + 7 && mouseY >= (j + 1) * 20 + 30 && mouseY < (j + 1) * 20 + 30 + 20) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Determines which row the mouse is in.
     */
    public int inBoxY() {
        for (int i = 0; i < 63; i++) {
            for (int j = 1; j < 30; j++) {
                if (mouseX >= i * 20 + 7 && mouseX < i * 20 + 20 + 7 && mouseY >= (j + 1) * 20 + 30 && mouseY < (j + 1) * 20 + 30 + 20) {
                    return j - 1;
                }
            }
        }
        return -1;
    }

    /**
     * Creates the 'state' array, which holds integer values corresponding to the different kinds of nodes.
     */
    public int[][] makeArray(int[][] arr) {
        for (int i = 0; i < 63; i++) {
            for (int j = 0; j < 30; j++) {
                arr[j][i] = 0;
            }
        }
        return arr;
    }
}
