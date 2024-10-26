-- Popularea tabelei `mood`
INSERT IGNORE INTO mood (energy_score, happiness_score, love_score, sadness_score) VALUES
                                                                                    (2, 1, 0, 9),  -- mood pentru cântece triste
                                                                                    (8, 9, 5, 1),  -- mood pentru cântece fericite
                                                                                    (7, 5, 6, 2),  -- mood pentru cântece energice
                                                                                     (3, 3, 4, 6); -- mood neutru

-- Popularea tabelei `song`
-- Presupunem că `mood_id` se referă la ID-ul mood-urilor deja introduse
INSERT IGNORE INTO song (artist, genre, title, mood_id, playlist_id) VALUES
                                                                      ('Artist1', 'Pop', 'Sad Song 1', 1, NULL),
                                                                      ('Artist2', 'Pop', 'Happy Song 1', 2, NULL),
                                                                      ('Artist3', 'Rock', 'Energetic Song 1', 3, NULL),
                                                                      ('Artist4', 'Jazz', 'Calm Song 1', 4, NULL),
                                                                      ('Artist5', 'Pop', 'Sad Song 2', 1, NULL),
                                                                      ('Artist6', 'Rock', 'Happy Song 2', 2, NULL),
                                                                      ('Artist7', 'Electronic', 'Energetic Song 2', 3, NULL),
                                                                      ('Artist8', 'Pop', 'Calm Song 2', 4, NULL);

-- (Opțional) Popularea tabelei `playlist` cu un playlist inițial
INSERT IGNORE INTO playlist (name) VALUES
                                    ('Chill Vibes'),
                                    ('Workout Mix');
