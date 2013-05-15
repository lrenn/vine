# vine

Vine is a library for resolving dependencies via
[Apache Ivy](http://ant.apache.org/ivy/) and generating ivy.xml files for clojure project definitions.
It is used by the lein-ivy leiningen plugin to allow clojure projects to resolve against and publish to Ivy repositories.

## Features

* :conf - Ivy configurations.  Map to Maven scopes when using maven
  repositories.

```clojure
      [foo "1.0.0+" :conf "master"]
      [bar "1.0.0+" :conf "jetty"]
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

See [lein-ivy](https://github.com/lrenn/lein-ivy).
     
## TODO

Vine and lein-ivy should eventually support all the features of the 
[Cake Ivy branch](https://github.com/lrenn/cake/wiki/Ivy).  Currently only resolution is supported.

## License

Copyright (C) 2012, 2013 Luke Renn

Distributed under the Eclipse Public License, the same as Clojure.
