package project.psa.dataserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class fileService {

    @Value("${image.upload-dir}")
    private String uploadImageDir;
//    @Value("${file.upload-dir}")
//    private String uploadFileDir;
    public String saveImage(MultipartFile file) throws IOException {
        // Kiểm tra thư mục tồn tại, nếu không thì tạo
        File folder = new File(uploadImageDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Đổi tên file tránh trùng lặp
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = uploadImageDir + File.separator + filename;

        // Lưu file vào thư mục
        file.transferTo(new File(filePath));

        return filename; // Trả về tên file để truy xuất sau này
    }

    public byte[] getImage(String filename) throws IOException {
        String filePath = uploadImageDir + File.separator + filename;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filename);
        }
        return Files.readAllBytes(Paths.get(filePath));
    }

//    public String saveFile(MultipartFile file) throws IOException {
//        // Kiểm tra thư mục tồn tại, nếu không thì tạo
//        File folder = new File(uploadFileDir);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//
//        // Đổi tên file tránh trùng lặp
//        String filename = UUID.randomUUID().toString() + "_original_" + file.getOriginalFilename();
//        String filePath = uploadFileDir + File.separator + filename;
//
//        // Lưu file vào thư mục
//        file.transferTo(new File(filePath));
//
//        return filename; // Trả về tên file để truy xuất sau này
//    }
//
//    public byte[] getFile(String filename) throws IOException {
//        String filePath = uploadFileDir + File.separator + filename;
//        File file = new File(filePath);
//        if (!file.exists()) {
//            throw new IOException("File not found: " + filename);
//        }
//        return Files.readAllBytes(Paths.get(filePath));
//    }

}
