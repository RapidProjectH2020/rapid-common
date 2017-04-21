/*******************************************************************************
 * Copyright (C) 2015, 2016 RAPID EU Project
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *******************************************************************************/
package eu.project.rapid.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import eu.project.rapid.common.RapidMessages.AnimationMsg;

/**
 * These are some utilities to be used on the Rapid project. In particular, some important functions
 * are used by the AS and AC.
 * 
 * @author sokol
 */
public class RapidUtils {

  private static final String TAG = "RapidUtils";
  private static final String IPADDRESS_PATTERN =
      "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
          + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

  private static boolean demoAnimate = true;

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

  public static synchronized void sendAnimationMsg(Configuration config, AnimationMsg msg) {
    sendAnimationMsg(config.getAnimationServerIp(), config.getAnimationServerPort(),
        msg.toString());
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

    if (demoAnimate) {
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
      System.out.println(
          TAG + " - Sending animation msg: " + msg + " to " + demoServerIp + ":" + demoServerPort);
      socket = new Socket(demoServerIp, demoServerPort);
      pout = new PrintWriter(socket.getOutputStream(), true);
      pout.print(msg);

    } catch (Exception e) {
      // System.err.println("Could not connect to animation server: " + demoServerIp + ":"
      // + demoServerPort + ": " + e);
    } finally {
      if (pout != null) {
        pout.close();
      }
      closeQuietly(socket);
    }
  }

  public static boolean isPrimaryAnimationServerRunning(String ip, int port) {
    Socket socket = null;
    PrintWriter pout = null;

    try {
      System.out.println(TAG + " - Connecting with primary server " + ip + ":" + port);
      socket = new Socket(ip, port);
      pout = new PrintWriter(socket.getOutputStream(), true);
      pout.print(AnimationMsg.PING);
      return true;
    } catch (Exception e) {
      // System.err.println("Could not connect to animation server: " + ip + ":"
      // + port + ": " + e);
    } finally {
      if (pout != null) {
        pout.close();
      }
      closeQuietly(socket);
    }
    return false;
  }

  /**
   * Validate ip address with regular expression
   * 
   * @param ip ip address for validation
   * @return true valid ip address, false invalid ip address
   */
  public static boolean validateIpAddress(final String ip) {
    Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
    return pattern.matcher(ip).matches();
  }

  public static String getVmIpLinux() {
    String localIpAddress = "";
    Enumeration<NetworkInterface> e;
    try {
      e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        Enumeration<InetAddress> ee = n.getInetAddresses();

        boolean stop = false;
        while (ee.hasMoreElements()) {
          InetAddress i = (InetAddress) ee.nextElement();
          if (i.getHostAddress().startsWith("10.0.")) {
            localIpAddress = i.getHostAddress();
            stop = true;
            break;
          }
        }
        if (stop)
          break;
      }
    } catch (SocketException e1) {
      System.err.println("Error while getting VM IP: " + e1);
    }

    return localIpAddress;

  }

  /**
   * Connects to the DS and returns the IP of the demo animation server.
   * 
   * @param dsIp
   * @param dsPort
   * @return IP of the demo animation server or null.
   */
  public static String getDemoAnimationServerIpFromDs(String dsIp, int dsPort) {
    String ip = null;
    Socket socket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try {
      socket = new Socket(dsIp, dsPort);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());

      oos.writeByte(RapidMessages.GET_DEMO_SERVER_IP_DS);
      oos.flush();
      ip = ois.readUTF();
    } catch (IOException e) {
      System.err.println("Could not connect to DS for getting demo server IP: " + e);
    } finally {
      closeQuietly(ois);
      closeQuietly(oos);
      closeQuietly(socket);
    }

    return ip;
  }
}
