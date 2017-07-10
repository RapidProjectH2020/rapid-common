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

import java.io.Serializable;
import java.security.PublicKey;

/**
 * The clone object
 *
 */
public class Clone implements Serializable {

  private static final long serialVersionUID = -6097868122333778588L;

  private int id = -1;
  private String name;
  private String ip;
  private CloneState status;
  private int clonePortBandwidthTest = 4321;
  private int port = RapidConstants.DEFAULT_VM_PORT;
  private int sslPort = RapidConstants.DEFAULT_VM_PORT_SSL;
  private CloneType type;
  private PublicKey publicKey;
  private boolean cryptoPossible = false;

  /**
   * Empty clone to be sent to the phone when no clone is available.
   */
  public Clone() {}

  /**
   * Used by the Configuration class to create an initial list of clones.
   * 
   * @param name
   */
  public Clone(String name) {
    this(name, null);
  }

  public Clone(String name, String ip) {
    this(name, ip, RapidConstants.DEFAULT_VM_PORT);
  }

  public Clone(String name, String ip, int port) {
    this(name, ip, port, RapidConstants.DEFAULT_VM_PORT_SSL);
  }

  public Clone(String name, String ip, int port, int sslPort) {
    this.name = name;
    this.ip = ip;
    this.port = port;
    this.sslPort = sslPort;
    this.status = CloneState.STOPPED;
    this.type = detectType(this.name);
  }

  public enum CloneType {
    UNKNOWN, VIRTUALBOX, AMAZON, KVM
  }

  public enum CloneState {
    UNKNOWN, STOPPED, PAUSED, RESUMED, AUTHENTICATED, ASSIGNED_TO_PHONE
  }

  /**
   * Detect the type of the clone from the name
   */
  public static CloneType detectType(String name) {
    if (name.startsWith("vb-"))
      return CloneType.VIRTUALBOX;
    else if (name.startsWith("amazon-"))
      return CloneType.AMAZON;
    else if (name.startsWith("kvm-"))
      return CloneType.KVM;
    else {
//      System.err.println("The type of this clone could not be determined");
//      printInfoAboutCloneName();
      return CloneType.UNKNOWN;
    }
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the ip
   */
  public String getIp() {
    return ip;
  }

  /**
   * @param ip the ip to set
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  /**
   * @return the status
   */
  public CloneState getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(CloneState status) {
    this.status = status;
  }

  /**
   * @return the portForPhone
   */
  public int getPort() {
    return port;
  }

  /**
   * @param port the portForPhone to set
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * @return the sslPortForPhone
   */
  public int getSslPort() {
    return sslPort;
  }

  /**
   * @param sslPort the sslPortForPhone to set
   */
  public void setSslPort(int sslPort) {
    this.sslPort = sslPort;
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
   * @return the type
   */
  public CloneType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(CloneType type) {
    this.type = type;
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

  public void describeClone() {
    System.out.println(name + " " + id + " " + ip);
  }

  /**
   * Find which is the status of this clone and make it available for the phone.
   * 
   * @return <b>True</b> if it was possible to make the clone available<br>
   *         <b>False</b> otherwise
   */
  public boolean prepareClone() {

    switch (this.type) {

      case KVM:
        System.out.println("Preparing the KVM clone " + this.name);
        System.err.println("Not yet implemented for the KVM clones");
        break;

      case VIRTUALBOX:

        System.out.println("Preparing the Virtualbox clone " + this.name);
        CloneState cloneState = getTheStateOfPhysicalMachine();
        System.out.println("clone " + this.name + " is on " + cloneState + " state");

        switch (cloneState) {

          case STOPPED:
            if (startVBClone()) {
              System.out.println("Started the Virtualbox clone " + this.name);
              this.status = CloneState.RESUMED;
              return true;
            } else {
              System.err.println("Could not start the Virtualbox clone " + this.name);
              this.status = CloneState.STOPPED;
              return false;
            }

          case PAUSED:
            if (resumeVBClone()) {
              System.out.println("Resumed the Virtualbox clone " + this.name);
              this.status = CloneState.RESUMED;
              return true;
            } else {
              System.err.println("Could not resume the Virtualbox clone " + this.name);
              this.status = CloneState.STOPPED;
              return false;
            }

          case RESUMED:
            System.out.println("The Virtualbox clone " + this.name + " was already started");
            return true;

          default:
            break;
        }

        return false;

      case AMAZON:
        System.out.println("Preparing the Amazon clone " + this.name);
        System.err.println("Not yet implemented for the amazon clones");
        break;

      case UNKNOWN:
        System.err.println("I don't know how to start the clone " + this.name);
        printInfoAboutCloneName();
        break;
    }

    return false;
  }

  /**
   * 
   * @return The state of the clone: STOPPED, RESUMED, PAUSED
   */
  private CloneState getTheStateOfPhysicalMachine() {
    switch (this.type) {

      case KVM:
        System.err.println("Not yet implemented for KVM clones");
        break;

      case VIRTUALBOX:
        String out = RapidUtils.executeCommand("/usr/local/bin/VBoxManage showvminfo " + this.name);

        if (out.contains("powered off (since")) {
          return CloneState.STOPPED;
        } else if (out.contains("running (since")) {
          return CloneState.RESUMED;
        } else if (out.contains("paused (since")) {
          return CloneState.PAUSED;
        }

        break;

      case AMAZON:
        System.err.println("Not yet implemented for the amazon clones");
        break;

      case UNKNOWN:
        System.err.println("I cant't get the physical state of the clone " + this.name);
        printInfoAboutCloneName();
        break;
    }

    return CloneState.UNKNOWN;
  }

  /**
   * Start a VB clone
   * 
   * @throws IllegalStateException
   */
  public boolean startKVMClone() {
    System.err.println("Not implemented");
    return false;
  }

  /**
   * Start a VB clone
   * 
   * @throws IllegalStateException
   */
  public boolean stopKVMClone() {
    System.err.println("Not implemented");
    return false;
  }

  /**
   * Start a VB clone
   * 
   * @throws IllegalStateException
   */
  public boolean startVBClone() {

    String out = RapidUtils
        .executeCommand("/usr/local/bin/VBoxManage startvm " + this.name + " --type headless");

    if (out.contains("has been successfully started.")) {
      return true;
    }

    return false;
  }

  /**
   * Resume a VB clone.
   * 
   * @return
   */
  public boolean resumeVBClone() {
    RapidUtils.executeCommand("/usr/local/bin/VBoxManage controlvm " + this.name + " resume");

    switch (getTheStateOfPhysicalMachine()) {
      case STOPPED:
        return false;
      case PAUSED:
        return false;
      case RESUMED:
        return true;

      default:
        break;
    }

    return false;
  }

  public void pauseVBClone() {
    System.err.println("Not implemented");
  }

  public void stopVBClone() {
    System.err.println("Not implemented");
  }

  // Methods to control the Amazon clones
  public void startAmazonClone() {
    System.err.println("Not implemented");
  }

  public void resumeAmazonClone() {
    System.err.println("Not implemented");
  }

  public void pauseAmazonClone() {
    System.err.println("Not implemented");
  }

  public void stopAmazonClone() {
    System.err.println("Not implemented");
  }

  /**
   * Print info about the clone.
   */
  public static void printInfoAboutCloneName() {
    System.err.println(
        "The name of the clone should start with vb- for VirtualBox clones, with kvm- for KVM clones, and with amazon- for Amazon clones.");
  }

  /**
   * @return the cryptoPossible
   */
  public boolean isCryptoPossible() {
    return cryptoPossible;
  }

  /**
   * @param cryptoPossible the cryptoPossible to set
   */
  public void setCryptoPossible(boolean cryptoPossible) {
    this.cryptoPossible = cryptoPossible;
  }

  @Override
  public String toString() {
    return "name: " + this.name + " IP: " + this.ip + " PORT:" + this.port + " SSL_PORT: "
        + this.sslPort + " cryptoPossible: " + this.cryptoPossible;
  }
}


