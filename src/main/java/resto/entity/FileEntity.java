package resto.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Files")
@Getter
@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "VARCHAR(36)", updatable = false)
    @Type(type = "uuid-char")
    private UUID id;

    private String fileName;
    private String mediaType;
    private long size;
    @Lob
    private  byte[] bytes;
    @CreationTimestamp
    private final LocalDateTime timestamp = LocalDateTime.now();

    public FileEntity(String fileName, String mediaType, long size) {
        this(fileName,mediaType,size, null);
    }

    public FileEntity(String fileName, String mediaType, long size, byte[] bytes) {
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.size = size;
        this.bytes = bytes;
    }
}

