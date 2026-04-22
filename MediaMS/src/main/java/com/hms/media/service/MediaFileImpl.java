package com.hms.media.service;

import com.hms.media.dto.MediaDTO;
import com.hms.media.entity.MediaFile;
import com.hms.media.entity.Storage;
import com.hms.media.repository.MediaFileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaFileImpl implements MediaService{
    private final MediaFileRepo mediaFileRepo;

    @Override
    public MediaDTO storeFile(MultipartFile file) throws IOException {
        MediaFile mediaFile = MediaFile.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .size(file.getSize())
                .data(file.getBytes())
                .storage(Storage.DB)
                .build();
        MediaFile savedFile = mediaFileRepo.save(mediaFile);
        return MediaDTO.builder()
                .id(savedFile.getId())
                .name(savedFile.getName())
                .type(savedFile.getType())
                .size(savedFile.getSize())
                .build();
    }

    @Override
    public Optional<MediaFile> getFile(Long id) {
        return mediaFileRepo.findById(id);
    }
}
