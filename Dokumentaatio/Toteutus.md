# Toteutusdokumentti

Shortest-Path-Challenge -reitinhakupeli on toteutettu Javalla, jossa pohjalla toimii omat tietorakenteet ja algoritmit,
ja jonka käyttöliittymä on tehty Javan Swing-kirjastolla. 
Tässä dokumentissa on kuvattu ohjelman rakenne ja toiminta korkealla tasolla, sekä suorituskykyyn liittyvä analyysi.
Tarkempi ohjelman rakenne löytyy [Javadocista](http://htmlpreview.github.io/?https://github.com/lauripaatelainen/Shortest-Path-Challenge/blob/master/Shortest-Path-Challenge/build/docs/javadoc/index.html).

## Rakenne
Ohjelma on jaettu useaan Java-pakettiin:

### com.edii.spc.datastructures
Sisältää geneerisiksi suunnitellut tietorakenteet, joista ei ole riippuvuuksia mihinkään muihin ohjelman osa-alueisiin,
ja voitaisiin periaatteessa käyttää sellaisenaan muissa sovellukseissa.
Listoja, joukkoja ja hajautustauluja kuvaavat tietorakenteet toteuttavat Javan vastaavat `java.util`-paketista löytyvät rajapinnat lähes(\*) täydellisesti.
Lisäksi paketissa on `MinHeap`- ja `Pair`-tietorakenteet, jotka eivät suoraan sovi mihinkään `java.util` -paketin rajapintoihin,
joten `MinHeap` toteuttaa vain Javan hyvin geneerisen `Collection`-rajapinnan ja `Pair` on vain itsenäinen luokka eikä toteuta mitään rajapintoj

(* lähes siksi, että listoissa ei ole toteutettu `java.util.subList(int start, int end)`-metodia sen monimutkaisuuden vuoksi.
Rajapinnan kuvauksessa subListin palauttaman listan tulisi heijastaa toiminnot alkuperäiseen listaan niin,
että palautettuun listaan tehdyt muutokset heijastuvat alkuperäiseen listaan. subList-toimintoa ei käytetä tässä sovelluksessa,
ja toteuttaminen olisi hyötyyn nähden tarpeettoman monimutkaista, joten jätin sen toteuttamisen scopen ulkopuolelle). 

### com.edii.spc.game
Sisältää peliin liittyvän logiikan ja pelin sisäisen tietorakenteen,
kuten sen miten pelikenttä jakautuu useisiin solmuihin ja painollisiin kaariin.
Tämän paketin luokat käyttävät datastructures-paketin tietorakenteita. 

### com.edii.spc.game.solvers
Sisältää pelin ratkaisuun käytetyt algoritmit.
Paketissa määritellään yksi rajapinta `Solver`,
sekä kolme sen toteuttavaa luokkaa.
Pelin ratkaisu aloitetaan rajapinnan `solve(GameField field)`-toiminnolla,
joka palauttaa `GameFieldPath`-tyyppisen polun alkusolmusta maalisolmuun.

`Solver`-rajapinta myös tukee ratkaisun keskeytystä `interrupt()`-komennolla jos ratkaisu kestää liian pitkään.
Tällöin alkuperäinen `solve()` operaatio keskeytyy ja päätyy `InterruptedException`-virheeseen. 

### com.edii.spc.game.perf
Sisältää reitinhakualgoritmien suorituskyvyn mittaukseen liittyvän toiminnallisuuden.
Paketin toimintoja käytetään vain suorituskykytietojen mittaukseen ja dokumentointiin,
joten paketin sisältämä koodi on jätetty tarkoituksella koodinlaatutarkistusten ulkopuolelle ja dokumentointi on heikompaa. 

### com.edii.spc.game.ui
Sisältää käyttöliittymään liittyvät komponentit.
Käyttöliittymäohjelmointi ei ole tämän kurssin pääaihe,
joten koodi ei laatu ja dokumentaatio on ydinaluetta heikompaa. 

## Tietorakenteet
`com.edii.spc.datastructures`-paketissa toteutetut tietorakenteet määräytyivät sen perusteella,
mitä varsinaiset reitinhakualgoritmit, sekä pelin logiikka tarvitsevat toimiakseen.
Paketissa on kolme abstraktia yläluokkaa, joita tietorakenteiden toteutukset hyödyntävät niiden operaatioiden osalta,
joissa suorituskykyyn ei ole vaikutusta. Abstraktit yläluokat ovat `OwnAbstractCollection`, `OwnAbstractSet` ja `OwnAbstractList`.

### Pair
Yksinkertainen tietorakenne, joka sisältää kaksi samantyyppistä oliota määritellyssä järjestyksessä.
Esimerkiksi pelikentän kaari hyödyntää sisäisestä `Pair` oliota, joka sisältää järjestyksessä alku- ja loppusolmut.
Ainoa erityinen toiminto, jota `Pair` tukee, on `inverse()`, joka luo uuden parin,
jossa alkuperäisen parin oliot ovat käänteisessä järjestyksessä.
Tämä on luonnollisesti `O(1)` aika- ja tilavaativudeen operaatio. 

### MinHeap
Minimikeon toteutus binäärikekona. Käyttää sisäisesti Javan natiivia arrayta tietojen säilyttämiseen,
mutta pitää ne keon mukaisessa järjestyksessä kekoehdon säilyttäen. 
Tukee `java.util.Collection`-rajapinnan vaatimia operaatioita,
ja niiden lisäksi minimikekoon liittyviä operaatioita `extractMin()` pienimmän alkion noutamiseksi ja `decreaseKey()`,
jolla voi ilmoittaa jos jonkin alkion avaimen paino on pienentynyt, jolloin alkion uusi paikka on syytä etsiä kekoehdon täyttymiseksi. 

Minikeon oleellisimmat toiminnot ovat `add()`, `decreaseKey()` ja `extractMin()`.
Lisäksi on hyödyllistä voida tarkistaa `contains()`-operaatiolla sisältääkö minikeko jo entuudestaan annetun alkion.
Minikeko käyttää sisäisesti listaa alkioiden säilyttämiseen.
Suoritettaessa operaatioita, lista pidetään sellaisessa järjestyksessä,
että kekoehto täyttyy aina, ja operaatiot saadaan sen myötä toteutettua tehokkaasti. 

Minimeon sisällä ylläpidetään hajautustaulua, jonka avaimena on minimikeon alkio ja arvona listan indeksi minimikoossa.
Koska hajautustaulun `put()`-, `remove()`- ja `get()`-operaatiot ovat aikavaativuuden `O(1)` operaatioita (keskimäärin),
ei hajautustaulun päivittäminen muuta minimikeon operaatioiden (keskimääräistä) aikavaativuusluokitusta. Hajautustaulu tarvitaan,
jotta alkion jäsenyyden tarkistava `contains()` saadaan toteutettua tehokkaasti. 

`add()`-operaatiossa lisättävä alkio lisätään ensin sisäisen listan loppuun,
jonka jälkeen kutsutaan `decreaseKey()`-metodia,
jolla ilmoitetaan minimikeolle, että listan viimeisen alkion arvo on pienentynyt.
`decreaseKey()` käy keon läpi alhaalta ylös, viimeisestä solmusta aina sen yläpuolella olevaan solmuun, kunnes löytyy alkion oikea paikka keossa niin,
että yläpuolella oleva solmu on pienempi ja alapuolella oleva on suurempi (tai yhtäsuuri). Pahimmassa tapauksessa keosta joudutaan käymään läpi
`h` solmua, jossa `h` on keon korkeus. Koska `h` = `O(log n)`, niin `add()`-operaation aikavaativuus on `O(log n)`. Koska lopuksi keon sisäinen hajautustaulu päivitetään,
ja voi olla että hajautustaulua joudutaan kasvattamaan, on pahimman tapauksen aikavaativuus `O(n)`. Hajautustaulun kasvatus on kuitenkin verrattain harvinainen operaatio, 
joten keskimääräinen aikavaativuus on `O(log n)`. 
`add()`-operaatio ei vaadi ylimääräistä tilaa, muuta kuin vakiomäärän, joten tilavaativuus on `O(1)`.

`extractMin()` poistaa ensin minimikeon juuressa olevan (eli pienimmän) solmun, siirtää sisäisen listan viimeisen alkion ensimmäiseksi, ja kutsuu `minHeapify()` operaatiota juurialkio parametrinaan. 
`minHeapify()` ottaa paremetriksi listan indeksin, joka kuvaa minimikeon solmua. `minHeapify()` olettaa että annetun solmun vasen ja oikea alipuu ovat jo minimikekoja, joten kun kutsutaan `minHeapify(0)`, on minimikeon pienin alkio joko indeksissä 0 tai sen vasemmassa tai oikeassa alipuussa (indeksit 1 ja 2). `minHeapify()` tarkistaa onko jomman kumman alipuun juurisolmu pienempi kuin tällä hetkellä
käsittelyssä oleva solmu, ja jos on, vaihtaa tällä hetkellä käsittelyssä olevan solmun ja pienemmän alipuun juurisolmun paikkoja, ja kutsuu rekursiivisesti `minHeapify()`-operaatiota sen alipuun juuren indeksillä,
jota muutettiin. Tällä varmistetaan, että kekoehto säilyy operaatioiden jälkeen. `minHeapify()` käy pahimmassa tapauksessa läpi koko keon ylhäältä alas, eli aikavaativuus on suoraan suhteessa puun korkeuteen, aikavaativuus on siis `O(log n)`. Tilavaativuus on rekursiivisesta kutsusta johtuen myös `O(log n)`. Tätä saisi optimoitua `O(1)` tilavaativuuteen muuttamalla rekursion silmukaksi. 
Koska kaikki muut `extractMin()`-operaatiossa tehdyt toiminnot onnistuvat ajassa ja tilassa `O(1)`, määrittää `minHeapify()`-operaatio aika- ja tilavaativuudet myös `extractMin()`-operaatiolla. 
Aika- ja tilavaativuus on siis `O(log n)`.

`contains()` tarkistaa kuuluuko annettu alkio minimikekoon ja palauttaa `true` jos kuuluu, `false` jos ei kuulu. Koska `add()`- ja `extractMin()`-operaatioiden yhteydessä päivitetään hajautustaulua,
onnistuu `contains()`-operaation suoritus (keskimäärin) vakioajassa. 

### OwnLinkedList
Linkitetyn listan toteutus. Toteuttaa `java.util.List`-rajapinnan kaikki toiminnot `subList()`-toimintoa lukuunottamatta.

Linkitetty lista on toteutettu kahteen suuntaan linkitettynä rengaslistana,
jonka ensimmäisen alkion edellinen solmu on listan viimeinen solmu ja viimeisestä solmusta seuraava solmu on listan ensimmäinen solmu.

Toteutus mahdollistaa `add()`-toiminnon vakioajassa `O(1)`, jos alkio lisätään listan loppuun. Jos alkio tarvitsee lisätä annetuun indeksiin, joudutaan
oikea listan solmu etsiä käymällä lista läpi listan ensimmäisestä solmusta alkaen, joten tällöin aikavaativuus on lineaarinen `O(n)`. 

Listan läpikäynti iteraattorilla onnistuu lineaarisessa ajassa `O(n)`. 

`remove()`-operaatio ottaa parametriksi listan indeksin, joka joudutaan jälleen etsimään lineaarisessa ajassa ja aikavaativuus on `O(n)`. 

Listassa ylläpidetään erikseen kokonaislukumuuttujassa listan tämänhetkistä kokoa, joten `size()` operaation aikavaativuus on `O(1)`.

Kaikki operaatiot ovat tilavaativuudeltaan vakiotilaisia `O(1)`-operaatioita.

### OwnList
Dynaaminen lista, joka vastaa esim. Javan `java.util.ArrayList`-toteutusta. Toteuttaa `java.util.List`-rajapinnan kaikki toiminnot `subList()`-toimintoa lukuunottamatta.

Lista on toteuttettu käyttämällä sisäisesti Javan array-taulukkotietorakennetta. Koska array on aina tietyn kokoinen, voi olla että listaan lisättäessä joudutaan arrayta kasvattamaan. 

`add()` operaatio on vakioaikainen `O(1)`jos alkio lisätään listan loppuun, ja jos sisäisessa arrayssa on entuudestaan riittävästi tilaa. Jos arrayta joudutaan kasvattamaan,
joudutaan kaikki listan alkiot siirtämään uuteen arrayyn, jolloin aikavaativuus on `O(n)`. Samoin jos alkio lisätään listan keskelle, pitää alkion jälkeisiä alkioita siirtää eteenpäin
ja aikavaativuus on `O(n)`. `add()`-operaation aikavaativuus on siis keskimäärin `O(1)` listan loppuun lisättäessä ja `O(n)` listan keskelle lisättäessä. 
Tilavaativuus on keskimäärin vakioaikainen `O(1)`. Poikkeuksena on tilanne, jolloin sisäistä taulukkoa joudutaan kasvattamaan, jolloin pahimman tapauksen tilavaativuus on `O(n)`.

`get()` operaatio hakee annetulla indeksillä löytyvän alkion suoraan listan sisäisestä taulukosta ja aika- ja tilavaativuudet ovat vakiot `O(1)`.

`remove()` operaatiossa poistettavan alkion jälkeisiä alkioita joudutaan siirtämään, joten aikavaativuus on `O(n)`. Ylimääräistä tilaa poistoon ei tarvita, joten tilavaativuus on `O(1)`.

`set()` operaatio asettaa annettuun indeksiin uuden alkion, ja aika- ja tilavaativuudet ovat vakiot `O(1)`.

### OwnMap
Hajautustaulun toteutus, joka vastaa esim. Javan `java.util.HashMap`-toteutusta. Tukee kaikkia `java.util.Map`-rajapinnan toimintoja.

Hajautustaulu on sisäisesti järjestetty niin, että hajautustaulu sisältää `c`-kokoisen taulukon linkitettyjä listoja. `c` on hajautustaulun sen hetkinen kapasiteetti. 
Kun hajautustauluun lisätään avain-arvopari, otetaan lisättävästä avaimesta `hashCode` ja modulo-operaatiolla muutetaan se `c`:n rajoihin. Oikean linkitetyn listan indeksi
taulukossa löytyy siis kaavalla `key.hashCode() % c`. 

Hajautustaulun aikavaativuuksissa oletetaan, että avaimen `hashCode()` lasketaan vakioajassa. Toinen oletus on, että avaimen `hashCode()`:n palauttamat tiivisteet jakautuvat tasaisesti.
Esim. jos `hashCode()` palauttaisi aina saman numeron, päätyisi kaikki hajautustaulun alkiot samaan linkitettyyn listoihin, ja operaatioiden aikavaativuus kasvaisi lineaariseksi `O(n)`.

Hajautustaululla on parametri `loadFactor`, joka kertoo kuinka täysi hajautustaulun sisäinen tietorakenne saa olla ennen kuin tietorakenteen kokoa pitää kasvattaa. 
Tässä toteutuksessa `loadFactor` on aina vakio 0.75, mutta olisi helposti muutettavissa käyttäjän määriteltäväksi. Toinen parametri `initialCapacity` kertoo kuinka suuri
hajautustaulun sisäinen tietorakenne on kun hajautustaulu luodaan. Tässä toteutuksessa `initialCapacity` on aina 10, mutta myös tämä parametri on helposti muutettavissa. 
Kun tietorakenteen kokoa joudutaan kasvattamaan, sen koko aina tuplaantuu, eikä pienene ikinä vaikka hajautustaulu tyhjennettäisiin. 

Oleelissimmat operaatiot ovat `put()`, `get()` ja `containsKey()`. Kaikissa operaatioissa lasketaan ensin avaimelle kuuluvan linkitetyn listan indeksi yllä kuvatulla tavalla. 
Lisäksi jos kyseisessä indeksissä on jo lista, pitää se käydä läpi ja tarkistaa löytyykö annetulla avaimella olemassa olevaa alkiota. Jos tiivisteet jakautuvat tasaisesti, niin
yleensä listaa ei ole, tai sen koko on hyvin pieni suhteessa koko tietorakenteen kokoon, joten keskimäärin operaatioissa päästään aikavaativuuteen `O(1)`. Pahimmassa tapaukseessa
suuri määrä avaimista päätyy samaan linkitettyyn listaan, tai lisäysoperaatiossa joudutaan kasvattamaan sisäistä tietorakennetta, jolloin aikavaativuus pahimmassa tapauksessa on `O(n)`.
Ylimääräistä tilaa operaatiot tarvitsevat vain vakion `O(1)` verran. 

### OwnSet
Joukon toteutus, joka vastaa esim. Javan `java.util.HashSet`-toteutusta. Tukee kaikkia `java.util.Set`-rajapinnan toimintoja.
Käyttää sisäisesti `OwnMap`-luokkaa, koska siinä luokassa on jo toteutettu tarvittava tiivisteen perusteella lokerointi. `OwnMap`-luokasta hyödynnetään joukon tapauksessa pelkästään avaimia, kaikki arvot asetetaan `null`-arvoiksi. Aika- ja tilavaativuudet ovat kuten `OwnMap`-luokalla. 
Aika- ja tilavaativuudet ovat siis kuten `OwnMap`-luokassa. 

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
