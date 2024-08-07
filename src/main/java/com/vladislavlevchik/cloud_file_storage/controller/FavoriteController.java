package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.FavoriteDeleteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FavoriteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorite/add")
    public ResponseEntity<?> addToFavorites(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        String username = getUserNameFromPrincipal();

        favoriteService.saveFavorite(username, favoriteRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("File has been successfully added to favourites")
                .build()
        );
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> listFavorites() {
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(favoriteService.listFavorites(username));
    }

    @DeleteMapping("/favorite")
    public ResponseEntity<?> deleteFromFavorites(@RequestBody FavoriteDeleteRequestDto favoriteDeleteRequestDto) {
        String username = getUserNameFromPrincipal();

        favoriteService.deleteFavorite(username, favoriteDeleteRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("File has been successfully deleted")
                .build()
        );
    }


    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
