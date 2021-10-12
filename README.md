# Overview
Dette repoet er tiltenkt å fungerer som et monorepo for kildekoden til alle NAV sine FHIR Implementation Guides (IG).
De forskjellige IG-ene genereres og publiseres automatisk som github-pages:
1. [MessagingCore](https://navikt.github.io/fhir/igs/MessagingCore/)
2. [SMART Forms](https://navikt.github.io/fhir/igs/SmartForms/)

## 🐟 FHIR Shorthand og SUSHI
Repoet inneholder [FHIR Shorthand](https://fshschool.org/) (FSH) prosjekter. FSH er et deklarativt og utviklervennelig språk for å definere FHIR-ressursene som inngår i profiler. FHIR-ressurser genereres vha. [SUSHI](https://github.com/FHIR/sushi): En kompilator som transformerer FSH til FHIR-ressurser. Dette repoet inneholde derfor ikke FHIR-ressurser av type ImplementationGuide, StructureDefinition, CodeSystem, ValueSet etc.

SUSHI bruker **sushi-config.yaml** [for å generere](http://build.fhir.org/ig/HL7/fhir-shorthand/branches/beta/sushi.html#configuration-file) **package-list.json og menu.xml** som kreves av IG Publisher, disse filene er derfor heller ikke med.

NAV har valgt å bruke FSH istedenfor [Forge](https://fire.ly/products/forge/): Et GUI-verktøy som generer FHIR-ressurser. Fordi det gir bedre kildekodekontroll og lettere lar seg integerere i CI\CD-pipelines. Forge krever også lisens og fungerer bare på Windows.

For å eksperimentere med FSH kan web-verktøyet [FSH Online](https://fshschool.org/FSHOnline/#/) anbefales.

## 📙 Bruk av begreper
I FHIR-verden brukes ofte begrepene implementasjonsguide, pakke, profiler og kontrakter\datamodeller om hverandre. Dette kan være forvirrende og vi skal prøve å oppklare:

* En [FHIR-profil](https://www.hl7.org/fhir/profiling.html) er en kontrakt (datamodell+regler) for bruk av en FHIR-ressurs (f.eks Patient). En profil er implementert som egne FHIR-ressurser (json/xml) laget for dette formålet, f.eks StructureDefinition.
* En FHIR Implementation Guide er en samling av:
    * FHIR-ressurser som utgjør profiler.
    * En HTML-side for human-readable dokumentasjon.
    * Generell metadata implementert som en egen FHIR-ressurs som også heter [ImplementationGuide](https://www.hl7.org/fhir/implementationguide.html).
    * Eksempler på FHIR-ressurser som er ihht. profilene.
* En [FHIR NPM Package](https://registry.fhir.org/learn) er IG-en uten dokumentasjon pakket som en NPM-pakke. Avengigheter til andre IG-er er gjenngitt som versionerte dependencies i *package.json*. En slik pakke kan brukes til validering og testing av FHIR-ressurser.

Mao. er det en 1:1:1 mapping mellom FSH-project, IG og FHIR Package.

## 📦 IG Publisher
[IG Publisher](https://confluence.hl7.org/display/FHIR/IG+Publisher+Documentation) er en open source java applikasjon ([github](https://github.com/HL7/fhir-ig-publisher)) som tar FHIR-ressurser, markdown og bilder som input og bruker Jekyll til å generere en statisk HTML-side som kan brukes som dokumentasjon. Dette pakker den også i en FHIR NPM Package for distribuering.

Et FSH-prosjekt følger en [bestemt struktur](https://fshschool.org/docs/sushi/project/), denne strukturen har likheter med [strukturen som forventes av IG Publisher](https://build.fhir.org/ig/FHIR/ig-guidance/using-templates.html). Tidligere var det slik at SUSHI måtte kjøres på et FSH-prosjekt for å generere inputen til IG Publisher, men [fra og med v1.0.75](http://build.fhir.org/ig/HL7/fhir-shorthand/branches/beta/sushi.html#ig-publisher-integration-autobuild-configuration) kalles SUSHI automatisk dersom en `/fsh`-mappe eksisterer.

## 🧪 Testing
Testing av profiler gjøres ved å bruke den offisielle FHIR-validatoren. Her bruker vi [fhir-validator-junit-engine](https://github.com/navikt/fhir-validator-junit-engine) for å definere og kjøre test-scenarioer i YAML-filer.

## 🚀 CI/CD
Generering og deployment av IG-ene gjøres vha. github-actions som kjører IG Publisher og committer artefaktene (html, css, js, assets) til en egen **gh-pages branch** som hostes med github-pages. Dette kan alternativt bli gjort av HL7 sin [Auto-IG-builder](https://github.com/FHIR/auto-ig-builder), men da mister vi litt fleksibilitet, vi må f.eks bruke domenet: `https://build.fhir.org/ig`.

### Releases
Publisering av releases til [FHIR Package Registry](https://registry.fhir.org/) krever en del manuelle steg som er [dokumentert her](https://confluence.hl7.org/pages/viewpage.action?pageId=97454344#FHIRPackageRegistryUserDocumentation-Themanualprocess). Vi har prøvd å automatisere dette så mye som mulig vha. GitHub-actions. 

Gitt at følgende kriterier for en IG gjelder:
- *Version* i *sushi-config.yaml* er oppdatert. 
- *ReleaseLabel* i *sushi-config.yaml* er satt til **release**.
- Versionen er dokumentert i `{ig}/input/pagecontent/CHANGELOG.md`.

Når nevnte kriterier er oppfyllt vil en ny repository-release lages og [package-feed.xml](https://navikt.github.io/fhir/package-feed.xml) oppdateres. Hvis feed-en er registrert i HL7 sin [package-feeds.json](https://github.com/FHIR/ig-registry) vil pakken automatisk publiseres til FHIR Package Registry. Husk at en versionert pakke-publikasjon er immutable og ikke kan slettes.

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBzdWJncmFwaCBHZW5lcmF0aW5nXG4gICAgSUdQW0lHIFB1Ymxpc2hlcl0gLS0-IFdbL2h0bWwsIGNzcywganMsIGltZy9dXG4gICAgSUdQIC0tPiBQWy9ucG0gcGFja2FnZS9dXG4gICAgZW5kXG4gICAgc3ViZ3JhcGggUHJvZmlsaW5nXG4gICAgRlNIW1Nob3J0aGFuZF0gLS0-IHxTVVNISXwgQ1xuICAgIEZbRm9yZ2VdIC4tPiBDWy9GSElSIENvbmZvcm1hbmNlIHJlc291cmNlcy9dXG4gICAgVFtUcmlmb2xpYV0gLi0-IENcbiAgICBPW290aGVyXSAuLT4gQ1xuICAgIGVuZFxuICAgIHN1YmdyYXBoIFRlc3RpbmdcbiAgICBDIC0tPiBURVNUe1Rlc3R9XG4gICAgVEVTVCAtLT4gfHN1Y2Nlc3N8IElHUFxuICAgIGVuZFxuICAgIHN1YmdyYXBoIERvY3VtZW50aW5nXG4gICAgTVsvbWFya2Rvd24vXSAtLT4gSUdQXG4gICAgQVsvYXNzZXRzL10gLS0-IElHUFxuICAgIFRFWy90ZW1wbGF0ZS9dIC0tPiBJR1BcbiAgICBlbmRcbiAgICBzdWJncmFwaCBIb3N0aW5nXG4gICAgVyAtLT4gV0hbV2ViIGhvc3RdXG4gICAgUCAtLT4gUltGSElSIFBhY2thZ2UgcmVnaXN0cnldXG4gICAgZW5kXG4gICAgc3R5bGUgUHJvZmlsaW5nIGZpbGw6I0EzRTRENyxzdHJva2U6IzMzM1xuICAgIHN0eWxlIFRlc3RpbmcgZmlsbDojQUJCMkI5LHN0cm9rZTojMzMzXG4gICAgc3R5bGUgRG9jdW1lbnRpbmcgZmlsbDojZjlmLHN0cm9rZTojMzMzXG4gICAgc3R5bGUgR2VuZXJhdGluZyBmaWxsOiNmOTYsc3Ryb2tlOiMzMzNcbiAgICBzdHlsZSBIb3N0aW5nIGZpbGw6IzU0OTlDNyxzdHJva2U6IzMzM1xuICAgIHN0eWxlIEYgc3Ryb2tlLWRhc2hhcnJheTogNVxuICAgIHN0eWxlIFQgc3Ryb2tlLWRhc2hhcnJheTogNVxuICAgIHN0eWxlIE8gc3Ryb2tlLWRhc2hhcnJheTogNSIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2UsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjpmYWxzZX0)](https://mermaid.live/edit/#eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBzdWJncmFwaCBHZW5lcmF0aW5nXG4gICAgSUdQW0lHIFB1Ymxpc2hlcl0gLS0-IFdbL2h0bWwsIGNzcywganMsIGltZy9dXG4gICAgSUdQIC0tPiBQWy9ucG0gcGFja2FnZS9dXG4gICAgZW5kXG4gICAgc3ViZ3JhcGggUHJvZmlsaW5nXG4gICAgRlNIW1Nob3J0aGFuZF0gLS0-IHxTVVNISXwgQ1xuICAgIEZbRm9yZ2VdIC4tPiBDWy9GSElSIENvbmZvcm1hbmNlIHJlc291cmNlcy9dXG4gICAgVFtUcmlmb2xpYV0gLi0-IENcbiAgICBPW290aGVyXSAuLT4gQ1xuICAgIGVuZFxuICAgIHN1YmdyYXBoIFRlc3RpbmdcbiAgICBDIC0tPiBURVNUe1Rlc3R9XG4gICAgVEVTVCAtLT4gfHN1Y2Nlc3N8IElHUFxuICAgIGVuZFxuICAgIHN1YmdyYXBoIERvY3VtZW50aW5nXG4gICAgTVsvbWFya2Rvd24vXSAtLT4gSUdQXG4gICAgQVsvYXNzZXRzL10gLS0-IElHUFxuICAgIFRFWy90ZW1wbGF0ZS9dIC0tPiBJR1BcbiAgICBlbmRcbiAgICBzdWJncmFwaCBIb3N0aW5nXG4gICAgVyAtLT4gV0hbV2ViIGhvc3RdXG4gICAgUCAtLT4gUltGSElSIFBhY2thZ2UgcmVnaXN0cnldXG4gICAgZW5kXG4gICAgc3R5bGUgUHJvZmlsaW5nIGZpbGw6I0EzRTRENyxzdHJva2U6IzMzM1xuICAgIHN0eWxlIFRlc3RpbmcgZmlsbDojQUJCMkI5LHN0cm9rZTojMzMzXG4gICAgc3R5bGUgRG9jdW1lbnRpbmcgZmlsbDojZjlmLHN0cm9rZTojMzMzXG4gICAgc3R5bGUgR2VuZXJhdGluZyBmaWxsOiNmOTYsc3Ryb2tlOiMzMzNcbiAgICBzdHlsZSBIb3N0aW5nIGZpbGw6IzU0OTlDNyxzdHJva2U6IzMzM1xuICAgIHN0eWxlIEYgc3Ryb2tlLWRhc2hhcnJheTogNVxuICAgIHN0eWxlIFQgc3Ryb2tlLWRhc2hhcnJheTogNVxuICAgIHN0eWxlIE8gc3Ryb2tlLWRhc2hhcnJheTogNSIsIm1lcm1haWQiOiJ7XG4gIFwidGhlbWVcIjogXCJkZWZhdWx0XCJcbn0iLCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)

# Development
For å bygge IG-er lokalt trenger du SUSHI, IG Publisher og alle avhengighetene. Dette kan du installere ved å følge de respektive installasjonsveiledningene, alternativt kan du bruke et docker-image, isåfall må du installere [Docker](https://docs.docker.com/get-docker/).

## 🖥️ NPM Tasks
Det er en `package.json` som inkluderer dependencies og pre-definerte skript for å automatisere vanlige oppgaver som: bygg, test, validate, clean++. Disse skriptene skal fungere på både linux, osx og windows.

Sørg først for å installere [JDK (>1.8)](https://adoptopenjdk.net/installation.html) samt [Node](https://nodejs.org/en/). Deretter åpner du et terminalvindu i root-katalogen og kjør `npm ci`, dette installere alle node-modules samt laster ned nødvendige .jar filer.

NPM tasks kan kjøres vha. en terminal eller gjennom vscode, følgende tasks er definert:
- build:docker: Bygger docker-imaget som brukes for kjøre IG-publisher.
- build:ig: Kjører IG Publisher vha. docker-imaget bygd av *build:docker*.
- build:package-feed: Genererer *package-feed.xml* vha. liquid template og GitHub releases API.
- build:sushi : Transformerer FSH filer til FHIR json ressurser.
- clean: Sletter genererte filer.
- test: Kjører tester som verifiserer at FHIR Validering av test ressurser vha. IG gir forventet resultat.
- validate: Validerer FHIR ressursene som inngår i IG-en.

Eksempel på kjøring av en NPM-task:
```
npm run validate
```

Flere av task-ene kjører mot en IG. For å velge IG må du editere `.npmrc` filen.

## 👨‍💻 Visual Studio Code
For utvikling av IG-er er det greit å bruke [vscode](https://code.visualstudio.com/).

[vscode-language-fsh](https://marketplace.visualstudio.com/items?itemName=kmahalingam.vscode-language-fsh) extension hjelper med litt syntax-highlighting og IntelliSense, og skal komme som en [anbefaling](.vscode/extensions.json) når du åpner repoet i vscode.

Vi har lagt til FHIR json-schema referanse i [.vscode/settings.json](.vscode/settings.json) slik at du får IntelliSense dersom du jobber med FHIR json-ressurser direkte.

Det er laget egne tasks i [.vscode/tasks.json](.vscode/tasks.json) som kan brukes for å bygge og teste IG-en, SUSHI er registrert som en bygg-task og kan dermed kjøres vha. **ctrl+shift+b** hurtigtast, de andre task-ene kan du velge ved å trykke **F1** og deretter skrive **Tasks: Run task**.

![how to run tasks gif](docs/run-task.gif)

## 🐋 Docker build image
Denne seksjonen trenger du ikke forholde deg til dersom du bruker Tasks i npm eller vscode.

Fordi transformeringen av et FSH-prosjekt til en IG krever mange dependencies (java, nodejs, npm, ruby, jekyll, sushi, ig-publisher etc.) har vi laget en Dockerfile for å bygge et docker-image som inneholder både SUSHI, IG Publisher og [FHIR Validator](https://confluence.hl7.org/display/FHIR/Using+the+FHIR+Validator) + alle nødvendige dependencies. 

Kjør følgende kommando fra *.docker* katalogen til dette repoet for å bygge docker-imaget, dette tar ca 4 minutter.
```
docker build -t navikt/fhir-ig-dev .
```

[SUSHI](https://fshschool.org/docs/sushi/running/#running-sushi) kjøres med følgende kommando, dette tar normalt noen sekunder og vil generere en **fsh-generated** katalog med FHIR-ressurser innenfor FSH-prosjektet.
```
docker run --rm -v {fsh-project-dir}:/data navikt/fhir-ig-dev sushi /data

## Eksempel på windows:
docker run --rm -v c:\repos\fhir\igs\MessagingCore:/data navikt/fhir-ig-dev sushi /data
```

[FHIR Validator](https://confluence.hl7.org/display/FHIR/Using+the+FHIR+Validator#UsingtheFHIRValidator-Runningthevalidator) brukes for å validere FHIR-ressurser. F.eks kan ressursene generert av SUSHI valideres med følgende kommando:
```
docker run --rm -v {fsh-project-dir}:/data navikt/fhir-ig-dev validator /data/fsh-generated/resources
```

[IG Publisher](https://wiki.hl7.org/IG_Publisher_Documentation#Running_in_command_line_mode) kjøres med følgende kommando, dette tar gjerne 3-4 minutter og vil generere en rekke kataloger innenfor prosjektet.
```
docker run --rm -v {fsh-project-dir}:/data navikt/fhir-ig-dev publisher -ig /data/ig.ini
```

### Package-cache
Eksemplene over bruker alle [`--rm`](https://docs.docker.com/engine/reference/run/#clean-up---rm) flagget som gjør at containeren slettes etter kjøringen, dette vil si at alle pakker må lastes ned for hver gang og det kan derfor være greit å lage et eget volume for **package-cache**, i tillegg kan det være greit å bruke `-it` flagget slik at du kan bruke *ctrl+c* for å avbryte kjøringer. Eksempel med SUSHI blir da følgende (gjelder også validator og publisher):
```
docker run -it --rm -v package-cache:/root/.fhir -v c:\repos\fhir\igs\MessagingCore:/data navikt/fhir-ig-dev sushi /data
```
