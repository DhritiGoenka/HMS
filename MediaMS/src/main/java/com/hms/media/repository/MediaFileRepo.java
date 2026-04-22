package com.hms.media.repository;

import com.hms.media.entity.MediaFile;
import org.springframework.data.repository.CrudRepository;

public interface MediaFileRepo extends CrudRepository<MediaFile, Long> {
}
