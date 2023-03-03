-- H2 2.1.214; 
;              
CREATE USER IF NOT EXISTS "USERNAME" SALT 'e3acce68c57c43be' HASH 'efc1c6daa7205cfabe19d33307ba2b623073b74aacc3118c9ee76fb1ffeb0c41' ADMIN;    
CREATE CACHED TABLE "PUBLIC"."KORISNICI"(
    "ID" BIGINT GENERATED ALWAYS AS IDENTITY(START WITH 1 RESTART WITH 70) NOT NULL,
    "USERNAME" CHARACTER VARYING(255),
    "EMAIL" CHARACTER VARYING(255),
    "IME" CHARACTER VARYING(255),
    "PREZIME" CHARACTER VARYING(255),
    "PASSWORD_HASH" INTEGER,
    "RAZINA_OVLASTI" INTEGER
); 
ALTER TABLE "PUBLIC"."KORISNICI" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_F" PRIMARY KEY("ID");     
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.KORISNICI;
INSERT INTO "PUBLIC"."KORISNICI"("ID", "USERNAME", "EMAIL", "IME", "PREZIME", "PASSWORD_HASH", "RAZINA_OVLASTI") OVERRIDING SYSTEM VALUE VALUES
(2, 'admin', 'email@gmail.com', 'Pepe', 'Pepic', 1216985755, 1),
(5, 'user', 'email@gmail.com', 'Ivan', 'okej', 1216985755, 0),
(7, 'test', 'email@gmail.com', 'Ime', 'Prezime', 1216985755, 0);               
CREATE CACHED TABLE "PUBLIC"."KORISNIK_STATION"(
    "IDKORISNIK" BIGINT NOT NULL,
    "IDSTATION" BIGINT NOT NULL
);          
-- 55 +/- SELECT COUNT(*) FROM PUBLIC.KORISNIK_STATION;        
INSERT INTO "PUBLIC"."KORISNIK_STATION" VALUES
(5, 22),
(5, 23),
(5, 24),
(2, 74),
(5, 74),
(2, 76),
(2, 77),
(2, 78),
(2, 79),
(2, 80),
(2, 81),
(2, 82),
(2, 83),
(2, 84),
(2, 85),
(5, 86),
(5, 87),
(5, 88),
(5, 89),
(5, 90),
(5, 91),
(5, 92),
(5, 93),
(5, 94),
(5, 95),
(5, 96),
(5, 97),
(5, 98),
(5, 99),
(5, 100),
(5, 101),
(5, 102),
(5, 103),
(5, 106),
(5, 107),
(5, 108),
(5, 109),
(5, 110),
(5, 111),
(5, 112),
(5, 113),
(5, 114),
(5, 115),
(5, 116),
(5, 117),
(5, 123),
(5, 76),
(5, 77),
(5, 124),
(5, 78),
(5, 79),
(5, 80),
(5, 84),
(7, 86),
(5, 81);
CREATE CACHED TABLE "PUBLIC"."STATIONS"(
    "ID" BIGINT GENERATED ALWAYS AS IDENTITY(START WITH 1 RESTART WITH 127) NOT NULL,
    "NAZIV" CHARACTER VARYING(255),
    "ZEMLJA" CHARACTER VARYING(255),
    "CODEC" CHARACTER VARYING(255),
    "BITRATE" INTEGER,
    "ZANROVI" CHARACTER VARYING(255),
    "URL" CHARACTER VARYING(255)
);   
ALTER TABLE "PUBLIC"."STATIONS" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_7" PRIMARY KEY("ID");      
-- 50 +/- SELECT COUNT(*) FROM PUBLIC.STATIONS;
INSERT INTO "PUBLIC"."STATIONS"("ID", "NAZIV", "ZEMLJA", "CODEC", "BITRATE", "ZANROVI", "URL") OVERRIDING SYSTEM VALUE VALUES
(21, U&'\0009Tick Tock Radio - 1950', 'Finland', 'MP3', 0, '1950', 'https://streaming.ticktock.radio/tt/1950/icecast.audio'),
(22, U&'\0009\0415\0432\0440\043e\043f\0430 \041f\043b\044e\0441', 'The Russian Federation', 'MP3', 128, '', 'http://ep256.hostingradio.ru:8052/europaplus256.mp3'),
(23, U&' \0009Radio Cuore | Le Grandi Emozione Italiane', 'Italy', 'MP3', 128, '', 'http://46.105.114.57:8001/stream2'),
(24, ' Radio ZET Chilli ', 'Poland', 'MP3', 128, '', 'https://ch.cdn.eurozet.pl/chi-net.mp3'),
(74, 'Braniteljski radio "Nepokoreni grad"', 'Croatia', 'MP3', 128, '', 'http://s3.radio.co/se83499ee0/listen'),
(75, 'City-radio 88.6', 'Croatia', 'MP3', 96, 'local music', 'http://s8.iqstreaming.com:8058/stream;'),
(76, 'DRUGI PROGRAM - HRVATSKI RADIO', 'Croatia', 'MP3', 64, '', 'http://23543.live.streamtheworld.com/PROGRAM2.mp3'),
(77, 'GLAS HRVATSKE - HRVATSKI RADIO', 'Croatia', 'MP3', 64, '', 'http://20723.live.streamtheworld.com/VOICEOFCROATIA.mp3'),
(78, 'Hrvatski Radio - Knin', 'Croatia', 'MP3', 64, '', 'http://20723.live.streamtheworld.com/KNIN.mp3'),
(79, 'Hrvatski Radio - Split', 'Croatia', 'MP3', 64, '', 'http://21223.live.streamtheworld.com/SPLIT.mp3'),
(80, 'Hrvatski Radio - Treci Program', 'Croatia', 'MP3', 128, '', 'http://21223.live.streamtheworld.com/PROGRAM3.mp3'),
(81, 'Narodni Radio Goga', 'Croatia', 'MP3', 320, 'folk,local music,rock', 'https://cmr-hosting.com:7013/;'),
(82, 'Online Radio Extra Kneginec', 'Croatia', 'MP3', 0, 'misc', 'http://stream.zeno.fm/c4kvver9bm8uv'),
(83, 'Online Radio Kneginec', 'Croatia', 'MP3', 0, 'folk', 'http://stream.zeno.fm/yetzdv442k8uv'),
(84, 'PRVI PROGRAM - HRVATSKI RADIO', 'Croatia', 'MP3', 64, '', 'http://21223.live.streamtheworld.com/PROGRAM1.mp3'),
(85, 'Radio 057', 'Croatia', 'MP3', 192, '', 'http://streaming.zadar.net:8000/radio057'),
(86, U&'\0009Fun Radio', 'Slovakia', 'MP3', 128, '', 'http://stream.funradio.sk:8000/fun128.mp3'),
(87, U&'\0009Polskie Radio Bialystok', 'Poland', 'MP3', 128, '', 'http://stream4.nadaje.com:15476/radiobialystok'),
(88, '  Radio Universidad - AM 1240 Universidad Nacional del Sur', 'Argentina', 'MP3', 64, 'radio universitaria,universidad nacional del sur,uns', 'https://sonic.cloudstreaming.eu:10983/;'),
(89, ' Bremen Zwei NEU', 'Germany', 'MP3', 128, 'ard,culture,information,public radio,radio bremen', 'https://icecast.radiobremen.de/rb/bremenzwei/live/mp3/128/stream.mp3'),
(90, U&'\00a0KLUX 89.5HD - Good Company', 'The United States Of America', 'MP3', 64, 'classical', 'http://204.141.167.20:12340/stream'),
(91, ' MC2 Buddha Bar Collection', 'Italy', 'MP3', 128, 'chillout,lounge', 'https://edge.singsingmusic.net/singsingweb024'),
(92, ' TNT Radio live ', 'The United States Of America', 'MP3', 128, 'climate sceptics,covid19,politics,talk', 'https://streaming.tntradio.live/'),
(93, U&' \+01f3a5 ues \+01f3ac ', 'Norway', 'MP3', 128, 'epic,movies,ost,soundtracks', 'https://listen.radionomy.com/unleashingepicsoundtracks'),
(94, '__1 oldies', 'The United States Of America', 'MP3', 128, '70s,80s,90s,disco,musica romantica,oldies,pop,rock', 'https://80sexitos.stream.laut.fm/80sexitos'),
(95, '__12PUNKS.FM__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'alternativ rock,classic rock,heavy metal,metal,new rock,punk,rock,rock hits,soft rock', 'https://12punks-high.rautemusik.fm/?ref=radiobrowser'),
(96, '__1oldies', 'The United States Of America', 'MP3', 128, '', 'https://stream.laut.fm/80sexitos'),
(97, '__80S__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, '1980s,80,80''s,80er,80s,golden oldies,goldies,oldies', 'https://80s-high.rautemusik.fm/?ref=radiobrowser'),
(98, '__90S__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, '1990s,90er,90s,dance,eurodance,hiphop,hits,oldies,pop,rap,rm.fm', 'https://90s-high.rautemusik.fm/?ref=radiobrowser'),
(99, '__BASS__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'chilled trap,drum & bass,drum ''n'' bass,drum and bass,hip-hop,hiphop,liquid trap,rap hiphop rnb,trap', 'https://bass-high.rautemusik.fm/?ref=radiobrowser');     
INSERT INTO "PUBLIC"."STATIONS"("ID", "NAZIV", "ZEMLJA", "CODEC", "BITRATE", "ZANROVI", "URL") OVERRIDING SYSTEM VALUE VALUES
(100, '__BEST OF SCHLAGER__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, '70s disco,best,best music,charts,deutschland,disco,discofox,hits,schlager,schlagerhits', 'https://best-of-schlager-high.rautemusik.fm/?ref=radiobrowser'),
(101, '__BIGCITYBEATS.FM__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'charts,dance,deejay,dj,dj sets,edm,electronic,elektronik,festival,hits,house,minimal', 'https://stream.bigcitybeats.de/bcb-radio/mp3-192/radiobrowser'),
(102, '__BREAKZ.FM__ by rm.fm (rautemusik)', 'Germany', 'MP3', 192, 'dance,deejay,dj,dj mixes,dj sets,edm,electro,electro house,electronic,hardstyle,hiphop,house,mixtapes,moombahton,reggaeton,rnb,urban', 'https://breakz-high.rautemusik.fm/?ref=radiobrowserinfo'),
(103, '__CHARTHITS.FM__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'charts,dance,edm,electro house,electronic,electronica,electropop,hits,house,top40', 'https://charthits-high.rautemusik.fm/?ref=radiobrowser'),
(104, '__CHRISTMAS CHOR__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'choral,christmas,christmas music,church,church of christ,klassik weihnachten christmas,weihnachten', 'https://christmas-chor-high.rautemusik.fm/?ref=radiobrowser'),
(105, '__CHRISTMAS SCHLAGER__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'christmas,christmas music,discofox,klassik weihnachten christmas,schlager,schlagerhits,weihnachten,weihnachtsrock', 'https://christmas-schlager-high.rautemusik.fm/?ref=radiobrowser'),
(106, '__CLUB__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'dance,deejay,dj,edm,electronic,elektro,handsup,hardstyle,hits,techno,trance', 'https://club-high.rautemusik.fm/?ref=radiobrowser'),
(107, '__COUNTRYHITS.FM__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'alternative country,alternative rock,americana,classic country,classic rock,country,hits,nashville,new country,rock,texas country', 'https://country-high.rautemusik.fm/?ref=radiobrowser'),
(108, '__DEUTSCHE HITS__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'charts,dance,german,hits,pop,pop music,pop rock', 'https://deutsche-hits-high.rautemusik.fm/?ref=radiobrowser'),
(109, '__DEUTSCHRAP CHARTS__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, U&'charts,deutsch,deutschrap,german,hip-hop,hiphop,hits,rap,rap hiphop rnb,rnb,top charts,\00e4', 'https://deutschrap-charts-high.rautemusik.fm/?ref=radiobrowser'),
(110, '__GOLDIES__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, '1960s,1970s,1980s,1990s,60er,60s,70er,70s,70s disco,80''s,80er,80s,90''s,90er,90s,classic hits,classic rock,dance,disco,eurodance,golden music,golden oldies,goldies,hits,music,oldies,oldschool,retro', 'https://goldies-high.rautemusik.fm/?ref=radiobrowser'),
(111, '__HAPPY HARDCORE__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'hardcore,hardstyle,hits,hot,hot hits,speedcore', 'https://happyhardcore-high.rautemusik.fm/?ref=radiobrowser'),
(112, '__HAPPY__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'charts,happy,happy hits,happyhits,hits,hot hits,pop,pop music,top charts', 'https://happy-high.rautemusik.fm/?ref=radiobrowser'),
(113, '__HARDER__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'dance,edm,electro,electronic,hardcore,hardstyle,hits,hot hits', 'https://harder-high.rautemusik.fm/?ref=radiobrowser'),
(114, '__JAM__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'hip-hop,hiphop,latin,moombahton,rap,reggaeton,rnb,urban', 'https://jam-high.rautemusik.fm/?ref=radiobrowser'),
(115, '__KARNEVAL__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'carnaval,carnival,cologne,disco,discofox,german,hits,hot hits,karneval,party,party hits,pop,rock,schlager', 'https://karneval-high.rautemusik.fm/?ref=radiobrowser'),
(116, '__KLASSIK__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'classic,classic hits,classical,klassik', 'https://klassik-high.rautemusik.fm/?ref=radiobrowser'),
(117, '__ROCK__ by rautemusik (rm.fm)', 'Germany', 'MP3', 192, 'alternative rock,classic rock,hard rock,heavy metal,metal,pop rock,punk,punk rock,rock,soft rock', 'https://rock-high.rautemusik.fm/?ref=radiobrowser');           
INSERT INTO "PUBLIC"."STATIONS"("ID", "NAZIV", "ZEMLJA", "CODEC", "BITRATE", "ZANROVI", "URL") OVERRIDING SYSTEM VALUE VALUES
(123, '106,7 Rockklassiker', 'Sweden', 'MP3', 128, 'classic rock', 'http://tx-bauerse.sharp-stream.com/http_live.php?ua=WEB&i=rockklassiker_instream_se_mp3'),
(124, U&'Hrvatski Katoli\010dki Radio', 'Croatia', 'MP3', 128, U&'chatolic,christian,croatia,hkr,katoli\010dki,kr\0161\0107anski', 'https://stream.hkr.hr/hkr.mp3');              
ALTER TABLE "PUBLIC"."KORISNIK_STATION" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_6" FOREIGN KEY("IDKORISNIK") REFERENCES "PUBLIC"."KORISNICI"("ID") NOCHECK;        
ALTER TABLE "PUBLIC"."KORISNIK_STATION" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_68" FOREIGN KEY("IDSTATION") REFERENCES "PUBLIC"."STATIONS"("ID") NOCHECK;         
