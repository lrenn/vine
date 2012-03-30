# vine

Vine's sole purpose is to allow leiningen to resolve dependencies via
Ivy. 

The goal is not to replace aether and maven in leiningen, but only to
provide a solution for those of us with a need for Ivy resolution.

## Usage

Vine needs to be installed to use an unofficial ivy branch of
leiningen.  This branch is in no way affiliated with mainline
leiningen development.  This assumes you have lein1 installed.

     cd src

     git clone <vine url>

     cd vine && lein install

     git clone <leiningen/ivy url>
     
     cd ../leiningen
     
     git checkout ivy
     
     cd leiningen-core && lein install
     
     cd ..
     
     ln -s bin/lein ~/bin/lein-ivy
     
     cd ../some-project
     
     lein-ivy deps
     
## Cake/Ivy

Work had previously been done to
[port the Cake build system to Ivy](https://github.com/lrenn/cake/wiki/Ivy).
The current plan is to support resolution via changes to
leiningen-core, and provide some of the functionality of the cake port
via a leiningen:ivy plugin.

## License

Copyright (C) 2012 Luke Renn

Distributed under the Eclipse Public License, the same as Clojure.
