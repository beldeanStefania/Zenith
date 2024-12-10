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
                        (4, 5, 8, 8), #thehills
                        (7, 8, 9, 2), #adoreu
                        (6, 10, 9, 1), #alliwantforchristmasisu
                        (5, 6, 7, 4), #apt
                        (3, 5, 8, 9), #changes
                        (9, 9, 6, 2), #cheapthrills
                        (8, 7, 6, 4), #countingstars
                        (7, 6, 7, 3), #darkhorse
                        (5, 4, 6, 9), #faded
                        (2, 3, 9, 10), #hello
                        (9, 9, 5, 1), #hipsdontlie
                        (4, 3, 9, 10), #lethergo
                        (3, 2, 8, 10), #sad
                        (5, 4, 9, 10), #seeyouagain
                        (9, 8, 7, 2), #shapeofyou
                        (4, 7, 8, 4), #snowman
                        (6, 5, 8, 8), #storyofmylife
                        (8, 9, 9, 2), #sugar
                        (10, 10, 6, 1), #uptownfunk
                        (10, 10, 5, 1), #wakawaka
                        (6, 5, 8, 9), #wedonttalkanymore
                        (6, 4, 8, 9), #acele
                        (8, 9, 7, 3), #byebyebye
                        (7, 8, 9, 3), #caiverzipepereti
                        (9, 9, 8, 2), #cumnenoi
                        (7, 6, 8, 5), #diewithasmile
                        (5, 7, 9, 8), #foreveryoung
                        (6, 6, 8, 7), #ipotecat
                        (9, 9, 6, 2), #mammamia
                        (6, 7, 9, 5), #niciodatasanuspuiniciodata
                        (8, 7, 6, 3), #notmyproblem
                        (6, 6, 8, 7), #oarecare
                        (5, 5, 8, 7), #oneofthegirls
                        (8, 8, 7, 4), #sheknows
                        (7, 7, 9, 5), #tu
                        (5, 6, 9, 9); #ultimul dans


UPDATE song SET mood_id = id where id between 1 and 57;
UPDATE song set genre = 'Pop' where id in ( 3, 11, 12, 16, 23, 24, 27, 28, 29, 31, 32, 33, 36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 47, 49, 51, 53, 54, 55, 56, 57);
UPDATE  song set genre = 'R&B' where id in ( 1, 8, 13, 22, 52, 54);
UPDATE song set genre = 'Hip-Hop' where id in ( 2, 6, 9, 10, 14, 20, 25, 26, 34, 35, 55);
UPDATE song set genre = 'Dance' where  id in (7, 15, 17, 18, 19, 50);
UPDATE song set genre = 'Rock' where  id in (4, 48 );
UPDATE song set genre = 'Electronic' where id in (5, 30);
UPDATE song set genre = 'Reggae' where id = 21;
UPDATE song set genre = 'Funk' where id = 40;




