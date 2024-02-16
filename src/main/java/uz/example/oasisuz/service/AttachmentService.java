package uz.example.oasisuz.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.example.oasisuz.entity.Attachment;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.repository.AttachmentRepository;


import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final CottageService cottageService;


    public void createAttachment(MultipartHttpServletRequest httpServletRequest, Integer cottageId, boolean mainAttachment) throws IOException {
//        Optional<Product> productOptional = productRepository.findById(productId);
//
        Cottage cottage = cottageService.getCottage(cottageId);
        if (cottage == null) {
            // to do
            return;
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

//            Path path = Paths.get(filePath + "/" + name);
//            Files.copy(picture.getInputStream(), path);

            Attachment savedAttachment = attachmentRepository.save(attachment);
            if(mainAttachment){
                cottage.setMainAttachment(savedAttachment);
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

//            FileInputStream fileInputStream = new FileInputStream(filePath + "/" + attachment.getName());
            FileCopyUtils.copy(attachment.getMainContent(), response.getOutputStream());

        }
    }

    public void createMainAttachment(MultipartHttpServletRequest httpServletRequest, Integer cottageId) throws IOException {
        createAttachment(httpServletRequest, cottageId, true);
    }
}
