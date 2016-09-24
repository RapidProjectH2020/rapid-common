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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;


/**
 * Configuration class used to read the config file and keep the parameters.<br>
 * This class is used only by the Android project, since the Linux version uses the Java Properties
 * configuration format.
 *
 */
public class Configuration {

  private Scanner configFileScanner;

  private String animationServerIp = RapidConstants.DEFAULT_PRIMARY_ANIMATION_SERVER_IP;
  private int animationServerPort = RapidConstants.DEFAULT_PRIMARY_ANIMATION_SERVER_PORT;
  private String dsIp = RapidConstants.DEFAULT_DS_IP;
  private int dsPort = RapidConstants.DEFAULT_DS_PORT;
  private String slamIp;
  private int slamPort = RapidConstants.DEFAULT_SLAM_PORT;
  private String vmmIp = RapidConstants.DEFAULT_VMM_IP;
  private int vmmPort = RapidConstants.DEFAULT_VMM_PORT;
  private Clone clone;
  private int clonePort = RapidConstants.DEFAULT_VM_PORT;
  private String cloneName;
  private int cloneId;
  private int commType;

  // SSL related configuration parameters
  private int sslClonePort = RapidConstants.DEFAULT_VM_PORT_SSL;
  private boolean cryptoInitialized = false;
  private PublicKey publicKey;
  private PrivateKey privateKey;
  private KeyManagerFactory kmf;
  private SSLContext sslContext;
  private SSLSocketFactory sslFactory;
  private int clonePortBandwidthTest = RapidConstants.DEFAULT_BANDWIDTH_PORT;

  private String gvirtusIp;// = "storm.uniparthenope.it";
  private int gvirtusPort = RapidConstants.DEFAULT_GVIRTUS_PORT;

  public Configuration() {}

  public Configuration(InputStream is) {
    configFileScanner = new Scanner(is);
  }

  public Configuration(String configFilePath) throws FileNotFoundException {
    this(new File(configFilePath));
  }

  public Configuration(File configFile) throws FileNotFoundException {
    configFileScanner = new Scanner(new FileReader(configFile));
  }

  /**
   * To be used by the clone and phone.
   * 
   * @throws FileNotFoundException
   */
  public void parseConfigFile() throws FileNotFoundException {
    parseConfigFile(null, null, null);
  }

  /**
   * To be used by the manager. Read the configuration file with the following format: <br>
   * # Comment <br>
   * [Category] <br>
   * 
   * @throws FileNotFoundException
   * 
   */
  public void parseConfigFile(ArrayList<Clone> kvmClones, ArrayList<Clone> vbClones,
      ArrayList<Clone> amazonClones) throws FileNotFoundException {

    try {
      while (configFileScanner.hasNext()) {

        // Get the next line of the file and remove any extra spaces
        String line = configFileScanner.nextLine().trim();

        // Empty line or comment
        if (line.length() == 0 || line.startsWith("#"))
          continue;

        else if (line.equals(RapidConstants.DS_IP)) {
          this.dsIp = configFileScanner.nextLine().trim();
        }

        else if (line.equals(RapidConstants.DS_PORT)) {
          this.dsPort = configFileScanner.nextInt();
        }

        else if (line.equals(RapidConstants.MANAGER_IP)) {
          this.vmmIp = configFileScanner.nextLine().trim();
        }

        else if (line.equals(RapidConstants.MANAGER_PORT)) {
          this.vmmPort = configFileScanner.nextInt();
        }

        else if (line.equals(RapidConstants.DEMO_SERVER_IP)) {
          this.animationServerIp = configFileScanner.nextLine().trim();
        }

        else if (line.equals(RapidConstants.DEMO_SERVER_PORT)) {
          this.animationServerPort = configFileScanner.nextInt();
        }

        // This is the port where the clones will listen for phone connections
        else if (line.equals(RapidConstants.CLONE_PORT)) {
          this.clonePort = configFileScanner.nextInt();
        }

        // This is the port where the clones will listen for SSL phone connections
        else if (line.equals(RapidConstants.CLONE_SSL_PORT)) {
          this.sslClonePort = configFileScanner.nextInt();
        }

        else if (line.equals(RapidConstants.CLONE_NAME)) {
          this.cloneName = configFileScanner.nextLine();
        }

        else if (line.equals(RapidConstants.CLONE_ID)) {
          this.cloneId = configFileScanner.nextInt();
        }

        else if (line.equals(RapidConstants.GVIRTUS_IP)) {
          this.gvirtusIp = configFileScanner.nextLine();
        }

        else if (line.equals(RapidConstants.GVIRTUS_PORT)) {
          this.gvirtusPort = configFileScanner.nextInt();
        }

        // Now there will be a list of clone names that can be used for starting the clones on VB
        else if (line.equals(RapidConstants.KVM_CLONES)) {
          String name = configFileScanner.nextLine().trim();
          while (name.length() > 0) {
            kvmClones.add(new Clone(name));
            name = configFileScanner.nextLine().trim();
          }
        }

        // Now there will be a list of clone names that can be used for starting the clones on VB
        else if (line.equals(RapidConstants.VB_CLONES)) {
          String name = configFileScanner.nextLine().trim();
          while (name.length() > 0) {
            vbClones.add(new Clone(name));
            name = configFileScanner.nextLine().trim();
          }
        }

        // Now there will be a list of clone names (ips) that can be used for starting the clones on
        // Amazon
        else if (line.equals(RapidConstants.AMAZON_CLONES)) {
          String name = configFileScanner.nextLine().trim();
          while (name.length() > 0) {
            amazonClones.add(new Clone(name));
            name = configFileScanner.nextLine().trim();
          }
        }
      }
    } finally {
      if (configFileScanner != null)
        configFileScanner.close();
    }
  }

  /**
   * @return the animationServerIp
   */
  public String getAnimationServerIp() {
    return animationServerIp;
  }

  /**
   * @param animationServerIp the animationServerIp to set
   */
  public void setAnimationServerIp(String animationServerIp) {
    this.animationServerIp = animationServerIp;
  }

  /**
   * @return the animationServerPort
   */
  public int getAnimationServerPort() {
    return animationServerPort;
  }

  /**
   * @param animationServerPort the animationServerPort to set
   */
  public void setAnimationServerPort(int animationServerPort) {
    this.animationServerPort = animationServerPort;
  }

  /**
   * @return the dsIp
   */
  public String getDSIp() {
    return dsIp;
  }

  /**
   * @param dsIp the dsIp to set
   */
  public void setDSIp(String dsIp) {
    this.dsIp = dsIp;
  }

  /**
   * @return the dsPort
   */
  public int getDSPort() {
    return dsPort;
  }

  /**
   * @param dsPort the dsPort to set
   */
  public void setDSPort(int dsPort) {
    this.dsPort = dsPort;
  }

  public String getVmmIp() {
    return vmmIp;
  }

  public void setVMMIp(String vmmIp) {
    this.vmmIp = vmmIp;
  }

  /**
   * @return the port
   */
  public int getVmmPort() {
    return vmmPort;
  }

  /**
   * @param port the port to set
   */
  public void setVMMPort(int port) {
    this.vmmPort = port;
  }

  /**
   * @return the clone
   */
  public Clone getClone() {
    return clone;
  }

  /**
   * @param clone the clone to set
   */
  public void setClone(Clone clone) {
    this.clone = clone;
  }

  /**
   * @return the clonePort
   */
  public int getClonePort() {
    return clonePort;
  }

  /**
   * @param clonePort the clonePort to set
   */
  public void setClonePort(int clonePort) {
    this.clonePort = clonePort;
  }

  /**
   * @return the sslClonePort
   */
  public int getSslClonePort() {
    return sslClonePort;
  }

  /**
   * @param sslClonePort the sslClonePort to set
   */
  public void setSslClonePort(int sslClonePort) {
    this.sslClonePort = sslClonePort;
  }

  public String getCloneName() {
    return cloneName;
  }

  public void setCloneName(String cloneName) {
    this.cloneName = cloneName;
  }

  public int getCloneId() {
    return cloneId;
  }

  public void setCloneId(int cloneId) {
    this.cloneId = cloneId;
  }

  /**
   * @return the cryptoInitialized
   */
  public boolean isCryptoInitialized() {
    return cryptoInitialized;
  }

  /**
   * @param cryptoInitialized the cryptoInitialized to set
   */
  public void setCryptoInitialized(boolean cryptoInitialized) {
    this.cryptoInitialized = cryptoInitialized;
  }

  /**
   * @return the publicKey
   */
  public PublicKey getPublicKey() {
    return publicKey;
  }

  /**
   * @param publicKey the publicKey to set
   */
  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  /**
   * @return the privateKey
   */
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  /**
   * @param privateKey the privateKey to set
   */
  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  /**
   * @return the kmf
   */
  public KeyManagerFactory getKmf() {
    return kmf;
  }

  /**
   * @param kmf the kmf to set
   */
  public void setKmf(KeyManagerFactory kmf) {
    this.kmf = kmf;
  }

  /**
   * @return the sslContext
   */
  public SSLContext getSslContext() {
    return sslContext;
  }

  /**
   * @param sslContext the sslContext to set
   */
  public void setSslContext(SSLContext sslContext) {
    this.sslContext = sslContext;
  }

  /**
   * @return the sslFactory
   */
  public SSLSocketFactory getSslFactory() {
    return sslFactory;
  }

  /**
   * @param sslFactory the sslFactory to set
   */
  public void setSslFactory(SSLSocketFactory sslFactory) {
    this.sslFactory = sslFactory;
  }

  /**
   * @return the commType
   */
  public int getCommType() {
    return commType;
  }

  /**
   * @param commType the commType to set
   */
  public void setCommType(int commType) {
    this.commType = commType;
  }

  /**
   * @return the clonePortBandwidthTest
   */
  public int getClonePortBandwidthTest() {
    return clonePortBandwidthTest;
  }

  /**
   * @param clonePortBandwidthTest the clonePortBandwidthTest to set
   */
  public void setClonePortBandwidthTest(int clonePortBandwidthTest) {
    this.clonePortBandwidthTest = clonePortBandwidthTest;
  }

  /**
   * @return the gvirtusIp
   */
  public String getGvirtusIp() {
    return gvirtusIp;
  }

  /**
   * @param gvirtusIp the gvirtusIp to set
   */
  public void setGvirtusIp(String gvirtusIp) {
    this.gvirtusIp = gvirtusIp;
  }

  /**
   * @return the gvirtusPort
   */
  public int getGvirtusPort() {
    return gvirtusPort;
  }

  /**
   * @param gvirtusPort the gvirtusPort to set
   */
  public void setGvirtusPort(int gvirtusPort) {
    this.gvirtusPort = gvirtusPort;
  }

  public void printConfigFile() {
    System.out.println(clonePort);
    System.out.println(sslClonePort);
  }

/**
 * @return the slamIp
 */
public String getSlamIp() {
	return slamIp;
}

/**
 * @param slamIp the slamIp to set
 */
public void setSlamIp(String slamIp) {
	this.slamIp = slamIp;
}

/**
 * @return the slamPort
 */
public int getSlamPort() {
	return slamPort;
}

/**
 * @param slamPort the slamPort to set
 */
public void setSlamPort(int slamPort) {
	this.slamPort = slamPort;
}
}
