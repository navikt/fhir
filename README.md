# NAV FHIR
Dette repoet er tiltenkt å fungerer som et monorepo for kildekoden til alle NAV sine FHIR Implementation Guides (IG).
De forskjellige IGene genereres og publiseres automatisk som github-pages:
1. [MessagingCore](https://navikt.github.io/fhir/MessagingCore/)

## FHIR Shorthand og SUSHI
Repoet inneholder [FHIR Shorthand](https://fshschool.org/) (FSH) prosjekter. FSH er et deklarativt og utviklervennelig språk for å definere FHIR ressursene som inngår i profiler. FHIR ressurser genereres vha. [SUSHI](https://github.com/FHIR/sushi); en kompilator som transformerer FSH til FHIR ressurser, dette repoet vil derfor ikke inneholde FHIR ressurser av type ImplementationGuide, StructureDefinition, CodeSystem, ValueSet etc. SUSHI vil også benytte **sushi-config.yaml** [for å generere](http://build.fhir.org/ig/HL7/fhir-shorthand/branches/beta/sushi.html#configuration-file) **package-list.json og menu.xml** som kreves av IG Publisher.

NAV har valgt å bruke FSH istedenfor [Forge](https://fire.ly/products/forge/); et GUI verktøy som generer FHIR ressurser, siden det gir bedre kildekodekontroll og lettere lar seg integerere i CI\CD-pipelines. Forge krever også lisens og fungerer bare på Windows.

## Bruk av begreper
I FHIR verden brukes ofte begrepene implementasjonsguide, pakke, profiler og kontrakter\datamodeller om hverandre. Dette kan gjerne være forvirrende og vi skal her prøve å oppklare dette:

* En [FHIR profil](https://www.hl7.org/fhir/profiling.html) er en kontrakt (datamodell+regler) for bruk av en FHIR ressurs (f.eks Patient). En profil er implementert som egne FHIR ressurser (json/xml) laget for dette formålet, f.eks StructureDefinition.
* En FHIR Implementation Guide er en samling av:
    * FHIR ressurser som utgjør profiler.
    * En HTML side for human-readable dokumentasjon.
    * Generell metadata implementert som en egen FHIR ressurs som heter [ImplementationGuide](https://www.hl7.org/fhir/implementationguide.html).
    * Eksempler på FHIR ressurser som er ihht. profilene.
* En [FHIR NPM Package](https://registry.fhir.org/learn) er IGen pakket som en NPM pakke. Avengigheter til andre IGer er gjenngitt som versionerte dependencies i package.json. En slik pakke kan brukes til validering og testing.

Mao. er det en 1:1:1 mapping mellom FSH-project, IG og FHIR Package.

## IG Publisher
[IG Publisher](https://confluence.hl7.org/display/FHIR/IG+Publisher+Documentation) er en open source java applikasjon ([ref](https://github.com/HL7/fhir-ig-publisher)) som tar fhir-ressurser, markdown og bilder som input og bruker Jekyll til å generere en statisk HTML side som kan brukes som dokumentasjon. Dette pakker den også i en FHIR NPM Package for distribuering.

Et FSH prosjekt følger en bestemt struktur [beskrevet her](https://fshschool.org/docs/sushi/project/), denne strukturen har likheter med [strukturen som forventes av IG Publisher](https://build.fhir.org/ig/FHIR/ig-guidance/using-templates.html). Tidligere var det slik at SUSHI måtte kjøres på et FSH prosjekt for å generere inputten til IG Publisher, men [fra og med v1.0.75 er dette unødvendig](http://build.fhir.org/ig/HL7/fhir-shorthand/branches/beta/sushi.html#ig-publisher-integration-autobuild-configuration).

## CI / CD
Generering og deployment av IGene gjøres vha. github-actions som kjører IG Publisher og commiter artefaktene (html, css, js, assets) til en egen **gh-pages branch** som hostes med github-pages. Dette kunne alternativt blitt gjort av HL7 sin [Auto-IG-builder](https://github.com/FHIR/auto-ig-builder), men da mister vi litt fleksibilitet, vi må f.eks bruke domenet `https://build.fhir.org/ig`.
