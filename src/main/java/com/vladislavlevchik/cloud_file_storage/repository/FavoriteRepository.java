package com.vladislavlevchik.cloud_file_storage.repository;

import com.vladislavlevchik.cloud_file_storage.entity.Favorite;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite f WHERE f.filename = :filename AND f.filepath = :filepath AND f.user.username = :username")
    void deleteByFilenameAndFilepathAndUsername(
            @Param("filename") String filename,
            @Param("filepath") String filepath,
            @Param("username") String username
    );

}
