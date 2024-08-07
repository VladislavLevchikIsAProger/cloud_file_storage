package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.FavoriteDeleteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FavoriteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FavoriteResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.Favorite;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.FavoriteRepository;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public void saveFavorite(String username, FavoriteRequestDto favoriteRequestDto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );

        Favorite favorite = Favorite.builder()
                .filename(favoriteRequestDto.getFilename())
                .filepath(favoriteRequestDto.getFilepath())
                .size(favoriteRequestDto.getSize())
                .build();

        favorite.setUser(user);

        favoriteRepository.save(favorite);
    }

    public List<FavoriteResponseDto> listFavorites(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );

        List<Favorite> favorites = user.getFavorites();

        return favorites.stream()
                .map(favorite -> mapper.map(favorite, FavoriteResponseDto.class))
                .collect(Collectors.toList());

    }

    public void deleteFavorite(String username, FavoriteDeleteRequestDto favoriteDeleteRequestDto) {

        favoriteRepository.deleteByFilenameAndFilepathAndUsername(favoriteDeleteRequestDto.getFilename(),
                favoriteDeleteRequestDto.getFilepath(), username);

    }

}
