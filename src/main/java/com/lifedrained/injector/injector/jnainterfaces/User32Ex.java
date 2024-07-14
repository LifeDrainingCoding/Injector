package com.lifedrained.injector.injector.jnainterfaces;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.win32.W32APIOptions;

public interface User32Ex extends User32 {
    User32Ex INSTANCE = Native.load("user32", User32Ex.class, W32APIOptions.UNICODE_OPTIONS);
    boolean GetObject(HBITMAP hbitmap, int cbBuffer, WinGDI.BITMAP bitmap);
}
