package booking.files.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "receipts")
public class Receipt {
    @Id
    private String id;
    private Binary file;
}
