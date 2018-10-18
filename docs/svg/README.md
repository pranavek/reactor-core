# Contributing SVGs

## Contributing

The recommended workflow for editing or contributing new SVG marble diagrams is as follows:

 - Use a standard SVG editor (the cross-platform standalone editor we would recommend is [`Inkscape`](https://inkscape.org))
 - For main labels (eg. for the operator name in a box), use the `Tahoma` font family and fall back to `Nimbus Sans L` for Linux:
   - `font-family:'Tahoma','Nimbus Sans L'`
 - For labels that should look more like code, use `Lucida Console` font family and fallback to `Courier New`:
   - `font-family:'Lucida Console','Courier New'`
 - Preferably, run the new/edited SVG through an optimizer, but keep it pretty printed (see below)

All `*.svg` files found in `/docs/svg/marbles` folder will be copied during `javadoc` task to a `doc-files` folder at the level of the `Flux.java` and `Mono.java` classes.
This same folder will be embedded in the `-sources.jar` and `-javadoc.jar` artifacts.
It is ignored by git (`.gitignore`) and should **not** be committed to source control.

### Optimizing the SVG files

We want the SVG size to be kept as small and clutter-free as possible, as the images are embedded in the source and javadoc jars.
As a result, we recommend that an optimization step be taken when editing/creating SVGs.

That said, in order for the SVG source to be readable in editors, we prefer the SVG to be kept pretty printed.

We also had a lot of problems with arrowheads disappearing during optimizations, which you should keep an eye out for.

So the recommended configuration for optimizing is as follows:

 - Use [svgo](https://github.com/svg/svgo) (it is an NPM module, but you can get it on OSX via Homebrew: `brew install svgo`)
 - Use the following command from the repo root to maintain arrowheads and pretty printing while optimizing the SVGs correctly:
 
```sh
svgo --folder="docs/svg/marbles/" --multipass --pretty --indent=2 --precision=0 --disable={cleanupIDs,removeNonInheritableGroupAttrs}
```

Alternatively, to optimize a single SVG file, use the `--input` option:

```sh
svgo --input="docs/svg/marbles/reduce.svg" --multipass --pretty --indent=2 --precision=0 --disable={cleanupIDs,removeNonInheritableGroupAttrs}
```

