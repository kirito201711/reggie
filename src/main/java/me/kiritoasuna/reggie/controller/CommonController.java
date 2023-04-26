package me.kiritoasuna.reggie.controller;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.kiritoasuna.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
/**
 *
 * 文件上传与下载
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {      // 参数名必须和前端名称一致
    log.info(file.toString());
        final var fileOriginalFilename = file.getOriginalFilename();    //  获取文件名
        final var suffix = fileOriginalFilename.substring(fileOriginalFilename.lastIndexOf("."));  //获取文件后缀
        String filename = UUID.randomUUID().toString()+suffix;
        File dir = new File(basePath);
        if (!dir.exists()){
            //，目录不存在，创建
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);
    }
    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {

            final var fileInputStream = new FileInputStream(new File(basePath+name));            //输入流,通过输入流读取文件内容
            ServletOutputStream outputStream = response.getOutputStream();// 输出流,通过输出流将文件写回浏览器，在浏览器展示图片了
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len =  fileInputStream.read(bytes))!= -1){
                 outputStream.write(bytes,0,len);
                 outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //输出流,通过输出流将文件
    }
}
