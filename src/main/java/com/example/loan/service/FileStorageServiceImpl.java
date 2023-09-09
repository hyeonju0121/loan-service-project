package com.example.loan.service;

import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
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

    private final ApplicationRepository applicationRepository;

    /**
     * 파일 업로드 기능
     */
    @Override
    public void save(Long applicationId, MultipartFile file) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_APPLICATION);
        }

        try {
            // applicationId 하위 경로 생성
            // ../dir/1
            // ../dir/2
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path directoryPath = Path.of(applicationPath);

            if (!Files.exists(directoryPath)) { // 하위 경로가 존재하지 않는 경우, 하위 경로 생성
                Files.createDirectory(directoryPath);
            }

            Files.copy(file.getInputStream(), Paths.get(applicationPath)
                            .resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    /**
     * 대출 신청 서류 다운로드
     */
    @Override
    public Resource load(Long applicationId, String fileName) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_APPLICATION);
        }

        try {
            String applicationPath = uploadPath.concat("/" + applicationId);

            Path file = Paths.get(applicationPath).resolve(fileName);
            // 웹 브라우저에서 사용하는 형식의 문자열 변환
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
    public Stream<Path> loadAll(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_APPLICATION);
        }

        // 신청 서류에 대한 정보 받아오기
        try {
            // 해당 applicationPath 하위에 있는 파일들만 조회
            // 디렉토리 같이 조회 안되게 filter 적용
            String applicationPath = uploadPath.concat("/" + applicationId);
            return Files.walk(Paths.get(applicationPath), 1).filter(
                    path -> !path.equals(Paths.get(applicationPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    /**
     * 대출 신청 서류 전체 삭제
     */
    @Override
    public void deleteAll(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.NOT_FOUND_APPLICATION);
        }
        String applicationPath = uploadPath.concat("/" + applicationId);
        // 해당 대출 신청 건에 해당하는 디렉토리 삭제
        FileSystemUtils.deleteRecursively(Paths.get(applicationPath).toFile());
    }

    /**
     * 대출 신청건이 유효한 지 검증하는 메서드
     */
    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
