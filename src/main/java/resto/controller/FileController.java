package resto.controller;

import io.swagger.annotations.Api;

import static resto.ApiPath.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import resto.entity.FileEntity;
//import logistics.service.FileService;
import resto.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping(ROOT + FILES)
@Api(tags = "Unload/download files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ApiOperation(value = "Upload file to server", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "all good"),
            @ApiResponse(code = 403, message = "no permission"),
            @ApiResponse(code = 401, message = "no connection")
    })
    @PostMapping
    public void saveFilesInFileSystem(@RequestParam MultipartFile multipartFile) {
        fileService.saveFileInFileSystem(multipartFile);

    }

    @PostMapping(BLOBS)
    public void saveFileAsBlog(@RequestParam MultipartFile multipartFile){
        fileService.saveFileInDbAsBlob(multipartFile);
    }

    @ApiOperation(value = "Download file by name from server by name", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "all good"),
            @ApiResponse(code = 403, message = "no permission"),
            @ApiResponse(code = 401, message = "no connection")
    })
    @GetMapping(FILE_BY_NAME)
    public ResponseEntity<Resource> getFileByNameFromFileSystem(@PathVariable(NAME_VARIABLE) String fileName) {
        Resource resource = new InputStreamResource((fileService.getFileFromSystemByName(fileName)));

        MediaType mediaType = fileService.getMediaTypeFromName(fileName);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .headers(getHeaders(fileName))
                .body(resource);
    }

    @GetMapping(GET_BLOB)
    public ResponseEntity<Resource> getFileById(@PathVariable(ID_VARIABLE) UUID id){
        FileEntity fileEntity = fileService.getFileById(id);

        Resource resource = new ByteArrayResource(fileEntity.getBytes());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(fileEntity.getMediaType()))
                .headers(getHeaders(fileEntity.getFileName()))
                .body(resource);
    }

    private HttpHeaders getHeaders(String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return headers;
    }
}
