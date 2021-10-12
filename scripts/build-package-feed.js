const fs = require('fs');
const download = require('download');
const { Liquid } = require('liquidjs');

(async () => {
  const releases = await download('https://api.github.com/repos/navikt/fhir/releases');
  const json = JSON.parse('{"releases":' + releases + '}');
  const template = fs.readFileSync('package-feed.liquid').toString();
  const result = await new Liquid().parseAndRender(template, json);
  fs.writeFileSync("package-feed.xml", result);
})();