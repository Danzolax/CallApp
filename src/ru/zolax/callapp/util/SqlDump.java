package ru.zolax.callapp.util;

import ru.zolax.callapp.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.TimerTask;

public class SqlDump extends TimerTask {

    @Override
    public void run() {
        try {
            System.out.println("#############3Backup db...#################");
            String executeCmd = Application.getInstance().getDatabase().dump();
            ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c", "cd C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin && " + executeCmd);
            builder.redirectErrorStream(true);
            Process runtimeProcess = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(runtimeProcess.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
            System.out.println("###########################################");
        } catch (IOException ex) {
            System.out.println( "Error at Backuprestore" + ex.getMessage());
        }
    }
}
