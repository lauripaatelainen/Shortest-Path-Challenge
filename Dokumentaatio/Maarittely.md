# Määrittelydokumentti
### Aihe
Kehitetään peli, jossa pelaajan pitää annettuna aikana etsiä lyhin reitti lähtöpisteestä maalipisteeseen. Pelikenttä koostuu neliön muotoisesta ruudukosta, jossa jokaista vierekkäistä ruutua yhdistää polku, jolla on painoarvo. Painot ovat positiivisia kokonaislukuja ja pelikentällä voi liikkua ylös, alas, oikealle ja vaemmalle, ei viistottain. 

### Pelin eteneminen
Pelaajalle annetaan aikaraja, jonka aikana reitti tulee löytää. Jos pelaaja ei löydä reittiä, pelaaja häviää kyseisen tason. Jos pelaaja löytää reitin, joka ei kuitenkaan ole lyhin reitti, pelaaja menettää pisteitä ja jää nykyiselle tasolle. Oikein löydetystä reitistä pelaaja saa pisteitä ja siirtyy seuraavalle tasolle, jossa pelikentän sivu on yhtä pidempi kuin edellisessä tasossa. Pelaajalla on alussa nolla pistettä, jonka jälkeen pisteitä kertyy tasoja voittamalla, ja peli päättyy kun pisteet vähenevät takaisin nollaan. 
Pelin päättyessä oikea vastaus (tai vastaukset) lasketaan käyttäen lyhimmän reitin algoritmia ja lyhin reitti näytetään pelaajalle. 
Optioina peliä voi laajentaa lisäämällä oikean vastauksen algoritmin etenemisestä visualisaatio ja laajentamalla peliä niin, että pelaajan tulee saavuttaa maali käymällä välietappien kautta. 
Pelikenttä generoidaan jokaisen tason alussa luomalla ruudukko, jonka ruutujen väliset painoarvot arvotaan satunnaisesti tietyltä lukuväliltä. 

### Algoritmit ja tietorakenteet
Oikea vastaus lasketaan useilla eri lyhimmän reitin algoritmeilla ja käyttäjä voi vertailla, miten eri algoritmeilla löydetyt vastaukset eroaa toisistaan. Koska oikeita ratkaisuja voi olla useita, on mielenkiintoista nähdä, löytävätkö algoritmit yleensä saman polun vai eri polut. 

Toteuttavat algoritmit ovat ainakin Dijkstra, Bellman-Ford ja A*.

Toteutettavia tietorakenteita ovat ainakin minimikeko ja ArrayList-tyyppinen dynaaminen lista. Tarkemman suunnittelun myötä mu
itakin tarpeita todennäköisesti ilmenee, esimerkiksi pisteiden laskennassa ja tallentamisessa hajatustaulu osoittautua hyödylliseksi.

### O-analyysi
Alla olevissa analyyseissä käytetään merkintöjä:
-	V on verkon solmujen määrä
-	E on verkon kaarien määrä
Koska tämän pelin tapauksessa E kasvaa samassa suhteessa V:n kanssa, on O(E) = O(V). 
Dijkstran algoritmissa aikavaativuuteen vaikuttaa, mihin tietorakenteeseen tutkimattomat solmut sijoitetaan. Yksinkertaisessa toteutuksessa solmut ovat tavallisessa taulukossa tai linkitetyssä listassa, jolloin seuraavan solmun hakemiseksi koko tietorakenne täytyy käydä läpi ja aikavaativuus on O(V), jolloin koko algoritmin aikavaativuus on O(V2). Algoritmin optimoinmiseksi solmut voidaan sijoittaa minimikekoon, jolloin solmun valinta on O(log n) aikavaativuuden operaatio. Minimikeon avulla koko Dijkstran algoritmin aikavaativuus pienenee vaativuudesta O(V2) vaativuuteen O(V log V). 
Bellman-Ford -algoritmin aikavaativuus on O(V*E), mikä on Dijkstran algoritmia enemmän. Algoritmin etuna Dijkstraan nähden olisi mahdollisuus negatiivisille reunojen painoille suunatuissa verkoissa, mutta siitä ei ole tämän pelin tapauksessa hyötyä. 
A* algoritmi on toiminnaltaan kuten Dijkstra, mutta algoritmin etenemistä ohjataan kustannusarviofunktiolla. Kustannusarviofunktion valinta vaikuittaa huomattavasti A* algoritmin aikavaativuuteen. Jos arvio on huono, voi algoritmi joutua pahimassa tapauksessa tutkimaan kaikki solmut, jolloin aikavaativuus on huomattavasti suurempi kuin paremmalla arviolla, jolla käytyjen solmujen lukumäärä saadaan paljon pienemmäksi. Optimitilanne olisi, että algoritmi kävisi vain solmuissa jotka ovat lyhimmällä polulla, mutta niin hyvän arviointifunktion teko on käytännössä mahdotonta. A* algoritmin aikavaativuutta kuvataan yleensä luvuilla b ja d, jossa b on branching factor, ja kuvaa kuinka moneen solmuun ratkaisupuu laajentuu jokaisesta solmusta, ja d on ratkaisupolun pituus. Hyvällä arviointifunktiolla pyritään minimoimaan b. A* algoritmin aikavaativuus näillä termeillä on O(bd). 

### Lähteet
1.	https://brilliant.org/wiki/a-star-search/
