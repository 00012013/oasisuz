package uz.example.oasisuz.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.example.oasisuz.service.AttachmentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cottage-attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;
    @PostMapping("/create-attachment/{cottageId}")
    public void createAttachment(@PathVariable Integer cottageId, MultipartHttpServletRequest httpServletRequest) throws IOException {

         attachmentService.createAttachment(httpServletRequest, cottageId, false);
    }
    @GetMapping("/get-file/{id}")
    public void getFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        attachmentService.getFile(id, response);
    }

    @PostMapping("/create-main-attachment/{cottageId}")
    public void createMainAttachment(@PathVariable Integer cottageId, MultipartHttpServletRequest httpServletRequest) throws IOException {
        attachmentService.createMainAttachment(httpServletRequest, cottageId);
    }
    @PostMapping("/upload/{id}")
    public void uploadFiles(@RequestParam("files") List<MultipartFile> files, @PathVariable Integer id) {
        attachmentService.uploadFiles(files, id);
    }

    @PostMapping("/delete-attachments")
    public void deleteAttachments(@RequestBody List<Integer> attachmentsIds){
        attachmentService.deleteAttachments(attachmentsIds);
    }
}
