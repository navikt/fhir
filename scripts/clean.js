const fs = require('fs');
const glob = require('glob');

glob('igs/*/+(fsh-generated|output|input-cache|temp|template)', (err, dirs) => {
  if (err) {
    console.log(err);
  } else {
    dirs.forEach(file => fs.rmSync(file, { recursive: true }));
  }
});

if (fs.existsSync('package-feed.xml')) {
  fs.rmSync('package-feed.xml');
}