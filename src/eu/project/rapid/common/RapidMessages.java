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

/**
 * Control Messages for client-server communication Message IDs up to 255 - one
 * byte only, as they are sent over sockets using write()/read() - only one byte
 * read/written.
 */
public class RapidMessages {
	// Generic Messages
	public static final int OK = 1;
	public static final int ERROR = 0;
	public static final int PING = 11;
	public static final int PONG = 12;

	// Communication Phone <-> Clone
	public static final int AC_REGISTER_AS = 13;
	public static final int AS_APP_PRESENT_AC = 14;
	public static final int AS_APP_REQ_AC = 15;
	public static final int AC_OFFLOAD_REQ_AS = 16;

	// Communication Phone <-> Clone (for testing purposes)
	public static final int SEND_INT = 17;
	public static final int SEND_BYTES = 18;
	public static final int RECEIVE_INT = 19;
	public static final int RECEIVE_BYTES = 20;

	// Communication Main Clone <-> Helper Clone
	public static final int CLONE_ID_SEND = 21;

	// Communication Phone/Clone <-> DS/Manager
	public static final int PHONE_CONNECTION = 30;
	public static final int CLONE_CONNECTION = 31;
	public static final int MANAGER_CONNECTION = 32;
	public static final int PHONE_AUTHENTICATION = 33;
	public static final int AC_REGISTER_NEW_DS = 34;
	public static final int AC_REGISTER_PREV_DS = 35;
	public static final int AS_RM_REGISTER_DS = 36;
	public static final int AS_RM_REGISTER_SLAM = 37;
	public static final int CLONE_AUTHENTICATION = 38;
	public static final int MANAGER_AUTHENTICATION = 39;
	public static final int GET_ASSOCIATED_CLONE_INFO = 40;
	public static final int GET_NEW_CLONE_INFO = 41;
	public static final int GET_PORT = 42;
	public static final int GET_SSL_PORT = 43;
	public static final int DOWNLOAD_FILE = 44;
	public static final int UPLOAD_FILE = 45;
	public static final int UPLOAD_FILE_RESULT = 46;
	public static final int DATA_RATE_THROTTLE = 47;
	public static final int DATA_RATE_UNTHROTTLE = 48;
	public static final int FORWARD_REQ = 49;
	public static final int FORWARD_START = 50;
	public static final int FORWARD_END = 51;
	public static final int PARALLEL_REQ = 52;
	public static final int PARALLEL_START = 53;
	public static final int PARALLEL_END = 54;

	// Communication Phone <-> SLAM
	public static final int AC_REGISTER_SLAM = 60;

	// Communication SLAM <-> VMM
	public static final int SLAM_START_VM_VMM = 70;
	public static final int SLAM_QUERY_IP_VMM = 71;

	// Communication DS <-> VMM
	public static final int VMM_REGISTER_DS = 80;
	public static final int VMM_NOTIFY_DS = 81;
	public static final int VM_REGISTER_DS = 82;
	public static final int VM_NOTIFY_DS = 83;
	public static final int HELPER_NOTIFY_DS = 84;
	public static final int DS_VM_DEREGISTER_VMM = 85;

	// Communication Phone/Clone <-> VMM
	public static final int AS_RM_REGISTER_VMM = 90;
	public static final int AS_RM_NOTIFY_VMM = 91;
	
	// Communication Phone <-> Registration Manager
	public static final int AC_HELLO_AC_RM = 1;

	// Messages to send to the demo server, which will create the animation
	// of the events happening on the system
	public static final String DS_UP = "DS_UP";

	// The VMM is started, registers with the DS and starts two VMs
	public static final String VMM_UP = "VMM_UP";
	public static final String VMM_REGISTER_DS_MSG = "VMM_REGISTER_DS"; /* name is changed from VMM_REGISTER_DS to VMM_REGISTER_DS_MSG to avoid name conflict */
	public static final String VMM_REGISTER_DS_OK = "VMM_REGISTER_DS_OK";
	public static final String VMM_START_TWO_VMS = "VMM_START_TWO_VMS";
	public static final String VMM_VM1_STARTED = "VMM_VM1_STARTED";
	public static final String VMM_VM2_STARTED = "VMM_VM2_STARTED";

	// The Acceleration Client (AC) on the User Device (UD) registers
	// as a NEW device with the DS and the VMM and asks for a VM
	public static final String AC_REGISTER_DS_NEW = "AC_REGISTER_DS_NEW";
	public static final String DS_FIND_AVAILABLE_VMM = "DS_FIND_AVAILABLE_VMM";
	public static final String AC_REGISTER_DS_NEW_OK = "AC_REGISTER_DS_NEW_OK";
	public static final String AC_REGISTER_VMM_NEW = "AC_REGISTER_VMM_NEW";
	public static final String VMM_FIND_AVAILABLE_VM = "VMM_FIND_AVAILABLE_VM";
	public static final String AC_REGISTER_VMM_NEW_OK = "AC_REGISTER_VMM_NEW_OK";

	// The Acceleration Client (AC) on the User Device (UD) registers
	// as a PREV device with the DS and the VMM and asks for a VM
	public static final String AC_REGISTER_DS_PREV = "AC_REGISTER_DS_PREV";
	public static final String DS_FIND_PREV_VMM = "DS_FIND_PREV_VMM";
	public static final String AC_REGISTER_DS_PREV_OK = "AC_REGISTER_DS_PREV_OK";
	public static final String AC_REGISTER_VMM_PREV = "AC_REGISTER_VMM_PREV";
	public static final String VMM_FIND_PREV_VM = "VMM_FIND_PREV_VM";
	public static final String AC_REGISTER_VMM_PREV_OK = "AC_REGISTER_VMM_PREV_OK";

	// The Acceleration Client (AC) on the User Device (UD) registers with the
	// AS on the VM
	// The registration phase goes through, connection, RTT, bandwidth
	// measurements, etc.
	public static final String AC_REGISTER_VM = "AC_REGISTER_VM";
	public static final String AC_CONNECT_VM = "AC_CONNECT_VM";
	public static final String AC_SEND_APK = "AC_SEND_APK";
	public static final String AC_RTT_MEASUREMENT = "AC_RTT_MEASUREMENT";
	public static final String AC_DL_MEASUREMENT = "AC_DW_MEASUREMENT";
	public static final String AC_UL_MEASUREMENT = "AC_UW_MEASUREMENT";
	public static final String AC_REGISTER_VM_OK = "AC_REGISTER_VM_OK";
	public static final String AC_DISCONNECT_VM = "AC_DISCONNECT_VM";

	// The method is run locally
	public static final String AC_DECISION_LOCAL = "AC_DECISION_LOCAL";
	public static final String AC_PREPARE_DATA = "AC_PREPARE_DATA";
	public static final String AC_EXEC_LOCAL = "AC_EXEC_LOCAL";
	public static final String AC_FINISHED_LOCAL = "AC_FINISHED_LOCAL";

	// The method is run remotely
	public static final String AC_DECISION_REMOTE = "AC_DECISION_REMOTE";
	public static final String AC_REMOTE_SEND_DATA = "AC_REMOTE_SEND_DATA";
	public static final String AC_EXEC_REMOTE = "AC_EXEC_REMOTE";
	public static final String AC_RESULT_REMOTE = "AC_RESULT_REMOTE";
	public static final String AC_FINISHED_REMOTE = "AC_FINISHED_REMOTE";

}
