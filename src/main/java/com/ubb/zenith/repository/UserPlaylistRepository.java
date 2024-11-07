package com.ubb.zenith.repository;

import com.ubb.zenith.model.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Integer> {
}
