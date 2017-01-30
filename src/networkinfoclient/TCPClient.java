/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinfoclient;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Alfonso
 */
public class TCPClient extends Thread {

    private String serverMessage;
    public String serverIp = ""; 
    public static final int SERVERPORT = 8080;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private PrintWriter out;
    private BufferedReader in;

    public TCPClient( String serverIp, OnMessageReceived listener) {
        mMessageListener = listener;
        this.serverIp = serverIp;
    }

    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {
        mRun = true;
        try {
            Socket socket = new Socket(serverIp, SERVERPORT);
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) {
                    serverMessage = in.readLine();
                    if (serverMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }
            } catch (Exception e) {
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        } catch (Exception e) {
        }
    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
