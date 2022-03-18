package com.cqc.tank.util;

import com.cqc.tank.config.ThreadPoolConfig;
import com.cqc.tank.entity.enums.AudioTypeEnum;
import sun.audio.AudioPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 声音类
 * @author Cqc on 2022/2/13 10:25 下午
 */
public class AudioUtil {
    private static Map<AudioTypeEnum, InputStream> audioClipMap;

    private static String exePath = System.getProperty("exe.path");
    /**
     * 游戏道具路径
     */
    private static String addUrl = "resources/audio/add.wav";
    private static String blastUrl = "resources/audio/blast.wav";
    private static String fireUrl = "resources/audio/fire.wav";
    private static String hitUrl = "resources/audio/hit.wav";
    private static String startUrl = "resources/audio/start.wav";
    private static String gameOverUrl = "resources/audio/gameover.wav";

    static {
        if (Objects.nonNull(exePath)) {
            addUrl = exePath + addUrl;
            blastUrl = exePath + blastUrl;
            fireUrl = exePath + fireUrl;
            hitUrl = exePath + hitUrl;
            startUrl = exePath + startUrl;
            gameOverUrl = exePath + gameOverUrl;
        }
        audioClipMap = new HashMap<>();
        try {
            // 先将音频都加载到内存
            audioClipMap.put(AudioTypeEnum.ADD, new FileInputStream(addUrl));
            audioClipMap.put(AudioTypeEnum.BLAST, new FileInputStream(blastUrl));
            audioClipMap.put(AudioTypeEnum.FIRE, new FileInputStream(fireUrl));
            audioClipMap.put(AudioTypeEnum.HIT, new FileInputStream(hitUrl));
            audioClipMap.put(AudioTypeEnum.START, new FileInputStream(startUrl));
            audioClipMap.put(AudioTypeEnum.GAME_OVER, new FileInputStream(gameOverUrl));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步播放音频
     */
    public static void syncPlayAudio(AudioTypeEnum audioType) {
        try {
            AudioPlayer.player.start(new FileInputStream(getAudioType(audioType)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // public static void play(AudioTypeEnum audioTypeEnum) {
    //     try {
    //         // 获取音频输入流
    //         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(getAudioType(audioTypeEnum)));
    //         // 获取音频编码对象
    //         AudioFormat audioFormat = audioInputStream.getFormat();
    //         // 设置数据输入
    //         DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
    //         SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
    //         sourceDataLine.open(audioFormat);
    //         sourceDataLine.start();
    //         // 从输入流中读取数据发送到混音器
    //         int count;
    //         byte[] tempBuffer = new byte[1024];
    //         while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
    //             if (count > 0) {
    //                 sourceDataLine.write(tempBuffer, 0, count);
    //             }
    //         }
    //         // 清空数据缓冲,并关闭输入
    //         sourceDataLine.drain();
    //         sourceDataLine.close();
    //     } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * 异步播放音频
     */
    public static void asyncPlayAudio(AudioTypeEnum audioType) {
        ThreadPoolConfig.execute(() -> {
            syncPlayAudio(audioType);
        });
    }

    /**
     * 获取坦克的宽度
     *
     * @return
     */
    public static String getAudioType(AudioTypeEnum audioType) {
        String url = null;
        switch (audioType) {
            case ADD:
                url = addUrl;
                break;
            case BLAST:
                url = blastUrl;
                break;
            case FIRE:
                url = fireUrl;
                break;
            case HIT:
                url = hitUrl;
                break;
            case START:
                url = startUrl;
                break;
            case GAME_OVER:
                url = gameOverUrl;
                break;
            default:
        }
        return url;
    }
}
