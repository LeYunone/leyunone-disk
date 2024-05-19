package com.leyunone.disk.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/5/16 16:24
 */
public class DownloadThread implements Runnable {

    private Long start;
    private Long end;
    private OutputStream os;
    private File file;

    public DownloadThread(Long start, Long end, OutputStream os, File file) {
        this.start = start;
        this.end = end;
        this.os = os;
        this.file = file;
    }

    @Override
    public void run() {
        if (start == null || end == null || os == null || !file.exists()) {
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            byte[] bf = new byte[1024];
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(start);
            long sum = 0L;
            while (sum <= end - start) {
                int len = randomAccessFile.read(bf);
                os.write(len);
                sum += len;
            }
        } catch (Exception e) {
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
