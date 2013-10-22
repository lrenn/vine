(defproject vine "0.2.1-SNAPSHOT"
  :description "Generate and resolve Clojure project.clj files via Apache Ivy."
  :url "http://github.com/lrenn/vine"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :description "Resolve leiningen projects with Apache Ivy."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.xml "0.0.7"]
                 [org.flatland/ordered "1.5.1"]
                 [org.apache.ivy/ivy "2.3.0"]
                 [me.raynes/fs "1.4.4"]])
