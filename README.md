#Dokumentáció

GET /api/contacts
* Tízesével kilistázza az aktív státuszú kapcsolattartókat
* Query paraméterben lehet megadni az oldalszámot (/api/contacts?page=), ha ezt nem adjuk meg, az első oldalt kapjuk
* A felhasználói esetben lévő hivatkozást az ID helyettesíti

GET /api/contacts/{id}
* Részletes leírás egy kapcsolattartóról
* Az ID-t Pathvariable-ként lehet megadni (api/contacts/1)
* Ha nem használatban lévő id-t adunk meg, hibaüzenetet ad

POST /api/contacts
* Új kapcsolattartó létrehozása
* A post kéréssel Jsonban megadandó: 
  * vezetéknév(lastName)
  * keresztnév(firstName)
  * vállalat(companyName)
  * ímél(eMail)
  * telefonszám(phoneNumber)
  * komment(comment)
* Rossz formátumú ímél vagy telefonszám esetén hibaüzenetet ad

PUT /api/contacts/id
* Kapcsolattartó adatainak megváltoztatása
* A változtatandó kapcsolattartó ID-jét a részletezéshez hasonlóan kell megadni.
* Az adatokat új létrehozásához hasonlóan kell megadni.

DELETE /api/contacts/id
* Kapcsolattartó státuszának töröltre váltása
* A kapcsoattartó nem törlődik.
