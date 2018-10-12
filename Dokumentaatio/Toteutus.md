# Toteutusdokumentti

Shortest-Path-Challenge -reitinhakupeli on toteutettu Javalla, jossa pohjalla toimii omat tietorakenteet ja algoritmit, ja jonka käyttöliittymä on tehty Javan Swing-kirjastolla. 
Tässä dokumentissa on kuvattu ohjelman rakenne ja toiminta korkealla tasolla, sekä suorituskykyyn liittyvä analyysi. Tarkempi ohjelman rakenne löytyy [Javadocista](http://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Shortest-Path-Challenge/build/docs/javadoc/index.html).

## Rakenne
Ohjelma on jaettu useaan Java-pakettiin:

### com.edii.spc.datastructures
Sisältää geneerisiksi suunnitellut tietorakenteet, joista ei ole riippuvuuksia mihinkään muihin ohjelman osa-alueisiin, ja voitaisiin periaatteessa käyttää sellaisenaan muissa sovellukseissa. Listoja, joukkoja ja hajautustauluja kuvaavat tietorakenteet toteuttavat Javan vastaavat `java.util`-paketista löytyvät rajapinnat lähes(\*) täydellisesti. Lisäksi paketissa on `MinHeap`- ja `Pair`-tietorakenteet, jotka eivät suoraan sovi mihinkään `java.util` -paketin rajapintoihin, joten `MinHeap` toteuttaa vain Javan hyvin geneerisen `Collection`-rajapinnan ja `Pair` on vain itsenäinen luokka eikä toteuta mitään rajapintoja (* lähes siksi, että listoissa ei ole toteutettu `java.util.subList(int start, int end)`-metodia sen monimutkaisuuden vuoksi. Rajapinnan kuvauksessa subListin palauttaman listan tulisi heijastaa toiminnot alkuperäiseen listaan niin, että palautettuun listaan tehdyt muutokset heijastuvat alkuperäiseen listaan. subList-toimintoa ei käytetä tässä sovelluksessa, ja toteuttaminen olisi hyötyyn nähden tarpeettoman monimutkaista, joten jätin sen toteuttamisen scopen ulkopuolelle). 

### com.edii.spc.game
Sisältää peliin liittyvän logiikan ja pelin sisäisen tietorakenteen, kuten sen miten pelikenttä jakautuu useisiin solmuihin ja painollisiin kaariin. Tämän paketin luokat käyttävät datastructures-paketin tietorakenteita. 

### com.edii.spc.game.solvers
Sisältää pelin ratkaisuun käytetyt algoritmit. Paketissa määritellään yksi rajapinta `Solver`, sekä kolme sen toteuttavaa luokkaa. Pelin ratkaisu aloitetaan rajapinnan `solve(GameField field)`-toiminnolla, joka palauttaa `GameFieldPath`-tyyppisen polun alkusolmusta maalisolmuun.

`Solver`-rajapinta myös tukee ratkaisun keskeytystä `interrupt()`-komennolla jos ratkaisu kestää liian pitkään. Tällöin alkuperäinen `solve()` operaatio keskeytyy ja päätyy `InterruptedException`-virheeseen. 

### com.edii.spc.game.perf
Sisältää reitinhakualgoritmien suorituskyvyn mittaukseen liittyvän toiminnallisuuden. Paketin toimintoja käytetään vain suorituskykytietojen mittaukseen ja dokumentointiin, joten paketin sisältämä koodi on jätetty tarkoituksella koodinlaatutarkistusten ulkopuolelle ja dokumentointi on heikompaa. 

### com.edii.spc.game.ui
Sisältää käyttöliittymään liittyvät komponentit. Käyttöliittymäohjelmointi ei ole tämän kurssin pääaihe, joten koodi ei laatu ja dokumentaatio on ydinaluetta heikompaa. 

## Tietorakenteet
`com.edii.spc.datastructures`-paketissa toteutetut tietorakenteet määräytyivät sen perusteella, mitä varsinaiset reitinhakualgoritmit, sekä pelin logiikka tarvitsevat toimiakseen. Paketissa on kolme abstraktia yläluokkaa, joita tietorakenteiden toteutukset hyödyntävät niiden operaatioiden osalta, joissa suorituskykyyn ei ole vaikutusta. Abstraktit yläluokat ovat `OwnAbstractCollection`, `OwnAbstractSet` ja `OwnAbstractList`.

### Pair
Yksinkertainen tietorakenne, joka sisältää kaksi samantyyppistä oliota määritellyssä järjestyksessä. Esimerkiksi pelikentän kaari hyödyntää sisäisestä `Pair` oliota, joka sisältää järjestyksessä alku- ja loppusolmut. Ainoa erityinen toiminto, jota `Pair` tukee, on `inverse()`, joka luo uuden parin, jossa alkuperäisen parin oliot ovat käänteisessä järjestyksessä. Tämä on luonnollisesti O(1) aika- ja tilavaativudeen operaatio. 

### MinHeap
Minimikeon toteutus binäärikekona. Käyttää sisäisesti Javan natiivia arrayta tietojen säilyttämiseen, mutta pitää ne keon mukaisessa järjestyksessä kekoehdon säilyttäen. 
Tukee `java.util.Collection`-rajapinnan vaatimia operaatioita, ja niiden lisäksi minimikekoon liittyviä operaatioita `extractMin()` pienimmän alkion noutamiseksi ja `decreaseKey()`, jolla voi ilmoittaa jos jonkin alkion avaimen paino on pienentynyt, jolloin alkion uusi paikka on syytä etsiä kekoehdon täyttymiseksi. 

*TODO: O-analyysi tuetuista operaatioista*

### OwnLinkedList
Linkitetyn listan toteutus. Toteuttaa `java.util.List`-rajapinnan kaikki toiminnot `subList()`-toimintoa lukuunottamatta.

*TODO: O-analyysi tuetuista operaatioista*

### OwnList
Dynaaminen lista, joka vastaa esim. Javan `java.util.ArrayList`-toteutusta. Toteuttaa `java.util.List`-rajapinnan kaikki toiminnot `subList()`-toimintoa lukuunottamatta.

*TODO: O-analyysi tuetuista operaatioista*

### OwnMap
Hajautustaulun toteutus, joka vastaa esim. Javan `java.util.HashMap`-toteutusta. Tukee kaikkia `java.util.Map`-rajapinnan toimintoja.

*TODO: O-analyysi tuetuista operaatioista*

### OwnSet
Joukon toteutus, joka vastaa esim. Javan `java.util.HashSet`-toteutusta. Tukee kaikkia `java.util.Set`-rajapinnan toimintoja.
Käyttää sisäisesti `OwnMap`-luokkaa, koska siinä luokassa on jo toteutettu tarvittava tiivisteen perusteella lokerointi. `OwnMap`-luokasta hyödynnetään joukon tapauksessa pelkästään avaimia, kaikki arvot asetetaan `null`-arvoiksi. Aika- ja tilavaativuudet ovat kuten `OwnMap`-luokalla. 

## Algoritmit

### Bellman-Ford
Bellman-Ford algoritmin toteutus. Tämä on toteutetuista algoritmeista heikoin, ja sillä on ongelmia suoriutua suuremmista pelikentistä. 

*TODO: O-analyysi ja mittaustulokset*

### Dijkstra
Dijkstran algoritmin toteutus. 

*TODO: O-analyysi ja mittaustulokset*

### A*
A* algoritmin toteutus.

*TODO: Pohdintaa arviointifunktion merkityksestä.*

*TODO: O-analyysi ja mittaustulokset* 




## Lähteet:

*TODO: Lisätään O-analyysin myötä*
