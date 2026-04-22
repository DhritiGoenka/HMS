package com.hms.media.api;

import com.hms.media.dto.MediaDTO;
import com.hms.media.entity.MediaFile;
import com.hms.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaAPI {
    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<MediaDTO> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        try{
            MediaDTO mediaDTO = mediaService.storeFile(file);
            return ResponseEntity.ok(mediaDTO);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id){
        Optional<MediaFile> mediaFile = mediaService.getFile(id);
        if(mediaFile.isPresent()){
            MediaFile mediaFile1 = mediaFile.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + mediaFile1.getName() + "\"")
                    .contentType(MediaType.parseMediaType(mediaFile1.getType()))
                    .body(mediaFile1.getData());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
