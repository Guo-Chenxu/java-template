package com.guochenxu.javatemplate.utils;

import com.alibaba.excel.EasyExcel;
import com.guochenxu.javatemplate.annotation.ExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * excel工具类
 *
 * @author: guoch
 * @create: 2025-01-08 16:27
 * @version: 1.0
 */
@Slf4j
@Component
@ExecutionTime
public class ExcelUtil {

    @Resource
    private ALiYunUtil aLiYunUtil;

    public static void export(String fileName, String sheetName, Class<?> clazz, List data) {
        EasyExcel.write(fileName, clazz).sheet(sheetName).doWrite(data);
    }

    public static byte[] exportToByte(String fileName, String sheetName, Class<?> clazz, List data) {
        export(fileName, sheetName, clazz, data);
        Path path = Paths.get(fileName);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return null;
        }
    }

    public String exportToOss(String fileName, String sheetName, Class<?> clazz, List data) {
        byte[] bytes = exportToByte(fileName, sheetName, clazz, data);
        if (bytes == null) {
            log.error("文件导出失败");
            return null;
        }
        return aLiYunUtil.uploadFile(fileName, bytes);
    }

    public List<Object> importExcel(String fileName, Class<?> clazz) {
        return EasyExcel.read(fileName).head(clazz).sheet().doReadSync();
    }

    public List<Object> importExcel(InputStream inputStream, Class<?> clazz) {
        return EasyExcel.read(inputStream).head(clazz).sheet().doReadSync();
    }
}
