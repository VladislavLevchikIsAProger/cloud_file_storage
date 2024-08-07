package com.vladislavlevchik.cloud_file_storage.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = {"filename", "filepath"}))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filepath;

    @Column(nullable = false)
    private String size;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
