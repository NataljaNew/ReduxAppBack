package resto.service;


import resto.entity.FileEntity;
import resto.exception.data.FileException;
import resto.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private static final int MAX_SIZE = 10000000;
    private final Set<String> types;
    private final Path fileLocation;
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        fileLocation = Paths.get("./files").toAbsolutePath().normalize();
        types = Set.of(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE);
    }

    public void saveFileInFileSystem(MultipartFile multipartFile){
        validateFile(multipartFile);
        createDirectory();

        try {
            FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize());
            fileEntity = fileRepository.save(fileEntity);

            Path location = fileLocation.resolve(fileEntity.getId().toString());
            Files.copy(multipartFile.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("cannot copy file" + e);
            throw new FileException("cannot copy file");
        }
    }

    private String getUniqueFileName(MultipartFile multipartFile) {
        return String.format("%s_%s", LocalDateTime.now().getNano(), multipartFile.getOriginalFilename());
    }

    private void createDirectory(){
        try {
            if(!Files.exists(fileLocation)){
                Files.createDirectory(fileLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error" + e);
            throw new FileException("Could not create new directory");
        }
    }

    public InputStream getFileFromSystemByName( String fileName){
        try {
            FileEntity fileEntity = fileRepository.findFirstByFileName(fileName);
            Path location = fileLocation.resolve(fileEntity.getId().toString());
            return Files.newInputStream(location);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(String.format("Could not download %s" , fileName));
        }
    }

    public FileEntity getFileById(UUID id){
        return fileRepository.findById(id).orElseThrow(()-> new FileException((String.format("Cannot find file by %s UUID", id))));
    }
    public MediaType getMediaTypeFromName (String fileName){
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        return MediaType.valueOf(contentType);
    }

    private void validateFile(MultipartFile multipartFile){
        if (multipartFile.getSize() > MAX_SIZE){
            throw new FileException(String.format("File size %s is too big", multipartFile.getSize()));
        }

        if(!types.contains(multipartFile.getContentType())){
            throw new FileException(String.format("Content type %s not allowed", multipartFile.getContentType()));
        }
    }

    public  void saveFileInDbAsBlob(MultipartFile multipartFile){
        validateFile(multipartFile);
        try{
            FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    multipartFile.getSize(),
                    multipartFile.getBytes());
            fileRepository.save(fileEntity);

        } catch (Exception e){
            log.error("Error cannot save file");
            throw new FileException(String.format("Cannot save %s file", multipartFile.getOriginalFilename()));
        }
    }
}
