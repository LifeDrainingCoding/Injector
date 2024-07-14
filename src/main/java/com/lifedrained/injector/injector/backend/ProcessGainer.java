package com.lifedrained.injector.injector.backend;
import com.lifedrained.injector.injector.jnainterfaces.*;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static com.sun.jna.platform.win32.WinGDI.BI_RGB;

public class ProcessGainer {
    private static final Logger log = LogManager.getLogger(ProcessGainer.class);

    public ArrayList<ProcessData> listProcesses(){
        int maxProcesses = 1024;
        int[] pids = new int[maxProcesses];
        ArrayList<ProcessData> processDataList =  new ArrayList<>();
        IntByReference pBytesReturned = new IntByReference();
        if(Psapi.INSTANCE.EnumProcesses(pids,pids.length*4,pBytesReturned)){
            int numberOfProcesses = pBytesReturned.getValue()/4;
            log.debug("Number of processes in the system: {}", numberOfProcesses);
            for (int i = 0; i<numberOfProcesses; i++){
                int pid = pids[i];
                String processName = getProcessName(pid);
                String processPath= getProcessPath(pid);
                BufferedImage icon = getProcessIcon(processPath);

                Image image = null;
                try{

                    image = SwingFXUtils.toFXImage(icon, null);
                } catch (Exception e){
                    log.error(e);
                }
                ProcessData data =  new ProcessData(processName,pid,image, processPath);
                if(!data.getName().equals("N/A")){
                    processDataList.add(data);
                }
            }

        }
        return processDataList;
    }
    private String getProcessName(int pid){
        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION |
                Kernel32.PROCESS_VM_READ,false,pid);
        if(hProcess != null){
            try{
                char[] processName = new char[512];
                if(Psapi.INSTANCE.GetProcessImageFileName(hProcess,processName,processName.length)>0){
                    return Native.toString(processName);

                }
            }finally {
                Kernel32.INSTANCE.CloseHandle(hProcess);
            }
        }
        return "N/A";
    }
    private String getProcessPath(int pid){
        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION
                | Kernel32.PROCESS_VM_READ,false,pid);
        if(hProcess != null){
            try{
                WinDef.HMODULE[] hmodule = new WinDef.HMODULE[1];
                IntByReference lpcbNeeded = new IntByReference();
                if(Psapi.INSTANCE.EnumProcessModules(hProcess,hmodule,hmodule.length* Native.POINTER_SIZE,lpcbNeeded)){

                    Pointer pathPointer = new Memory(1024);
                    Psapi.INSTANCE.GetModuleFileNameEx(hProcess,hmodule[0],pathPointer, 1024);
                    return pathPointer.getString(0);

                }
            }finally {
                Kernel32.INSTANCE.CloseHandle(hProcess);
            }
        }
        return "N/A";
    }
    private BufferedImage getProcessIcon(String path){
        if(path==null || path.isEmpty()|| !new File(path).exists()){
            return null;
        }
        WinDef.HICON[] smallIcon = new WinDef.HICON[1];
        WinDef.HICON[] bigIcon = new WinDef.HICON[1];
        int nIcons = Shell32.INSTANCE.ExtractIconEx(path,0,bigIcon,smallIcon,1 );
        if(nIcons>0 && bigIcon[0]!=null){
            try {
                return convertIconToImage(bigIcon[0]);
            }catch (Exception ex){
                log.error("error during converting Icon to Image",ex);
            }
        }
        return null;
    }
    private BufferedImage convertIconToImage(WinDef.HICON icon){
        WinGDI.ICONINFO  iconinfo = new WinGDI.ICONINFO();
        if(User32.INSTANCE.GetIconInfo(icon, iconinfo)){
            WinDef.HBITMAP hbitmap = iconinfo.hbmColor;
            try{

                return convertHBITMAPtoImage(hbitmap);
            }catch (Exception ex){
                log.error("error during converting HBITMAP to image" ,ex);
            }
        }
        return null;
    }
    private BufferedImage convertHBITMAPtoImage(WinDef.HBITMAP hbitmap){
        WinGDI.BITMAP bmp =  new WinGDI.BITMAP();
        if(User32Ex.INSTANCE.GetObject(hbitmap,bmp.size(),bmp )){
            int width = bmp.bmWidth.intValue();
            int height = Math.abs(bmp.bmHeight.intValue());
            Pointer pixels =  new Memory((long) width *height);

            WinGDI.BITMAPINFO bi =  new WinGDI.BITMAPINFO();
            bi.bmiHeader.biSize = bi.size();
            bi.bmiHeader.biHeight = -height;
            bi.bmiHeader.biWidth = width;
            bi.bmiHeader.biPlanes = 1;
            bi.bmiHeader.biBitCount = 32;
            bi.bmiHeader.biCompression = BI_RGB;
            if(GDI32.INSTANCE.GetDIBits(User32.INSTANCE.GetDC(null),hbitmap,0,height,
                    pixels,bi, WinGDI.DIB_RGB_COLORS)>0){
                BufferedImage image =  new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
                image.setRGB(0,0,width,height,pixels.getIntArray(0,width*height),
                        0, width);
                return image;

            }

        }
        return null;
    }
}
