package com.cqc.tank.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 声音类
 * @author Cqc on 2022/2/13 10:25 下午
 */
public class AudioUtil {
    private static String blastUrl = "resources/audio/blast.wav";
    private static String exePath = System.getProperty("exe.path");

    static {
        if (Objects.nonNull(exePath)) {
            blastUrl = exePath + blastUrl;
        }
    }

    /**
     * 播放背景音乐
     */
    public static void playBgm() {
        new Thread(() -> {
            try {
                // 获取音频输入流
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(blastUrl));
                // 获取音频编码对象
                AudioFormat audioFormat = audioInputStream.getFormat();
                // 设置数据输入
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
                /*
                 * 从输入流中读取数据发送到混音器
                 */
                int count;
                byte tempBuffer[] = new byte[1024];
                while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (count > 0) {
                        sourceDataLine.write(tempBuffer, 0, count);
                    }
                }
                // 清空数据缓冲,并关闭输入
                sourceDataLine.drain();
                sourceDataLine.close();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }


}
