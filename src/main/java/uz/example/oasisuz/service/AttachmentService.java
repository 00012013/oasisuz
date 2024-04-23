package uz.example.oasisuz.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.example.oasisuz.entity.Attachment;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.exception.CustomException;
import uz.example.oasisuz.repository.AttachmentRepository;


import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final CottageService cottageService;
    private final UsersService usersService;

    public void createAttachment(MultipartHttpServletRequest httpServletRequest, Integer cottageId, boolean mainAttachment) throws IOException {
        Cottage cottage = cottageService.getCottage(cottageId);
        if (cottage == null) {
            throw new CustomException(String.format("Cottage not found, id: %s", cottageId), HttpStatus.BAD_REQUEST);
        }

        Iterator<String> fileNames = httpServletRequest.getFileNames();
        MultipartFile picture = httpServletRequest.getFile(fileNames.next());
        if (picture != null) {
            String originalFilename = picture.getOriginalFilename();
            Attachment attachment = Attachment.builder()
                    .contentType(picture.getContentType())
                    .mainContent(picture.getBytes())
                    .cottage(cottage)
                    .fileOriginalName(originalFilename)
                    .build();
            String[] split = originalFilename.split("\\.");

            String name = UUID.randomUUID() + split[split.length - 1];

            attachment.setName(name);

            Attachment savedAttachment = attachmentRepository.save(attachment);
            if (mainAttachment) {
                cottage.setMainAttachmentId(savedAttachment.getId());
                cottageService.saveMainAttachmentId(cottage);
            }
        }
    }

    public void getFile(Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);

        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();

            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileOriginalName() + "\"");

            response.setContentType(attachment.getContentType());

            FileCopyUtils.copy(attachment.getMainContent(), response.getOutputStream());

        }
    }

    public void createMainAttachment(MultipartHttpServletRequest httpServletRequest, Integer cottageId) throws IOException {
        createAttachment(httpServletRequest, cottageId, true);
    }

    @SneakyThrows
    public void uploadFiles(List<MultipartFile> files, Integer id) {
        Cottage cottage = cottageService.getCottage(id);
        if (cottage == null) {
            throw new CustomException(String.format("Cottage not found, id: %s", id), HttpStatus.BAD_REQUEST);
        }
        List<Attachment> attachmentList = new ArrayList<>();
        for (MultipartFile file : files) {
            Attachment attachment = Attachment.builder()
                    .contentType(file.getContentType())
                    .mainContent(file.getBytes())
                    .cottage(cottage)
                    .fileOriginalName(file.getOriginalFilename())
                    .build();
            String[] split = attachment.getFileOriginalName().split("\\.");

            String name = UUID.randomUUID() + split[split.length - 1];

            attachment.setName(name);

            attachmentList.add(attachment);
        }
        attachmentRepository.saveAll(attachmentList);
    }

    public void deleteAttachments(List<Integer> attachmentsIds) {
        attachmentRepository.deleteAllById(attachmentsIds);
    }
}
