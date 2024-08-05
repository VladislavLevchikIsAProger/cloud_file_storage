package com.vladislavlevchik.cloud_file_storage.repository;

import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomFolderRepository extends JpaRepository<CustomFolder, Long> {

}
