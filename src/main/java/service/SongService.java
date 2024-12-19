package service;

import model.Song;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SongService implements ISongService {
	private final Map<Integer, Song> songs;

	public SongService() {
		songs = new LinkedHashMap<>();
	}

	@Override
	public List<Song> findAll() {
		return new ArrayList<>(songs.values());
	}

	@Override
	public void add(Song song) {
		songs.put(song.getId(), song);
	}

	@Override
	public Song findById(int id) {
		return songs.get(id);
	}
}
