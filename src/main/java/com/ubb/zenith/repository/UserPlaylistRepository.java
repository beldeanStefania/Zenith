package com.ubb.zenith.repository;

import com.ubb.zenith.model.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, UserPlaylist.UserPlaylistsId> {


    //Metoda de a gasi un UserPlaylist dupa id user si id playlist
    Optional<UserPlaylist> findByUserIdAndPlaylistId(Integer userId, Integer playlistId);
}
