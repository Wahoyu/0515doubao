package com.dtt.anms.syslog.processing.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DLog {
    private FileWriter fw = null;

    private String _fileName = "";

    private String _fileDir = "";

    private String _fileDirSub = "";

    private String fileEncode = "";

    public DLog(String _fileName, String _fileDir, String _fileDirSub) {
        // 文件名
        this._fileName = _fileName;
        // 文件路径
        this._fileDir = _fileDir;
        // 文件子路径
        this._fileDirSub = _fileDirSub;
    }

    private boolean fileInit() {

        try {
            // 系统字符编码
            fileEncode = System.getProperty("file.encoding");

            String fileDir = _fileDir;

            // 文件名按日期创建
            String fileName = getDateString(_fileName);

            // 子目录按日期创建
            if (_fileDirSub != null && !_fileDirSub.equals("")) {

                String fileDirSub = getDateString(_fileDirSub);

                fileDir = _fileDir + "/" + fileDirSub;
            }

            File dir = new File(fileDir);
            File logfile = new File(fileDir, fileName);


            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\n---------------------------------");
            stringBuffer.append("\n文件目录："+fileDir);
            stringBuffer.append("\n文件名称："+fileName);

            // 目录不存在创建目录
            if (!dir.exists()) {
                stringBuffer.append("\n创建目录");
                dir.mkdirs();
            }else{
                stringBuffer.append("\n目录已存在");
            }
            // 文件不存在创建文件
            if (!logfile.exists()) {
                stringBuffer.append("\n创建文件");
                logfile.createNewFile();
            }else{
                stringBuffer.append("\n文件已存在");
            }
            stringBuffer.append("\n---------------------------------");

            log.info(stringBuffer.toString());
            // 初始化文件
            fw = new FileWriter(logfile, true);
        } catch (Exception e) {
            log.error("创建文件异常", e);
            return false;
        }

        return true;
    }

    private String getDateString(String value) {
        // 判断值是否可以装换
        if (value == null || value.trim().length() == 0) {
            return "";
        }

        // 根据 字符串中包含的字符判断装替换成什么样的日期格式
        if (value.trim().lastIndexOf("$yyyyMMdd$") > -1) {
            // 年 月 日
            return value.replaceAll("\\$yyyyMMdd\\$", dateFormate("yyyyMMdd"));
        } else if (value.trim().lastIndexOf("$yyyyMM$") > -1) {
            // 年 月
            return value.replaceAll("\\$yyyyMM\\$", dateFormate("yyyyMM"));
        } else if (value.trim().lastIndexOf("$yyyy$") > -1) {
            // 年
            return value.replaceAll("\\$yyyyMM\\$", dateFormate("yyyy"));
        } else {
            // 按照原样返回
            return value;
        }
    }

    public synchronized void fileWrite(String str) {
        // 重发次数
        int count = 0;
        while (count < 2 && fileInit()) {
            try {
                fw.write(str + "\n\n");
                // fw.write(new String((str +
                // "\n\n").getBytes("GBK"),fileEncode));
                fw.flush();
                count = Integer.MAX_VALUE;
                log.info("写告警报文成功");
            } catch (Exception e) {
                count++;
                log.error("写告警报文异常", e);
            } finally {
                this.fileClose();
            }
        }
    }

    private void fileClose() {
        try {
            fw.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private String dateFormate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
}
