package eu.project.rapid.common;

import eu.project.rapid.common.RapidMessages.AnimationMsg;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AnimationMsgSender {

    private static final String TAG = "AnimationMsgSender";
    private static AnimationMsgSender instance;

    private static boolean demoAnimate = true;

    private static final Thread demoServerThread;
    private static BlockingQueue<String> commandQueue = new ArrayBlockingQueue<>(1000);
    private static String demoServerIp;
    private static int demoServerPort;

    static {
        demoServerThread = new Thread() {
            public void run() {

                System.out.println("Started thread that consumes the commands to send to the Demo server");

                while (true) {
                    try {
                        String cmd = commandQueue.take();
                        sendAnimationMsg(cmd);
                    } catch (InterruptedException e) {
                        System.err.println(TAG + " - " + e);
                    }
                }
            }
        };

        demoServerThread.start();
    }

    private AnimationMsgSender(String demoServerIp, int demoServerPort) {
        AnimationMsgSender.demoServerIp = demoServerIp;
        AnimationMsgSender.demoServerPort = demoServerPort;
    }

    public static synchronized AnimationMsgSender getInstance(String demoServerIp, int demoServerPort) {
        if (instance == null) {
            instance = new AnimationMsgSender(demoServerIp, demoServerPort);
        }

        return instance;
    }

    /**
     * Connect to the animation server and send the messages that represent the sequence of
     * operations.
     *
     * @param msg
     */
    public synchronized void sendAnimationMsg(final AnimationMsg msg) {

        if (demoAnimate) {
            boolean added = false;
            while (!added) {
                try {
                    commandQueue.put(msg.toString());
                    added = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void sendAnimationMsg(String msg) {
        Socket socket = null;
        PrintWriter pout = null;

        try {
            System.out.println(
                    TAG + " - Sending animation msg: " + msg + " to " + demoServerIp + ":" + demoServerPort);
            socket = new Socket();
            socket.connect(new InetSocketAddress(demoServerIp, demoServerPort), 1000);
            pout = new PrintWriter(socket.getOutputStream(), true);
            pout.print(msg);

        } catch (Exception e) {
            // System.err.println("Could not connect to animation server: " + demoServerIp + ":"
            // + demoServerPort + ": " + e);
        } finally {
            if (pout != null) {
                pout.close();
            }
            RapidUtils.closeQuietly(socket);
        }
    }
}
