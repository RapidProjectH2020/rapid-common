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
 * Control Messages for client-server communication Message IDs up to 255 - one byte only, as they
 * are sent over sockets using write()/read() - only one byte read/written.
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
	public static final int AC_REGISTER_SLAM = 36;
	public static final int AS_RM_REGISTER_DS = 37;
	public static final int AS_RM_REGISTER_VMM = 38;
	public static final int AS_RM_NOTIFY_VMM = 39;
	public static final int CLONE_AUTHENTICATION = 40;
	public static final int MANAGER_AUTHENTICATION = 41;
	public static final int GET_ASSOCIATED_CLONE_INFO = 42;
	public static final int GET_NEW_CLONE_INFO = 43;
	public static final int GET_PORT = 44;
	public static final int GET_SSL_PORT = 45;
	public static final int DOWNLOAD_FILE = 146;
	public static final int UPLOAD_FILE = 147;
	public static final int UPLOAD_FILE_RESULT = 48;
	public static final int DATA_RATE_THROTTLE = 49;
	public static final int DATA_RATE_UNTHROTTLE = 50;
	public static final int FORWARD_REQ = 51;
	public static final int FORWARD_START = 52;
	public static final int FORWARD_END = 53;
	public static final int PARALLEL_REQ = 54;
	public static final int PARALLEL_START = 55;
	public static final int PARALLEL_END = 56;

	// Demo server registers with the DS so that the others can find its IP
	public static final int DEMO_SERVER_REGISTER_DS = 80;

	//  Everyone to DS for asking for demo animation ip
	public static final int GET_DEMO_SERVER_IP_DS = 81;

	// Communication SLAM <-> VMM
	public static final int SLAM_START_VM_VMM = 60;
	public static final int VMM_REGISTER_SLAM = 61;
	public static final int SLAM_GET_VMCPU_VMM = 62;
	public static final int SLAM_GET_VMINFO_VMM = 63;

	// Communication DS <-> VMM
	public static final int VMM_REGISTER_DS = 70;
	public static final int VMM_NOTIFY_DS = 71;
	public static final int VM_REGISTER_DS = 72;
	public static final int VM_NOTIFY_DS = 73;
	public static final int HELPER_NOTIFY_DS = 74;
	public static final int DS_VM_DEREGISTER_VMM = 75;
	
	// Communication DS <-> SLAM
	public static final int SLAM_REGISTER_DS = 90;

	// Communication Phone <-> Registration Manager
	public static final int AC_HELLO_AC_RM = 1;

	// Messages to send to the demo server, which will create the animation
	// of the events happening on the system
	public enum AnimationMsg {
		
		PING,

		// Scenario 1: DS, SLAM, VMM starting up
		INITIAL_IMG_0,
		DS_UP,
		SLAM_UP,
		SLAM_REGISTER_DS,
		VMM_UP,
		VMM_REGISTER_DS,
		VMM_REGISTER_SLAM,

		// Scenario 2: 
		AC_INITIAL_IMG,
		AC_NEW_REGISTER_DS,
		DS_NEW_FIND_MACHINES,
		DS_NEW_IP_LIST_AC,
		AC_NEW_REGISTER_SLAM,
		SLAM_NEW_VM_VMM,
		VMM_NEW_START_VM,
		VMM_NEW_REGISTER_AS,
		VMM_NEW_VM_REGISTER_DS,
		VMM_NEW_VM_IP_SLAM,
		SLAM_NEW_VM_IP_AC,
		AC_NEW_REGISTER_VM,
		AC_NEW_CONN_VM,
		AC_NEW_APK_VM,
		AC_NEW_RTT_VM,
		AC_NEW_DL_RATE_VM,
		AC_NEW_UL_RATE_VM,
		AC_NEW_REGISTRATION_OK_VM,

		// Scenario 3: 
		AC_PREV_REGISTRATION_OK_VM,
		AC_PREV_UL_RATE_VM,
		AC_PREV_DL_RATE_VM,
		AC_PREV_RTT_VM,
		AC_PREV_APK_VM,
		AC_PREV_CONN_VM,
		AC_PREV_REGISTER_VM,
		AC_REGISTER_VM_ERROR,
		SLAM_PREV_VM_IP_AC,
		VMM_PREV_VM_IP_SLAM,
		VMM_PREV_FIND_VM,
		SLAM_PREV_VM_REQ_VMM,
		AC_PREV_REGISTER_SLAM,
		DS_PREV_IP_AC,
		DS_PREV_FIND_MACHINE,
		AC_PREV_VM_DS,
		//	AC_INITIAL_IMG

		// Scenario 4: offload execution
		AC_OFFLOADING_FINISHED,
		AS_RESULT_AC,
		AS_RUN_METHOD,
		AC_DECISION_OFFLOAD_AS,
		AC_PREPARE_DATA,
		//	AC_INITIAL_IMG,

		//	Scenario 5: local execution

		AC_LOCAL_FINISHED,
		AC_DECISION_LOCAL,
		//AC_PREPARE_DATA,
		//AC_INITIAL_IMG,
		
		// Scenario 6: D2D 1

		AC_OFFLOADING_FINISHED_D2D,
		AS_RESULT_AC_D2D,
		AS_RUN_METHOD_D2D,
		AC_OFFLOAD_D2D,
		AC_PREPARE_DATA_D2D,
		
		//Scenario 7: D2D 2
		
		AC_RECEIVED_D2D,
		AC_NO_MORE_D2D,
		AS_BROADCASTING_D2D,
		AC_LISTENING_D2D,
		D2D_INITIAL_IMG,
	}
}
