package com.example.loan.service;

import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    /**
     * 파일 업로드 기능
     */
    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(),
                    Paths.get(uploadPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    /**
     * 대출 신청 서류 다운로드
     */
    @Override
    public Resource load(String fileName) {
        try {
            Path file = Paths.get(uploadPath).resolve(fileName);

            Resource resource = new UrlResource(file.toUri());

            if (resource.isReadable() || resource.exists()) {
                return resource;
            } else { // 해당 경로에 파일이 존재하지 않는 경우, 에러 발생
                throw new BaseException(ResultType.NOT_EXIST_FILE);
            }

        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        // 신청 서류에 대한 정보 받아오기
        try {
            // 해당 uploadPath 하위에 있는 파일들만 조회
            // 디렉토리 같이 조회 안되게 filter 적용
            return Files.walk(Paths.get(uploadPath), 1).filter(
                    path -> !path.equals(Paths.get(uploadPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    /**
     * 대출 신청 서류 전체 삭제
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
    }
}
