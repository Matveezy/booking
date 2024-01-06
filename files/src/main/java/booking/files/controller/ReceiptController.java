package booking.files.controller;

import booking.files.document.Receipt;
import booking.files.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ByteArrayResource downloadReceipt(@PathVariable String id) {
        Receipt receipt = receiptService.downloadReceipt(id);
        return new ByteArrayResource(receipt.getFile().getData());
    }
}
