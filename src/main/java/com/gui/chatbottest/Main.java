package com.gui.chatbottest;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guilherme
 */
public class Main {

    public static void main(String[] args) {
        TelegramBot tb = new TelegramBot("389731961:AAFyrsrnnxXRKeToKj2P-Y0EYcYmZv7S0HQ");
        try {
            tb.run();
        } catch (UnirestException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
