# NAV FHIR Profiles
Dette repoet inneholder [FHIR Shorthand](https://build.fhir.org/ig/HL7/fhir-shorthand/) (FSH) prosjekter. FSH er et deklarativt og utviklervennelig språk for å definere FHIR ressursene som inngår i profiler. FHIR ressurser genereres vha. [SUSHI](https://github.com/FHIR/sushi); en kompilator som transformerer FSH til FHIR ressurser, dette repoet vil derfor ikke inneholde FHIR ressurser av type StructureDefinition etc.

NAV har valgt å bruke FSH istedenfor [FORGE](https://fire.ly/products/forge/); et GUI verktøy som generer FHIR ressurser, siden det gir bedre kildekodekontroll og lettere lar seg integerere i CI\CD-pipelines.
