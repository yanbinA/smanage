package com.temple.manage;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

@SpringBootTest
class ManageApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/video/527196772523712513/1620886514630/4hfkh0tx31mo/d7a28922da92460ff3509ab35c31c9df_w544_h960.mp4");
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/image/527196772523712513/1607579329376/425zxch73g8w/37cca0e22cc13914525490a42f58f60b_w828_h828.jpg");
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/video/527196772523712513/1620886514630/4hfkh0tx31mo/d7a28922da92460ff3509ab35c31c9df_w544_h960.mp4");
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/image/527196772523712513/1607579329376/425zxch73g8w/37cca0e22cc13914525490a42f58f60b_w828_h828.jpg");
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/video/527196772523712513/1620886514630/4hfkh0tx31mo/d7a28922da92460ff3509ab35c31c9df_w544_h960.mp4");
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/image/527196772523712513/1607579329376/425zxch73g8w/37cca0e22cc13914525490a42f58f60b_w828_h828.jpg");
        fileUnique("https://im0001-test.oss-accelerate.aliyuncs.com/video/536680642214240257/1640138267250/53in57hvgef4/1e688f228769ae639d362533cfc8fd6d_w720_h1280.mp4?x-oss-process=video/snapshot,t_1000,m_fast");
    }

    public static void fileUnique(String url) throws NoSuchAlgorithmException {
        long timeMillis = System.currentTimeMillis();
        MessageDigest md = MessageDigest.getInstance("MD5");
        CRC32 crc = new CRC32();
        try (InputStream inputStream = new URL(url).openStream()){
            System.out.println("download file  " + (System.currentTimeMillis() - timeMillis));
            byte[] buffer = new byte[1024 * 1024 * 10];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
//                md.update(buffer, 0, length);
//                crc.update(buffer, 0, length);
            }
            System.out.println("md5 file  " + (System.currentTimeMillis() - timeMillis));
            System.out.println("md5:" + Hex.encodeHexString(md.digest()));
            System.out.println("md5 file  " + (System.currentTimeMillis() - timeMillis));
            System.out.println("crc file  " + (System.currentTimeMillis() - timeMillis));
            System.out.println("crc:" + crc.getValue());
            System.out.println("crc file  " + (System.currentTimeMillis() - timeMillis));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("md5 used time " + (System.currentTimeMillis() - timeMillis));
    }
}
