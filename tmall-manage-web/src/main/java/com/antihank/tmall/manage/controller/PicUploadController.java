package com.antihank.tmall.manage.controller;

import com.antihank.tmall.manage.vo.PicUploadResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/5.
 */
@Controller
@RequestMapping("/pic")
public class PicUploadController implements Serializable {
    private static final String[] PICTURE_TYPE_ALLOW = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final long serialVersionUID = 3598670525806638591L;
    @Value("${TMALL_IMAGE_PATH}")
    private String imagePath;


    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String uploadPicture(@RequestParam("uploadFile") MultipartFile file) throws JsonProcessingException {

        PicUploadResult result = new PicUploadResult();
        result.setError(1);

        //校验文件名
        String originalFilename = file.getOriginalFilename();
        boolean flag = false;

        for (String s : PICTURE_TYPE_ALLOW) {
            if (originalFilename.endsWith(s)) {
                flag = true;
                break;
            }
        }

        //校验文件内容
        if (flag) {
            try {
                //读取图片
                BufferedImage img = ImageIO.read(file.getInputStream());

                //上传图片
                String path = this.getClass().getClassLoader().getResource("tracker.conf").toString();
                if (path.split(":").length > 2) {
                    path = path.replace("file:/", "");
                } else {
                    path = path.replace("file:", "");

                }
                //准备工作
                ClientGlobal.init(path);

                TrackerClient trackerClient = new TrackerClient();
                TrackerServer trackerServer = trackerClient.getConnection();
                StorageServer storageServer = null;
                StorageClient storageClient = new StorageClient(trackerServer, storageServer);

                //存储文件
                byte[] bytes = file.getBytes();
                String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
                String[] back = storageClient.upload_file(bytes, ext, null);

                StringBuilder pathToFile = new StringBuilder(imagePath);
                //拼接路径
                for (String s : back) {
                    pathToFile.append("/");
                    pathToFile.append(s);
                }
                result.setUrl(pathToFile.toString());
                result.setError(0);
            } catch (IOException | MyException e) {

                e.printStackTrace();
            }
        }
        //写出为json格式的纯字符串
        return mapper.writeValueAsString(result);
    }
}
