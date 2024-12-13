# Splunk UML Renderer

Renders uml that has been logged as a `uml` field.

In Java:

```java
log.info(Markers.append("uml", myUml), "My description of this uml");
```

Note: The uml value must be formatted with the usual newlines expected, although indentation is not required.

### Mermaid

This uses the Mermaid rendering library, which can be updated by downloading from https://unpkg.com/browse/mermaid@10.8.0/dist/mermaid.js

### Build

The "build" is just zipping everything in this folder (except for a couple of files, like this one)

`build.sh`

### Testing and using locally

fyi, this entry in `manifest.json` permits extension to access any internal site.

```
"key": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlbRkyHSX+vQUu1BTlO1tR41JD1EDg7G/ehOXHBXoP/85o3rwZPfTEn16yM9+McSjrS9VFlk88YztWz70RLkE9ChDGfeZY0Y+/BCiiFGfiFx4kt27hUAWM0BHYHkvugDPwo9oOsg+nOmXKgViXcYKRmEVIYiLKNjgapsTm7Z3i8E9OaR3OZU7TYBbOgWfefGU95sjLoUkRYwyAZfGQxfW8JdYplyFo0kQaCJbQJGmBlhI3EZ1Smo61kgtS9gJZwtpcicw/q9izWQ4SCaT2l6Grn5cWsG/4tVqCPkUYULoqaqcdzijsu5wSMAsnsl4/nrQT84lqVMJ6PxSWdbgwA2ByQIDAQAB"
```

It may be committed to the repo and published to Chrome (which deletes it).
