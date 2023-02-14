package org.example;

import javax.swing.*;

public class ProgressThread extends Thread{
    JProgressBar progressBar;

    public ProgressThread(JProgressBar progressBar){
        this.progressBar = progressBar;
    }
    public void run(){
        for (int i = 0; i < 80; i++) {
            if (progressBar.getValue() < i){
                progressBar.setValue(i);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
