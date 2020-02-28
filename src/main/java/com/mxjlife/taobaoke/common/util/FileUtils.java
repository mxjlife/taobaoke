package com.mxjlife.taobaoke.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/** 
 * 类说明: 文件读取, 写入
 * @author mxj
 * @email xj.meng@sinowaycredit.com
 * @version 创建时间：2017年12月8日 上午9:54:18 
 */
@Slf4j
public class FileUtils {
    
    private static final int bufferSize = 1024;
    
    /**
     * 读取文本文件, 返回读取到的字符串
     * @param path 文件位置
     * @return
     */
    public static String readTextFile(String path){
        StringBuilder sb = new  StringBuilder();
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            //使用BOMInputStream自动去除UTF-8中的BOM！！！  
            reader = new InputStreamReader(new BOMInputStream(new FileInputStream(path)));
            br = new BufferedReader(reader);
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line.trim());
            }
        } catch (Exception e) {
            log.error("读取文件失败" + e.getMessage());
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Reader关闭失败" + e.getMessage());
                }
            }
            if(br != null){
                try {
                    br.close();
                } catch (Exception e2) {
                    log.error("BufferReader关闭失败" + e2.getMessage());
                }
            }
        }
        return sb.toString();
    }
    
    /**
     * 读取文本文件, 并将文本文件按行读取到list中
     * @param path
     * @return
     */
    public static List<String> readTextFileToList(String path){
        List<String> result = new ArrayList<String>();
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            //使用BOMInputStream自动去除UTF-8中的BOM！！！  
            reader = new InputStreamReader(new BOMInputStream(new FileInputStream(path)));
            br = new BufferedReader(reader);
            String line = null;
            while((line = br.readLine()) != null){
                result.add(line.trim());
            }
        } catch (Exception e) {
            log.error("读取文件失败" + e.getMessage());
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Reader关闭失败" + e.getMessage());
                }
            }
            if(br != null){
                try {
                    br.close();
                } catch (Exception e2) {
                    log.error("BufferReader关闭失败" + e2.getMessage());
                }
            }
        }
        return result;
    }
    
    
    /**
     * 读取二进制文件
     * @param path 文件位置
     * @return
     */
    public static byte[] readBinaryFile(String path){
        byte[] result = new byte[0];
        FileInputStream input = null;
        try {
            File file = new File(path);
            input = new FileInputStream(file);
            byte[] buffer = new byte[bufferSize];
            while (true) {
                int len = input.read(buffer);
                if (len == -1) {
                    break;
                }
                byte[] joinedArray = new byte[result.length + len];
                System.arraycopy(result, 0, joinedArray, 0, result.length);
                System.arraycopy(buffer, 0, joinedArray, result.length, len);
                result = joinedArray;
            }
        } catch (Exception e) {
            log.error("读取文件失败" + e.getMessage());
        } finally {
            try {
                if(input != null)
                    input.close();
            } catch (Exception e) {
                log.error("FileInputStream关闭失败" + e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * 从磁盘上将文件读取出来, 并转换为base64编码
     * @param path
     * @return
     * @throws Exception
     */
    public static String fileToBase64(String path){
        String result = null;
        File file = new File(path);
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            input.read(buffer);
            result = Base64.encodeBase64String(buffer);
        } catch (Exception e) {
            log.error("读取文件失败" + e.getMessage());
        }finally{
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("FileInputStream关闭失败" + e.getMessage());
                }
            }
        }
        return result;
    }
    
    /**
     * 写入二进制文件
     * @param binaryFile 文件的二进制编码
     * @param path 文件位置
     * @param fileName 文件名
     * @return
     */
    public static String writeBinaryFile(byte[] binaryFile, String path, String fileName){
        FileOutputStream output = null;
        try {
            (new File(path)).mkdirs();
            output = new FileOutputStream(path + "/" + fileName);
            output.write(binaryFile);
        } catch (Exception e) {
            log.error("写入文件失败" + e.getMessage());
        } finally {
            try {
                if(output != null)
                    output.close();
            } catch (Exception e) {
                log.error("FileOutputStream关闭失败" + e.getMessage());
            }
        }
        return path;
    }
    
    /**
     * 写入文本文件, 不断写入, 往里面追加
     * @param data 文件的二进制编码
     * @param path 文件位置
     * @param fileName 文件名
     * @return
     */
    public static String writeTextFile(String data, String path, String fileName){
        FileWriter writer = null;
        try {
            (new File(path)).mkdirs();
            writer = new FileWriter(path + "/" +  fileName, true);
            writer.write(data + System.lineSeparator());
            writer.flush();
        } catch (Exception e) {
            log.error("写入文件失败" + e.getMessage());
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (Exception e) {
                log.error("FileWriter关闭失败" + e.getMessage());
            }
        }
        return path + fileName;
    }
    
    /**
     * 写入文本文件, 不断写入, 往里面追加
     * @param data 文件的二进制编码
     * @param path 文件位置
     * @param fileName 文件名
     * @return
     */
    public static String writeTextFile(Collection<String> data, String path, String fileName){
        FileWriter writer = null;
        try {
            (new File(path)).mkdirs();
            writer = new FileWriter(path + "/" +  fileName, true);
            for (String str : data) {
                writer.write(str + System.lineSeparator());
            }
            writer.flush();
        } catch (Exception e) {
            log.error("写入文件失败" + e.getMessage());
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (Exception e) {
                log.error("FileWriter关闭失败" + e.getMessage());
            }
        }
        return path + fileName;
    }
    
    
    /**
     * 将文件转成base64 字符串
     * 
     * @param path 文件路径
     * @return
     * @throws Exception
     */

    public static String getBase64File(String path){
        String result = null;
        FileInputStream input = null;
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length()];
            input = new FileInputStream(file);
            input.read(buffer);
            result = Base64.encodeBase64String(buffer);
        } catch (Exception e){
            log.error("读取文件失败" + e.getMessage());
        } finally {
            try {
                if(input != null){
                    input.close();
                }
            } catch (IOException e) {
                log.error("FileInputStream关闭失败" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 将base64字符解码保存文件
     * 
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void saveBase64File(String base64Code, String targetPath){
        FileOutputStream out = null;
        try {
            File file = new File(targetPath);
            byte[] buffer = Base64.decodeBase64(base64Code);
            out = new FileOutputStream(file);
            out.write(buffer);
        } catch (Exception e){
            log.error("保存文件失败" + e.getMessage());
        } finally {
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                log.error("FileOutputStream关闭失败" + e.getMessage());
            }
        }
    }


    /**
     * 该方法用于将文件按照uuid的名字进行保存
     * @param stream
     * @param path
     * @param filename
     */
    public static boolean saveInputStream(InputStream stream, String path, String filename){
        boolean flag = false;
        FileOutputStream fs= null;
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        try {
            fs = new FileOutputStream( path + ""+ filename);

            byte[] buffer =new byte[1024];
            int len = 0;
            while ((len=stream.read(buffer))!=-1){
                fs.write(buffer,0,len);
                fs.flush();
            }
            flag = true;
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        } catch (IOException e) {
            System.out.println("文件写入失败");
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 文件下载对象
     * @param filePath 文件路径
     * @param fileName 下载文件时的文件名
     * @return
     */
    public static ResponseEntity<byte[]> fileResponseEntity(String filePath, String fileName){

        byte[] body = null;
        File file = new File(filePath);
        //获取文件
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);
        } catch (FileNotFoundException e) {
            log.error("要下载的文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //传入文件名为空时使用文件自己的名称
        if (StringUtils.isBlank(fileName)){
            fileName = file.getName();
        }
        HttpHeaders headers = new HttpHeaders();
        //设置文件类型
        headers.add("Content-Disposition", "attachment;filename=" + fileName);
//        headers.add("Content-Type", "application/octet-stream");
        headers.add("Content-Type", "application/octet-stream");
        //设置Http状态码
        HttpStatus statusCode = HttpStatus.OK;
        //返回数据
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        return entity;

    }

}
