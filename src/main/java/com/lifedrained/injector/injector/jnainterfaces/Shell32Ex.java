package com.lifedrained.injector.injector.jnainterfaces;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.ShellAPI;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

public interface Shell32Ex extends ShellAPI {
    Shell32Ex INSTANCE = Native.load("shell32", Shell32Ex.class, W32APIOptions.UNICODE_OPTIONS);

    // ExtractIconEx retrieves one or more icons from the specified file.
    int ExtractIconEx(String lpszFile, int nIconIndex, WinDef.HICON[] phiconLarge, WinDef.HICON[] phiconSmall, int nIcons);
}
