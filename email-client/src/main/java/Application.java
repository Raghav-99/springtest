public class Application {
    public static void main(String[] args) {
        try {
            EmailClient ec = new EmailClient();
            ec.start();    
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        System.out.println("Client shutting down...");
        
    }
}
