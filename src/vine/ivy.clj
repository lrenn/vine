(ns vine.ivy
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:use [flatland.ordered.map :as ordered])
  (:import [org.apache.ivy Ivy]
           [org.apache.ivy.core.report ArtifactDownloadReport ResolveReport]
           [org.apache.ivy.core.resolve ResolveOptions]
           [org.apache.ivy.core.retrieve RetrieveOptions]
           [org.apache.ivy.core.publish PublishOptions]
           [org.apache.ivy.core.deliver DeliverOptions]
           [org.apache.ivy.core.module.id ArtifactId ModuleId ModuleRevisionId]
           [org.apache.ivy.core.module.descriptor DefaultExcludeRule]
           [org.apache.ivy.core.module.descriptor DefaultModuleDescriptor]
           [org.apache.ivy.core.module.descriptor DefaultDependencyDescriptor]
           [org.apache.ivy.plugins.resolver IBiblioResolver]
           [org.apache.ivy.plugins.matcher ExactPatternMatcher PatternMatcher]
           [org.apache.ivy.util.filter FilterHelper]
           [org.apache.ivy.util DefaultMessageLogger Message]))

(def artifact-pattern "[organisation]/[module]/([branch]/)[revision]/[type]s/[artifact].[ext]")

(def default-repos (ordered/ordered-map
                    "central" {:url "http://repo1.maven.org/maven2"}
                    "clojars" {:url "http://clojars.org/repo/"}))

(defn ibiblio-resolver
  "Creates a maven compatible ivy resolver from a lein repository."
  [id repo]
  (let [repo (if (map? repo) repo {:url repo})]
    (doto (IBiblioResolver.)
      (.setRoot (:url repo))
      (.setName id)
      (.setM2compatible true))))

(defn make-resolvers
  "Generates a list of ibiblio resolvers based on the repositories defined in the project."
  [project]
  (let [repos (concat default-repos (:repositories project))]
    (map (fn [[id repo]] (ibiblio-resolver id repo)) repos)))

(defn add-resolvers
  "Adds resolvers to an Ivy instance, defaulting to the main chain"
  ([ivy resolvers]
     (add-resolvers ivy resolvers "main"))
  ([ivy resolvers chain]
     (let [settings (.getSettings ivy)
           chain (.getResolver settings chain)]
       (doseq [r resolvers]
         (.setSettings r settings)
         (.addResolver settings r)
         (.add chain r)))
     ivy))

(defn- make-logger
  [level]
  (proxy [DefaultMessageLogger] [level]
    (log [msg l]
      (if (<= l (.getLevel this))
        (println msg)))
    (doProgress [] (print "."))
    (doEndProgress [msg] (println msg))))

(defn- make-ivy
  []
  (let [ivy (Ivy/newInstance)]
    (doto (.getLoggerEngine ivy)
      (.pushLogger (make-logger Message/MSG_INFO)))
    ivy))

(defn ivy
  "Create a new Ivy instance."
  ([]
     (let [ivy (make-ivy)]
       (.configureDefault ivy)
       ivy))
  ([settings]
     (if-not settings
       (ivy)
       (let [ivy (make-ivy)]
         (.configure ivy (.toURL (io/file settings)))
         ivy))))

(defn ivy-resolve-xml
  [ivy ivy-xml & {:keys [conf types]
                  :or   {conf  "*"
                         types "jar,bundle"}
                  :as   opts}]
  (let [engine  (.getResolveEngine ivy)
        options (ResolveOptions.)]
    (.setConfs options (into-array (string/split conf #",")))
    (.setArtifactFilter options (FilterHelper/getArtifactTypeFilter types))
    (.resolve ivy (.toURL ivy-xml) options)))
