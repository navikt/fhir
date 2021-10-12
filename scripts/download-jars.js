const fs = require('fs');
const download = require('download');
 
const downloads = [
    'https://github.com/hapifhir/org.hl7.fhir.core/releases/latest/download/validator_cli.jar',
    'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.8.1/junit-platform-console-standalone-1.8.1.jar',
    'https://github.com/navikt/fhir-validator-junit-engine/releases/latest/download/fhir-validator-junit-engine.jar'
].filter(url => !fs.existsSync('libs/'.concat(url.split('/').pop())));

(async () => { await Promise.all(downloads.map(url => download(url, 'libs'))) })();