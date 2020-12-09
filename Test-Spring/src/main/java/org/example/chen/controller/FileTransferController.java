package org.example.chen.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.example.chen.http.HttpUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;

@RestController
@Slf4j
public class FileTransferController {

    /**
     * 文件下载2
     * @return
     * @throws Exception
     */
    @GetMapping("/file/getText1")
    public ResponseEntity<FileSystemResource> getText() throws  Exception{
        log.info("获取文件");
        File file = new File("./temp/test.txt");
        FileUtils.write(file, "fdafdxcxdsas", "utf-8");
        return HttpUtils.export(file);
    }

    /**
     * 文件下载2
     * @return
     * @throws Exception
     */
    @GetMapping("/file/getText2")
    public void getText(HttpServletResponse response) throws  Exception{
        log.info("获取文件2");
        File file = new File("./temp/test.txt");
        FileUtils.write(file, "fdafdxcxdsas", "utf-8");
        response.setContentType("application/x-download;");
        response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
        try (OutputStream out = response.getOutputStream()) {
            StreamUtils.copy(FileUtils.openInputStream(file), out);
        }
        return;
    }
}
