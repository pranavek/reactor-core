# Contributing SVGs

Most images are in SVG format and represent a marble diagram.
Such SVG are embedded in the javadoc jar, and as such need to be in a `doc-files` folder inside the source hierarchy.

As a result, most SVG live in `/reactor-core/src/main/java/reactor/core/publisher/doc-files/marbles/`.

Additional SVG that are specific to the reference documentation can be added later in a subfolder here in `/docs/svg/`.

## Contributing Marble Diagrams

The recommended workflow for editing or contributing new SVG marble diagrams is as follows:

 - Use a standard SVG editor (the cross-platform standalone editor we would recommend is [`Inkscape`](https://inkscape.org))
 - For main labels (eg. for the operator name in a box), use the `Tahoma` font family and fall back to `Nimbus Sans L` for Linux:
   - `font-family:'Tahoma','Nimbus Sans L'`
 - For labels that should look more like code, use `Lucida Console` font family and fallback to `Courier New`:
   - `font-family:'Lucida Console','Courier New'`
 - Preferably, run the new/edited SVG through an optimizer, but keep it pretty printed (see below)

All `*.svg` marble diagrams are found in `/reactor-core/src/main/java/reactor/core/publisher/doc-files/marbles/` folder.
This same folder will be embedded in the `-sources.jar` and `-javadoc.jar` artifacts.

### Optimizing the SVG files

We want the SVG size to be kept as small and clutter-free as possible, as the images are embedded in the source and javadoc jars.
As a result, we recommend that an optimization step be taken when editing/creating SVGs.

That said, in order for the SVG source to be readable in editors, we prefer the SVG to be kept pretty printed.

We also had a lot of problems with arrowheads disappearing during optimizations, which you should keep an eye out for.

So the recommended configuration for optimizing is as follows:

 - Use [svgo](https://github.com/svg/svgo) (it is an NPM module, but you can get it on OSX via Homebrew: `brew install svgo`)
 - Use the following command from the repo root to maintain arrowheads and pretty printing while optimizing the SVGs correctly:
 
```sh
svgo --folder="reactor-core/src/main/java/reactor/core/publisher/doc-files/marbles/" --multipass --pretty --indent=2 --precision=0 --disable={cleanupIDs,removeNonInheritableGroupAttrs}
```

Alternatively, to optimize a single SVG file, use the `--input` option:

```sh
svgo --input="reactor-core/src/main/java/reactor/core/publisher/doc-files/marbles/reduce.svg" --multipass --pretty --indent=2 --precision=0 --disable={cleanupIDs,removeNonInheritableGroupAttrs}
```

