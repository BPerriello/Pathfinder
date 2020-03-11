public class Main implements Runnable {

    private GUI gui = new GUI();

    public static void main(String[] args) {
        new Thread(new Main()).start();
    }

    @Override
    public void run() {
        while(true){
            gui.repaint();
        }
    }
}
