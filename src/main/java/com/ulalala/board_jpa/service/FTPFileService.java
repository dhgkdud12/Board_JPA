package com.ulalala.board_jpa.service;

import com.ulalala.board_jpa.common.response.SuccessMessage;
import com.ulalala.board_jpa.common.response.exception.ErrorCode;
import com.ulalala.board_jpa.common.response.exception.TicketingException;
import com.ulalala.board_jpa.dao.repository.FileRepository;
import com.ulalala.board_jpa.domain.FileEntity;
import com.ulalala.board_jpa.dto.board.BoardRequest;
import com.ulalala.board_jpa.dto.file.FileRequest;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class FTPFileService {

    private final FileRepository fileRepository;

    public FTPFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public String uploadFiletoFtp(BoardRequest boardRequest) throws IOException {

        MultipartFile file = boardRequest.getFile();

        // 원본 파일명, 확장자
        String fileName = file.getOriginalFilename();
        int idx = fileName.indexOf(".");
        String onlyFName = fileName.substring(0, idx);
        String fileE = fileName.substring(idx+1);

        // 파일명 변환
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        String convertName = onlyFName+formattedNow;

        // 파일 경로
        String filePath = "/file/";
        String fullPath = filePath+convertName+"."+fileE;

        // 파일 크기
        long fileSize = file.getSize();

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply = 0;
            ftpClient.connect("211.62.140.77",21); // 호스트 연결
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.out.println("연결 비정상");
            } else {
                ftpClient.login("administrator","ulalalab12!@");
                showServerReply(ftpClient);

                ftpClient.enterLocalPassiveMode(); // passive 모드
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                boolean isSuccess = ftpClient.storeFile(fullPath, file.getInputStream()); // 파일 저장
                if (isSuccess) {
                    System.out.println("업로드 성공");
                }
            }

        } catch (FileNotFoundException e) {
            throw new TicketingException(ErrorCode.INVALID_FILE);
        } catch (SocketException e){
            System.out.println("소켓 오류");
        }

        ftpClient.disconnect(); // 연결 해제

        Integer bIdx = boardRequest.getBId();
        System.out.println(onlyFName);
        System.out.println(convertName);
        System.out.println(fileE);
        System.out.println(filePath);
        System.out.println(fileSize);

        FileEntity fileEntity = new FileEntity(null, bIdx, onlyFName, convertName, filePath, fileE, fileSize);

        if (fileRepository.save(fileEntity) != null) return SuccessMessage.SUCCESS_FILE_UP.getMessage();
        else throw new TicketingException(ErrorCode.FAIL_FILE_UP);
    }

    private static void showServerReply(FTPClient ftpClient){
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public ResponseEntity<Object> downloadFilefromFTP(Integer fIdx) {

        // 로그인했을 경우만 다운로드 가능

        HttpHeaders headers = new HttpHeaders();
        Resource resource = null;

        FileEntity g_file = fileRepository.findById(fIdx).get();
        String path = g_file.getPath(); // 파일 경로 얻기

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply = 0;
            ftpClient.connect("211.62.140.77",21); // 호스트 연결
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.out.println("연결 비정상");
            } else {
                ftpClient.login("administrator","ulalalab12!@");
                ftpClient.enterLocalPassiveMode(); // passive 모드
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory(path); // 디렉토리 이동

                String name = "temp"+"."+g_file.getExtension();
                File f = new File(name);
                f.createNewFile();

                OutputStream outputStream = new FileOutputStream(name);

                if (ftpClient.retrieveFile(g_file.getConvertName()+"."+g_file.getExtension(), outputStream)) // 파일 저장
                    System.out.println("success");

                InputStream inputStream = new FileInputStream(name);

                resource = new InputStreamResource(inputStream); // 파일 resource 얻기

                headers.setContentDisposition(ContentDisposition.builder("attachment").filename(g_file.getFileName()+"."+g_file.getExtension()).build());
                ftpClient.disconnect(); // 연결 해제

                f.delete();
            }

        } catch(Exception e) {
            throw new TicketingException(ErrorCode.FAIL_FILE_DOWN);
        }
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
