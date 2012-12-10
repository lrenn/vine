(ns vine.core
  (:require [clojure.java.io :as io])
  (:use [vine.ivy]
        [vine.xml]))

(defn report-files
  "Returns a list of Files for a given resolve report."
  [report dependencies-key]
  (let [conf (if (= :dependencies dependencies-key)
               "default"
               (name dependencies-key))
        artifact-reports (.getAllArtifactsReports
                          (.getConfigurationReport report conf))]
    (map #(.getLocalFile %1) artifact-reports)))

(defn ivy-instance
  "Initializes and returns an Ivy instance given a project map."
  [project]
  (if-let [settings (:ivy-settings project)] 
    (ivy (:ivy-settings project))
    (add-resolvers (ivy) (make-resolvers project))))

(defn ivy-resolve
  [project]
  (let [ivy-xml (ivy-xml project)
        ivy     (ivy-instance project)]
    (ivy-resolve-xml ivy ivy-xml)))

(defn resolved-files
  [project dependencies-key]
  (let [report (ivy-resolve project)]
    (report-files report dependencies-key)))
