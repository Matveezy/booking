package booking.files.controller;

import booking.files.document.Receipt;
import booking.files.service.ReceiptService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/receipt")
@RestController
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/upload")
    public String uploadReceipt(@RequestParam("file") MultipartFile file) {
        receiptService.uploadReceipt(file);
        return "Uploaded %s bytes".formatted(file.getSize());
    }

    @GetMapping("/download/{id}")
    public void downloadReceipt(@PathVariable String id, HttpServletResponse response) {
        Receipt receipt = receiptService.downloadReceipt(id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"receipt.txt\"");

        try {
            response.getOutputStream().write(receipt.getFile().getData());
        } catch (IOException e) {
            log.info("Can't download file");
        }
    }

    @MessageMapping("/ws")
    public void uploadReceiptWs(File file) {
        receiptService.uploadReceipt(file);
    }
}
