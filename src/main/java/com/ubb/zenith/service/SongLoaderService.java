package com.ubb.zenith.service;

import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.SongRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class SongLoaderService implements CommandLineRunner {

    private final SongRepository songRepository;

    @Value("${songs.folder-path}")
    private String folderPath;

    public SongLoaderService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadSongsFromFolder();
    }

    private void loadSongsFromFolder() {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    try {
                        saveSongFromFile(file);
                    } catch (IOException e) {
                        System.err.println("Failed to load song from file: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveSongFromFile(File file) throws IOException {
        String fileName = file.getName();
        String[] nameParts = fileName.split("_");
        if (nameParts.length < 2) {
            System.err.println("Invalid file name format: " + fileName);
            return;
        }

        String title = nameParts[0];
        String artist = nameParts[1].replace(".mp3", "");

        if (songRepository.findByArtistAndTitle(artist, title).isPresent()) {
            System.out.println("Song already exists: " + title + " by " + artist);
            return;
        }

        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setGenre("Unknown");
        song.setAudioData(Files.readAllBytes(file.toPath()));

        songRepository.save(song);
        System.out.println("Saved song: " + title + " by " + artist);
    }

}
