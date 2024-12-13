origin_version=$(git show origin/master:chrome/extensions/splunk-uml-renderer/manifest.json | fgrep '"version"' | sed 's/.*: "//' | sed 's/".*//')
this_version=$(fgrep '"version"' manifest.json | sed 's/.*: "//' | sed 's/".*//')

if [[ "$origin_version" == "$this_version" ]]; then
  echo "Error: Version unchanged at $origin_version"
  exit 1
fi

# remove "key" from manifest.json before zipping

mv manifest.json manifest_orig.json
cat manifest_orig.json | grep -v key > manifest.json

zip -FS -r -Z store "dist.zip" . -x manifest_orig.json -x build.sh -x README.md -x promo.png -x *.zip -x .*

rm manifest.json
mv manifest_orig.json manifest.json
