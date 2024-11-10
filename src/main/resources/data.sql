INSERT into mood(energy_score, happiness_score, love_score, sadness_score) values (4, 6, 7, 8), #crymeariver
                        (7, 9, 3, 1), #cyclone
                        (3, 8, 8, 7), #escapism
                        (3, 9, 4, 8), #faint
                        (4, 6, 5, 8), #fallback
                        (7, 10, 4, 1), #fein
                        (9, 10, 7, 1), #ferrari
                        (5, 6, 9, 7), #friends
                        (6, 8, 3, 4), #goosebumps
                        (7, 8, 6, 5), #highestintheroom
                        (3, 5, 9, 9), #loveisblind
                        (8, 7, 9, 3), #loveistheway
                        (5, 6, 9, 6), #loveonthebrain
                        (3, 5, 6, 9), #mockingbird
                        (9, 8, 6, 1), #mrsaxobeat
                        (9, 8, 1, 1), #numaduclaclub
                        (9, 10, 4, 1), #onthefloor
                        (7, 6, 8, 2), #stereolove
                        (9, 10, 8, 1), #suavemente
                        (4, 6, 5, 8), #superman
                        (4, 4, 9, 7), #teamo
                        (4, 5, 8, 8); # thehills

UPDATE song SET mood_id = id where id between 1 and 22;
UPDATE song set genre = 'Pop' where id in ( 3, 11, 12, 16);
UPDATE  song set genre = 'R&B' where id in ( 1, 8, 13, 22);
UPDATE song set genre = 'Hip-Hop' where id in ( 2, 6, 9, 10, 14, 20);
UPDATE song set genre = 'Dance' where  id in (7, 15, 17, 18, 19);
UPDATE song set genre = 'Rock' where  id = 4;
UPDATE song set genre = 'Electronic' where id = 5;
UPDATE song set genre = 'Reggae' where id = 21;

INSERT INTO user(username,email,password,age) Values ('Alex','alexandru.com','$2a$12$ChDeMO7MgsJwzDPVKTKhMeVaV35y3qrVZQhns2VgIzRaisDInizlq',17)


