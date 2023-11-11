package de.htwb.ai.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.htwb.ai.model.Song;
import de.htwb.ai.repo.SongRepository;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;
    JsonMapper mapper;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getAllSongs() {
        mapper = new JsonMapper();
        List<Song> songs = songRepository.findAll();
        StringBuilder sb = new StringBuilder();
        try {
            for (Song s : songs) {
                sb.append(mapper.writeValueAsString(s));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(sb.toString());
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getSongById(@PathVariable("id") int id) {
        mapper = new JsonMapper();
        Optional<Song> song = songRepository.findById(id);
        if (song.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            String response = "";
            try {
                response = mapper.writeValueAsString(song.get());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> createSong(@RequestBody String song) {
        mapper = new JsonMapper();
        Song songToSave;
        try {
            songToSave = mapper.readValue(song, Song.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
        if(StringUtils.isBlank(songToSave.getTitle())){
            return ResponseEntity.badRequest().build();
        }
        songRepository.save(songToSave);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(songToSave.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateSong(@PathVariable("id") int id, @RequestBody Song song) {
        if (!(id == song.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (songRepository.existsById(id)) {
            songRepository.save(song);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteSong(@PathVariable("id") int id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
