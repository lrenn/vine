(ns vine.core
  (:require [clojure.java.io :as io])
  (:use [vine.ivy]
        [vine.xml]))

(defn report-files
  "Returns a list of Files for a given resolve report."
  [report]
  (map #(.getLocalFile %1) (.getAllArtifactsReports report)))

(defn init-ivy
  "Initializes and returns an Ivy instance given a project map."
  [project]
  (if (:ivy-settings project)
    (ivy (:ivy-settings project))
    (add-resolvers (ivy) (make-resolvers project))))

(defn resolve-dependencies-xml
  [project depenencies-key]
  (binding [*out* (io/writer ".ivy-log")]
    (let [ivy-xml (ivy-xml project)
          ivy     (init-ivy project)]
      (report-files (ivy-resolve-xml ivy ivy-xml)))))