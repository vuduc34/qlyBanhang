package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ResponMessage;
import project.psa.dataserver.service.fileService;

import java.io.IOException;

@RestController
@RequestMapping(constant.API.PREFIX)
public class fileController {
    @Autowired
    private fileService fileService;
    @PostMapping(value = "/file/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponMessage uploadImage(@RequestParam("file") MultipartFile file) {
        ResponMessage responMessage = new ResponMessage();
        try {
            String filename = fileService.saveImage(file);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(filename);
            return responMessage;
        } catch ( IOException e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
            return responMessage;
        }
    }

    // API lấy ảnh theo tên file
    @GetMapping("/auth/file/getImage/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            byte[] image = fileService.getImage(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

//    @PostMapping(value = "/file/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponMessage uploadFile(@RequestParam("file") MultipartFile file) {
//        ResponMessage responMessage = new ResponMessage();
//        try {
//            String filename = fileService.saveFile(file);
//            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
//            responMessage.setMessage(constant.MESSAGE.SUCCESS);
//            responMessage.setData(filename);
//            return responMessage;
//        } catch ( IOException e) {
//            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
//            responMessage.setMessage(constant.MESSAGE.ERROR);
//            responMessage.setData(e.getMessage());
//            return responMessage;
//        }
//    }
//
//    // API lấy ảnh theo tên file
//    @GetMapping("/file/getFile/{filename}")
//    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
//        try {
//            byte[] file = fileService.getFile(filename);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getFileName(filename))
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(file);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//    public String getFileName(String fileName) {
//        String[] parts = fileName.split("_original_");
//        if (parts.length > 1) {
//            return parts[1]; // Lấy phần sau "_original_"
//        }
//        return fileName; // Trả về nguyên chuỗi nếu không tìm thấy "_original_"
//    }
}
