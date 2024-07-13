package com.lifedrained.injector.injector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class VACBPloader {
    private static final Logger log = LogManager.getLogger(VACBPloader.class);
    private final String localpath = Paths.get("src","main","resources","com","lifedrained","injector",
            "injector","VACBP.exe").toString();
    private ProcessBuilder pb;
    public VACBPloader() {

    }
    public void launchVACBP() {
        pb = new ProcessBuilder(localpath);
        try{
        pb.start();
        }catch(IOException e){
            log.error(e);
        }
    }
}
