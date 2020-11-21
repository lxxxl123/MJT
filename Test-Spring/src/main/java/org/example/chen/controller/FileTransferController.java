package org.example.chen.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.example.chen.http.HttpUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@Slf4j
public class FileTransferController {

    /**
     * 文件下载
     * @return
     * @throws Exception
     */
    @GetMapping("/file/getText")
    public ResponseEntity<FileSystemResource> getText() throws  Exception{
        log.info("获取文件");
        File file = new File("./temp/test.txt");
        FileUtils.write(file, "fdafdxcxdsas", "utf-8");
        return HttpUtils.export(file);
    }
}
