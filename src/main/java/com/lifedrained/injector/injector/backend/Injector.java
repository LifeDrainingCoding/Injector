package com.lifedrained.injector.injector.backend;

import com.lifedrained.injector.injector.jnainterfaces.Kernel32Ext;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.BaseTSD.DWORD_PTR;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinNT.*;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Pointer;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.lifedrained.injector.injector.backend.Singleton.*;

public class Injector {

	private static final Logger log = LogManager.getLogger(Injector.class);
	private static Kernel32Ext kernel32 =  Native.load("kernel32.dll", Kernel32Ext.class, W32APIOptions.ASCII_OPTIONS);

    

	
	public  boolean inject(int processID, String dllName) {
		DWORD_PTR processAccess = new DWORD_PTR(0x43A);
		
		HANDLE hProcess = kernel32.OpenProcess(processAccess, new BOOL(false), new DWORD_PTR(processID));
		if(hProcess == null) {
            log.error("Handle was NULL! Error: {}", kernel32.GetLastError());
			setVisibleError("Handle was NULL! Error: "+String.valueOf(kernel32.GetLastError()));
			return false;
		}
		
		DWORD_PTR loadLibraryAddress = kernel32.GetProcAddress(kernel32.GetModuleHandle("KERNEL32"), "LoadLibraryA");
		if(loadLibraryAddress.intValue() == 0) {
            log.error("Could not find LoadLibrary! Error: {}", kernel32.GetLastError());
			setVisibleError("Could not find LoadLibrary! Error: "+String.valueOf(kernel32.GetLastError()));
			return false;
		}
		
		LPVOID dllNameAddress = kernel32.VirtualAllocEx(hProcess, null, (dllName.length() + 1), new DWORD_PTR(0x3000), new DWORD_PTR(0x4));
		if(dllNameAddress == null) {
            log.error("dllNameAddress was NULL! Error: {}", kernel32.GetLastError());
			setVisibleError("dllNameAddress was NULL! Error: "+String.valueOf(kernel32.GetLastError()));
			return false;
		}

		Pointer m = new Memory((dllName.length() + 1)*2L);
		m.setString(0, dllName); 

		boolean wpmSuccess = kernel32.WriteProcessMemory(hProcess, dllNameAddress, m, dllName.length(), null).booleanValue();
		if(!wpmSuccess) {
            log.error("WriteProcessMemory failed! Error: {}", kernel32.GetLastError());
			setVisibleError("WriteProcessMemory failed! Error: "+String.valueOf(kernel32.GetLastError()));
			return false;
		}
		
		DWORD_PTR threadHandle = kernel32.CreateRemoteThread(hProcess, 0, 0, loadLibraryAddress, dllNameAddress, 0, 0);			
		if(threadHandle.intValue() == 0) {
            log.error("threadHandle was invalid! Error: {}", kernel32.GetLastError());
			setVisibleError("threadHandle was invalid! Error: "+String.valueOf(kernel32.GetLastError()));
			return false;
		}
		
		kernel32.CloseHandle(hProcess);
		
		return true;
	}
	private static void setVisibleError(String errorMessage){
		getInstance().getDebugLbl().setVisible(true);
		getInstance().getCopyBtn().setVisible(true);
		getInstance().getCopyBtn().setDisable(!true);
		Alert alert = new Alert(Alert.AlertType.ERROR,"An error occured!\n see error log the right part of injector.",
				ButtonType.CLOSE);
		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
			@Override
			public void handle(DialogEvent event) {
				getInstance().getDebugLbl().setText(errorMessage);
			}
		});
		alert.showAndWait();
	}

}
