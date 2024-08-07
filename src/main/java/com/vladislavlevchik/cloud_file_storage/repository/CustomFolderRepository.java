package com.vladislavlevchik.cloud_file_storage.repository;

import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomFolderRepository extends JpaRepository<CustomFolder, Long> {

    @Query("SELECT cf.color FROM CustomFolder cf WHERE cf.name = :name AND cf.user.username = :username")
    String findColorByNameAndUsername(@Param("name") String name, @Param("username") String username);

}
