package booking.files.service;

import booking.files.document.Receipt;
import booking.files.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    @SneakyThrows
    public void uploadReceipt(MultipartFile file) {
        Receipt receipt = new Receipt(file.getOriginalFilename(), new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        receiptRepository.insert(receipt);
    }

    public Receipt downloadReceipt(String id) {
        return receiptRepository.findById(id).orElseThrow();
    }
}
