(ns vine.core
  (:require [clojure.java.io :as io]
            [vine.ivy :refer :all]
            [vine.xml :refer :all])
  (:refer-clojure :exclude [deliver resolve]))

(defn ivy-instance
  "Initializes and returns an Ivy instance given a project map."
  [project]
  (if-let [settings (:ivy-settings project)] 
    (ivy (:ivy-settings project))
    (add-resolvers (ivy) (make-resolvers project))))

(defn ivy-resolve
  "Resolves a project using the supplied ivy instance."
  [ivy project]
  (resolve ivy (ivy-xml project)))

(defn ivy-publish
  [ivy report & opts]
  (apply publish ivy report opts))

(defn report-files
  "Returns a list of Files for a given resolve report and
   dependency key.  Dependency keys are mapped to configurations."
  [report dependencies-key]
  (let [conf (if (= :dependencies dependencies-key)
               "default"
               (name dependencies-key))
        artifact-reports (-> report
                             (.getConfigurationReport conf)
                             (.getAllArtifactsReports))]
    (map #(.getLocalFile %1) artifact-reports)))
