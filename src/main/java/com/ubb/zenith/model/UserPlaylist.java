package com.ubb.zenith.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlaylist {
    @Embeddable
    public static class UserPlaylistsId implements Serializable {
        private Integer userId;
        private Integer playlistId;

        // Default constructor
        public UserPlaylistsId() {}

        // Parameterized constructor
        public UserPlaylistsId(Integer userId, Integer playlistId) {
            this.userId = userId;
            this.playlistId = playlistId;
        }

        // Override equals and hashCode methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserPlaylistsId that = (UserPlaylistsId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(playlistId, that.playlistId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, playlistId);
        }
    }

    @EmbeddedId
    private UserPlaylistsId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @Temporal(TemporalType.DATE)
    private Date date;
}