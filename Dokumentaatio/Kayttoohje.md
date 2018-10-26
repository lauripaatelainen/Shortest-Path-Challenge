# Käyttöohje

## Sovelluksen käynnistäminen

Sovellus käynnistetään suorittamalla valmiiksi paketoitu jar-paketti. (Lataa jar-paketti [täältä](../jar/Shortest-Path-Challenge.jar))

java -jar Shortest-Path-Challenge

## Pelin käynnistäminen

Peli käynnistetään painamalla aloitusruudulta yläreunasta 'Uusi peli'-nappia,
ja antamalla ruudulle aukeavaan dialogiin pelikentän koko. Koko kuvaa pelikentän
sivun pituutta, eli esim. kentän koolla 3, peli kenttä on 3x3 kokoinen sisältäen 9 solmua. 

## Pelin pelaaminen

Tarkoitus on löytää vasemmassa ylänurkassa olevasta lähtösolmusta lyhin polku
oikeassa alanurkassa olevaan maalisolmuun. 
Peli näyttää vihreällä merkittynä pelaajan tämänhetkisen polun. Alkuun vain vasemman
ylänurkan lähtösolmu on vihreällä ja polkua saa jatkettua klikkaamalla mitä tahansa 
sallittua seuraavaa solmua. 

Kun maalisolmu on saavutettu ja pelaaja uskoo löytäneensä oikean vastauksen, pitää 
vastaus vahvistaa painamalla pelikentän alapuolelta nappia 'Vahvista vastaus'. Tämän jälkeen
pelaajalle ilmoitetaan oliko hän löytänyt lyhimmän polun vai ei. Oikea lyhin ratkaisu etsitään
tässä vaiheessa kolmella eri algoritmilla, ja niiden löytämät lyhimmät polun merkitään pelikenttään
eri värein. Samoin algoritmien ajoaika näytetään käyttäjälle pelikentän alapuolella algoritmien
vertailua varten. 
