# AI Legal Oponent: Demo Use Case
> **Téma:** Skončenie nájmu a spoločný nájom manželov
> **Cieľ:** Ukázať schopnosť AI "reasoningu" a synergiu s judikatúrou (RAG).

Tento scenár demonštruje, ako **AI Legal Oponent** chráni používateľa pred právnymi chybami, ktoré by bežné fulltextové vyhľadávanie neodhalilo.

---

## Scenár
**Situácia:** Používateľ (prenajímateľ) chce ukončiť nájomnú zmluvu s manželským párom. Myslí si, že postupoval správne podľa všeobecných ustanovení zákona, ale neuvedomuje si špecifiká judikatúry.

### 1. Vstupné dáta (Knowledge Base)
Systém má k dispozícii tieto dokumenty v RAG databáze:
* `obciansky_zakonnik_najom.txt`: Všeobecné ustanovenia Občianskeho zákonníka o výpovedi (§ 677 a nasl.).
* `judikat_manzelia.txt`: Povinnosť doručiť výpoveď obom manželom.

---

### 2. Priebeh Interakcie (Demo Flow)

#### Vstup používateľa (User Prompt)
*Používateľ hrá rolu sebavedomého prenajímateľa:*

> "Mám nájomníkov, manželov Novákovcov, ktorí neplatia. Minulý týždeň som poslal výpoveď z nájmu pánovi Novákovi. Prevzal ju, takže výpovedná lehota začala plynúť a som krytý."

#### Proces na pozadí (Reasoning)
1.  **Analýza entity:** detekcia kľúčových slov.
2.  **Retrieval (Vyhľadávanie):** Nájdenie § 677 (všeobecná výpoveď) ALE aj judikát NS SR (špeciálna podmienka pre manželov).
3.  **Logická kontrola:** AI porovná "poslal som pánovi Novákovi" vs. "musí byť doručená obom".
4.  **Výsledok:** Identifikuje rozpor (kritickú chybu).

#### Odpoveď AI Oponenta (Output)
*Vygenerovaná odpoveď systému:*

```text
⚠️ VAROVANIE: Váš aktuálny postup je právne neúčinný. Nájomná zmluva naďalej trvá a výpovedná lehota nezačala plynúť.

Ako váš právny oponent vás musím upozorniť, že spoliehať sa na doručenie výpovede len jednému z manželov je fatálna chyba, ktorá by viedla k prehre na súde pri prípadnej žalobe o vypratanie.

Tu je podrobná analýza vašej situácie na základe poskytnutých dokumentov:

1. Analýza právnej situácie a identifikácia chyby
Váš predpoklad, že "ste krytý" prevzatím výpovede pánom Novákom, je nesprávny. Keďže ide o manželov, vznikol im tzv. spoločný nájom bytu manželmi.

Toto právo sa vyznačuje tým, že svedčí obom manželom spoločne a nedielne.

Vzhľadom na túto nedielnosť sa vyžaduje, aby výpoveď bola daná obidvom manželom a obidvom musí byť tiež doručená.

Kde ste urobili chybu: Adresovali a doručili ste výpoveď iba manželovi. V zmysle judikatúry (súdnych rozhodnutí) však výpovedi, ktorá smeruje len voči jednému z manželov, nemožno priznať právnu relevanciu.

2. Právne následky a riziká
Súčasný stav je pre vás mimoriadne nevýhodný:

Neplatnosť vs. Ničotnosť: V tomto prípade nejde len o to, že by výpoveď bola neplatná. Právny názor je taký, že o zániku nájmu nemožno ani uvažovať. Z právneho hľadiska je to tak, akoby ste žiadnu výpoveď ani nepodali.

Neplatenie nájomného: Hoci nájomníci neplatia, proces ukončenia nájmu ste nezačali platne. Čas, ktorý teraz plynie, sa nezapočítava do výpovednej lehoty.


Súdny spor: Ak by ste podali žalobu o vypratanie bytu na základe tejto výpovede, súd by ju zamietol, pretože nájomný vzťah stále trvá v zmysle § 710 Občianskeho zákonníka.

3. Odporúčaný správny postup
Aby ste dosiahli platné ukončenie nájmu, musíte postupovať nasledovne v súlade so zákonom a judikatúrou:

Vyhotovte novú výpoveď: Musí byť adresovaná menovite obom manželom (pán Novák aj pani Nováková).

Doručte výpoveď obom:

Ideálne pošlite dve samostatné zásielky do vlastných rúk (jednu na meno pána, druhú na meno pani), alebo zabezpečte, aby na preberacom protokole/doručenke figurovali podpisy oboch manželov.

Pamätajte, že výpoveď musí byť doručená obidvom.

Dodržte výpovednú lehotu:

Pokiaľ nemáte v zmluve dohodnuté inak, pri nájme nehnuteľností platí trojmesačná výpovedná lehota.

Nájomnú zmluvu na neurčitú dobu možno zrušiť iba výpoveďou (ak nedôjde k dohode).

Zhrnutie: Vaša súčasná výpoveď je "zdrap papiera". Musíte začať odznova a doručiť výpoveď každému z manželov osobitne.