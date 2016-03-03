package eu.project.rapid.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RapidUtils {

  private static final String TAG = "RapidUtils";

  private static final Thread demoServerThread;
  private static BlockingQueue<String> commandQueue = new ArrayBlockingQueue<String>(1000);
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
          }
        }
      }
    };

    demoServerThread.start();
  }

  /**
   * Utility to execute a shell command on a Mac OS.
   * 
   * @param command
   * @param asRoot
   * @param password
   * @return
   */
  public static String executeCommand(String command, boolean asRoot, String password) {

    Process p = null;
    String[] cmd = new String[] {"/bin/bash", "-c", command};

    try {
      if (asRoot) {
        cmd = new String[] {"/bin/bash", "-c", "echo " + password + "|sudo -S " + command};
      }

      System.out.println("Executing command: " + command);
      p = Runtime.getRuntime().exec(cmd);
      // you can pass the system command or a script to exec command.
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

      // read the output from the command
      StringBuilder sb = new StringBuilder();
      String s = "";
      while ((s = stdInput.readLine()) != null) {
        // System.out.println("Std OUT: "+s);
        sb.append(s);
      }

      while ((s = stdError.readLine()) != null) {
        System.out.println("Std ERROR : " + s);
      }

      System.out.println(sb.toString());

      stdInput.close();
      stdError.close();
      writer.close();

      return sb.toString();

    } catch (IOException e) {

      e.printStackTrace();
    }
    return null;
  }

  public static String executeCommand(String command) {
    return executeCommand(command, false, null);
  }

  public static byte[] serialize(Object obj) throws IOException {
    byte[] serializedObj;

    ByteArrayOutputStream b = new ByteArrayOutputStream();
    ObjectOutputStream o = new ObjectOutputStream(b);
    try {
      o.writeObject(obj);
      o.flush();
      serializedObj = b.toByteArray();
    } finally {
      o.close();
    }

    return serializedObj;
  }

  public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    return RapidUtils.deserialize(bytes, 0, bytes.length);
  }

  public static Object deserialize(byte[] bytes, int offset, int length)
      throws IOException, ClassNotFoundException {
    Object obj;

    ByteArrayInputStream b = new ByteArrayInputStream(bytes, offset, length);
    ObjectInputStream o = new ObjectInputStream(b);
    obj = o.readObject();
    o.close();

    return obj;
  }

  /**
   * Connect to the animation server and send the messages that represent the sequence of
   * operations.
   * 
   * @param config
   * @param millis
   * @param msgs
   */
  public static synchronized void sendAnimationMsg(Configuration config, String msg) {
    sendAnimationMsg(config.getAnimationServerIp(), config.getAnimationServerPort(), msg);
  }

  /**
   * Connect to the animation server and send the messages that represent the sequence of
   * operations.
   * 
   * @param config
   * @param millis
   * @param msgs
   */
  public static synchronized void sendAnimationMsg(final String ip, final int port,
      final String msg) {

    RapidUtils.demoServerIp = ip;
    RapidUtils.demoServerPort = port;

    boolean added = false;
    while (!added) {
      try {
        commandQueue.put(msg);
        added = true;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void closeQuietly(InputStream is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
      }
    }
  }

  public static void closeQuietly(OutputStream os) {
    if (os != null) {
      try {
        os.close();
      } catch (IOException e) {
      }
    }
  }

  public static void closeQuietly(ObjectInputStream oIs) {
    if (oIs != null) {
      try {
        oIs.close();
      } catch (IOException e) {
      }
    }
  }

  public static void closeQuietly(ObjectOutputStream oOs) {
    if (oOs != null) {
      try {
        oOs.close();
      } catch (IOException e) {
      }
    }
  }

  public static void closeQuietly(Socket socket) {
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
      }
    }
  }

  private static void sendAnimationMsg(String msg) {
    Socket socket = null;
    PrintWriter pout = null;

    try {
      System.out.println(TAG + " - Sending animation msg: " + msg);
      socket = new Socket(demoServerIp, demoServerPort);
      pout = new PrintWriter(socket.getOutputStream(), true);
      pout.print(msg);

    } catch (UnknownHostException e) {
      System.err.println("Could not connect to animation server: " + demoServerIp + ":"
          + demoServerPort + ": " + e);
    } catch (IOException e) {
      System.err.println("Could not connect to animation server: " + demoServerIp + ":"
          + demoServerPort + ": " + e);
    } catch (Exception e) {
      System.err.println("Could not connect to animation server: " + demoServerIp + ":"
          + demoServerPort + ": " + e);
    } finally {
      if (pout != null)
        pout.close();
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {
        }
      }
    }
  }
}
