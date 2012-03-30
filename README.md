# vine

Vine is a library to allow leiningen to resolve dependencies via
[Apache Ivy](http://ant.apache.org/ivy/). 

The goal is not to replace aether and maven in leiningen, but only to
provide a solution for those of us with a need for Ivy resolution.

## Features

* :conf - Ivy configurations.  Map to Maven scopes when using maven
  repositories.

```clojure
      [foo "1.0.0+" :conf "master"]
      [foo "1.0.0+" :conf "devel->master"] ; (even if added as a non-dev dependency would be treated as one)
```      
      

* :transitive true/false - Resolve transitively or not (pull down just that modules artifacts, or that modules artifacts and all of it's dependencies artifacts).
```clojure
      [foo "1.0.0+" :transitive false]
```

* latest.integration/latest.release revisions.
```clojure      
      [foo "latest.integration"]
      [bar "latest.release"]
```

* branches - In addition to revisions, dependencies can also have a branch.  
```clojure      
      [scratch "latest.integration" :branch "foo"]
```

* force revision - By specifying `:force "true"` in a dependency you can stop conflict managers from evicting old revisions.

* changing modules - By specifying `:changing "true"` you can have ivy ignore the cache for this particular dependency.

* `:ivysettings "path/to/ivysettings.xml"` - You can specify your own [ivysettings file](http://ant.apache.org/ivy/history/latest-milestone/settings.html).  Note that the default clojure and clojars repositories are not automatically added when supplying a custom ivysettings file.  In addition, any :repositories in project.clj will be added to an ivy chain named "default".

## Usage

Bootstrapping the leiningen ivy branch takes a bit of work.  I'll
assume you have leiningen 1.7.0 installed already and named lein.

     git clone git://github.com/lrenn/vine.git

     cd vine && lein install && cd  ..

     git clone git://github.com/lrenn/leiningen.git
     
     cd leiningen
     
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
