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

import eu.project.rapid.common.RapidConstants;
import eu.project.rapid.common.RapidConstants.SETUP_TYPE;


/**
 * Configuration class used to read the config file and keep the parameters.
 *
 */
public class Configuration {

  public static final int DEFAULT_CLONE_PORT = 4322;
  public static final int DEFAULT_SSL_CLONE_PORT = 5322;

  private Scanner configFileScanner;

  private String animationServerIp;
  private int animationServerPort = 6666;
  private String dsIp;
  private int dsPort = 4319;
  private String managerIp;
  private int managerPort = 4320;
  private SETUP_TYPE setupType; // Local, Amazon, Hybrid
  private int nrClonesKVMToStartOnStartup = 0;
  private int nrClonesVBToStartOnStartup = 0;
  private int nrClonesAmazonToStartOnStartup = 0;
  private Clone clone;
  private int clonePortBandwidthTest = 4321;
  private int clonePort = DEFAULT_CLONE_PORT;
  private String cloneName;
  private int cloneId;
  private int commType;

  // SSL related configuration parameters
  private int sslClonePort = DEFAULT_SSL_CLONE_PORT;
  private boolean cryptoInitialized = false;
  private PublicKey publicKey;
  private PrivateKey privateKey;
  private KeyManagerFactory kmf;
  private SSLContext sslContext;
  private SSLSocketFactory sslFactory;

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
          this.managerIp = configFileScanner.nextLine().trim();
        }

        else if (line.equals(RapidConstants.MANAGER_PORT)) {
          this.managerPort = configFileScanner.nextInt();
        }

        else if (line.equals(RapidConstants.DEMO_SERVER_IP)) {
          this.animationServerIp = configFileScanner.nextLine().trim();
        }

        else if (line.equals(RapidConstants.DEMO_SERVER_PORT)) {
          this.animationServerPort = configFileScanner.nextInt();
        }

        // If this is the type of the C2C platform to create.
        // Expected one of the alternatives: Local, Amazon, or Hybrid.
        // For the moment this implementation does not support Hybrid configuration in automatic
        // way.
        else if (line.equals(RapidConstants.CLONE_TYPES)) {
          String temp = configFileScanner.nextLine().trim();
          if (temp.equalsIgnoreCase("Virtualbox"))
            setSetupType(RapidConstants.SETUP_TYPE.VIRTUALBOX);
          else if (temp.equalsIgnoreCase("KVM"))
            setSetupType(RapidConstants.SETUP_TYPE.KVM);
          else if (temp.equalsIgnoreCase("Amazon"))
            setSetupType(RapidConstants.SETUP_TYPE.AMAZON);
          else if (temp.equalsIgnoreCase("Hybrid"))
            setSetupType(RapidConstants.SETUP_TYPE.HYBRID);
          else {
            System.err.println("Configuration error: " + temp);
          }
        }

        // If this is the line containing the number of KVM clones to start in this phase.
        // Next line should be a number indicating the number of clones.
        else if (line.equals(RapidConstants.NR_CLONES_KVM_TO_START)) {
          this.nrClonesKVMToStartOnStartup = configFileScanner.nextInt();
        }

        // If this is the line containing the number of VB clones to start in this phase.
        // Next line should be a number indicating the number of clones.
        else if (line.equals(RapidConstants.NR_CLONES_VB_TO_START)) {
          this.nrClonesVBToStartOnStartup = configFileScanner.nextInt();
        }

        // If this is the line containing the number of Amazon clones to start in this phase.
        // Next line should be a number indicating the number of clones.
        else if (line.equals(RapidConstants.NR_CLONES_AMAZON_TO_START)) {
          this.nrClonesAmazonToStartOnStartup = configFileScanner.nextInt();
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

  public String getManagerIp() {
    return managerIp;
  }

  public void setManagerIp(String managerIp) {
    this.managerIp = managerIp;
  }

  /**
   * @return the port
   */
  public int getManagerPort() {
    return managerPort;
  }

  /**
   * @param port the port to set
   */
  public void setManagerPort(int port) {
    this.managerPort = port;
  }

  /**
   * @return the setupType
   */
  public SETUP_TYPE getSetupType() {
    return setupType;
  }

  /**
   * @param type The setup type to set: Local, Amazon, Hybrid
   */
  public void setSetupType(SETUP_TYPE type) {

    System.out.println("Setting the type to: " + type);

    this.setupType = type;
  }

  /**
   * @return the nrClonesKVMToStartOnStartup
   */
  public int getNrClonesKVMToStartOnStartup() {
    return nrClonesKVMToStartOnStartup;
  }

  /**
   * @param nrClonesKVMToStartOnStartup the nrClonesKVMToStartOnStartup to set
   */
  public void setNrClonesKVMToStartOnStartup(int nrClonesKVMToStartOnStartup) {
    this.nrClonesKVMToStartOnStartup = nrClonesKVMToStartOnStartup;
  }

  /**
   * @return the nrClonesVBToStartOnStartup
   */
  public int getNrClonesVBToStartOnStartup() {
    return nrClonesVBToStartOnStartup;
  }

  /**
   * @param nrClonesVBToStartOnStartup the nrClonesVBToStartOnStartup to set
   */
  public void setNrClonesVBToStartOnStartup(int nrClonesVBToStartOnStartup) {
    this.nrClonesVBToStartOnStartup = nrClonesVBToStartOnStartup;
  }

  /**
   * @return the nrClonesAmazonToStartOnStartup
   */
  public int getNrClonesAmazonToStartOnStartup() {
    return nrClonesAmazonToStartOnStartup;
  }

  /**
   * @param nrClonesAmazonToStartOnStartup the nrClonesAmazonToStartOnStartup to set
   */
  public void setNrClonesAmazonToStartOnStartup(int nrClonesAmazonToStartOnStartup) {
    this.nrClonesAmazonToStartOnStartup = nrClonesAmazonToStartOnStartup;
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

  public void printConfigFile() {
    System.out.println(setupType);
    System.out.println(nrClonesVBToStartOnStartup);
    System.out.println(nrClonesAmazonToStartOnStartup);
    System.out.println(clonePort);
    System.out.println(sslClonePort);
  }
}
