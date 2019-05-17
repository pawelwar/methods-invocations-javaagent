package warek.application;

import java.security.SecureRandom;

public class AppMain {

    public static void main(String[] args) {
        TestService service1 = new TestService();
        service1.someLongRunningMethod();
        service1.someMethod();

        TestService service2 = new TestService();
        service2.someLongRunningMethod();
        service2.someMethod();
    }

    static class TestService {

        SecureRandom random = new SecureRandom();

        private void someLongRunningMethod() {
            randomSleep();
        }

        private void someMethod() {
            // intentionally nothing
        }

        private void randomSleep() {
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
