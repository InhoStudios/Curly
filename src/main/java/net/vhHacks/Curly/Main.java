package net.vhHacks.Curly;

import javax.security.auth.login.LoginException;

public class Main {

    public static Thread runner;

    public static void main(String[] args) throws LoginException {
        Bot bot = new Bot();
        runner = new Thread(bot);

        start();
    }

    public static void start() {
        runner.start();
    }

}
