import updater.Updater;

public class main {
    static class CustomInteger {
        int value;
        public CustomInteger(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws Exception {
        Updater updater = new Updater();
        updater.run();
    }
}
