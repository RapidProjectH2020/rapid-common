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

public class RapidConstants {

  public static final int MAX_NUM_CLIENTS = 32;

  // Offloading decision related variables
  // public static final int LOCATION_NOT_DECIDED = -1;
  public static final int LOCATION_LOCAL = 1;
  public static final int LOCATION_REMOTE = 2;
  public static final int LOCATION_HYBRID = 3;
  public static final int LOCATION_DYNAMIC_TIME = 4;
  public static final int LOCATION_DYNAMIC_ENERGY = 5;
  public static final int LOCATION_DYNAMIC_TIME_ENERGY = 6;

  // Default ports to be used by the different components
  public static final String DEFAULT_SILO_SERVER_IP = "83.235.169.221";
  public static final int DEFAULT_VM_PORT = 4322;
  public static final int DEFAULT_VM_PORT_SSL = 5322;
  public static final String DEFAULT_DS_IP = DEFAULT_SILO_SERVER_IP;
  public static final int DEFAULT_DS_PORT = 9001;
  public static final String DEFAULT_VMM_IP = DEFAULT_SILO_SERVER_IP;
  public static final int DEFAULT_VMM_PORT = 9000;
  public static final int DEFAULT_SLAM_PORT = 9002;
  public static final int DEFAULT_BANDWIDTH_PORT = 4321;
  // public static final String DEFAULT_PRIMARY_ANIMATION_SERVER_IP = "192.168.0.212";
  public static final String DEFAULT_PRIMARY_ANIMATION_SERVER_IP = DEFAULT_SILO_SERVER_IP;
  public static final int DEFAULT_PRIMARY_ANIMATION_SERVER_PORT = 6666;
  public static final String DEFAULT_SECONDARY_ANIMATION_SERVER_IP = "192.168.0.3"; // FIXME put the
                                                                                    // IP of the
                                                                                    // macbook
                                                                                    // machine here
  public static final int DEFAULT_SECONDARY_ANIMATION_SERVER_PORT = 6665;
  public static final int DEFAULT_GVIRTUS_PORT = 9992;
  public static final int AC_RM_PORT_DEFAULT = 6543;
  public static final int REGISTER_WITH_QOS_PARAMS = 1;
  public static final int REGISTER_WITHOUT_QOS_PARAMS = 0;

  // TODO: check the real device name as returned by android for the HTC G1 phone
  public static final String PHONE_NAME_HTC_G1 = "HTC G1";
  public static final String PHONE_MODEL_HTC_DESIRE = "HTC Desire";
  public static final String PHONE_MODEL_SAMSUNG_GALAXY_S = "samsung GT-I9000";
  public static final String PHONE_MODEL_MOTOROLA_MOTO_G = "Motorola Moto G";

  // The constants of the configuration files
  public static final String DEMO_SERVER_IP = "[DEMO SERVER IP]";
  public static final String DEMO_SERVER_PORT = "[DEMO SERVER PORT]";
  public static final String DS_IP = "[DS IP]";
  public static final String DS_PORT = "[DS PORT]";
  public static final String GVIRTUS_IP = "[GVIRTUS IP]";
  public static final String GVIRTUS_PORT = "[GVIRTUS PORT]";
  public static final String MANAGER_IP = "[MANAGER IP]";
  public static final String MANAGER_PORT = "[MANAGER PORT]";
  public static final String CLONE_TYPES = "[CLONE TYPES]"; // Type has to be
  // one of:
  // Local,
  // Amazon,
  // or Hybrid
  public static final String NR_CLONES_KVM_TO_START = "[NUMBER OF KVM CLONES TO START ON STARTUP]";
  public static final String NR_CLONES_VB_TO_START = "[NUMBER OF VB CLONES TO START ON STARTUP]";
  public static final String NR_CLONES_AMAZON_TO_START =
      "[NUMBER OF AMAZON CLONES TO START ON STARTUP]";
  public static final String KVM_CLONES = "[KVM CLONES]";
  public static final String VB_CLONES = "[VIRTUALBOX CLONES]";
  public static final String AMAZON_CLONES = "[AMAZON CLONES]";
  public static final String CLONE_PORT = "[CLONE PORT]";
  public static final String CLONE_SSL_PORT = "[CLONE SSL PORT]";
  public static final String CLONE_BW_TEST_PORT = "[CLONE BW TEST PORT]";
  public static final String CLONE_NAME = "[CLONE NAME]";
  public static final String CLONE_ID = "[CLONE ID]";

  public static final String MANAGER_CONFIG_FILE = "config-manager.cfg";
  public static final String DS_CONFIG_FILE = "config-ds.cfg";

  // public static final int COMM_CLEAR = 1;
  // public static final int COMM_SSL = 2;

  public static enum COMM_TYPE {
    CLEAR, SSL
  }

  public static enum ExecLocation {
    LOCAL, REMOTE, DYNAMIC, DYNAMIC_TIME, DYNAMIC_ENERGY, HYBRID
  }

  public static enum REGIME {
    AC, AS
  }

  // supported OS in the cloud
  public static enum OS {
    LINUX, ANDROID
  }
}
