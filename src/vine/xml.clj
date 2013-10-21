(ns vine.xml
  (:require [clojure.java.io :as io]
            [clojure.data.xml :as xml]
            [clojure.set :refer (rename-keys)]))

(defn group
  "Gets the group id out of a dependency or exclude like 'vine.core => vine, 'vine => vine."
  [dep]
  (or (namespace dep) (name dep)))

(defn exclusion-opts
  [opts]
  (-> (apply hash-map opts)
      (select-keys [:extension :classifier])
      (rename-keys {:extension :ext :classifier :m:classifier})))

(defn exclusion
  [[dep & opts]]
  (let [opts (exclusion-opts opts)]
    [:exclude (merge {:org (group dep) :name (name dep)} opts)]))

(defn make-dependencies
  [deps default-conf]
  (for [[dep version & opts] deps]
    (let [opts (apply hash-map opts)]
      `[:dependency
        ~(merge (if (:classifier opts) {:m:classifier (:classifier opts)})
                (select-keys opts [:branch :transitive :force :changing])
                {:conf (or (:conf opts) default-conf)
                 :rev version :name (name dep) :org (group dep)})
        ~@(if-let [exclusions (:exclusions opts)]
            (vec (map exclusion exclusions)))])))

(defn dependencies
  [project]
  `[:dependencies
    ~@(make-dependencies (:dependencies project) "default->default")
    ~@(make-dependencies (:plugins project) "plugins->default")])

(def default-confs
  {:confs [{:name "master"}
           {:name "default"}
           {:name "plugins" :visibility "private"}]})

(defn configurations
  [project]
  (let [confs (or (:configurations project) default-confs)]
    `[:configurations
      ~(select-keys confs [:defaultconf :defaultconfmapping :confmappingoverride])
      ~@(for [conf (:confs confs)]
          [:conf conf])]))

(defn default-pubs
  [project]
  {:artifacts [{:name (:name project)
                :type "jar"
                :conf "master,default"}]})

(defn publications
  [project]
  (let [pubs (or (:publications project) (default-pubs project))]
    `[:publications
      ~(select-keys pubs [:defaultconf])
      ~@(for [artifact (:artifacts pubs)]
          [:artifact artifact])]))

(defn ivy-sexp
  [project]
  (let [description (:description project)
        url (:url project)
        license (:license project)]
    [:ivy-module {:version "2.0"
                  :xmlns:m "http://ant.apache.org/ivy/maven"}
     [:info {:organisation (:group project)
             :module (:name project)
             :revision (:version project)}      
      (if (or url description)
        [:description
         (if url {:homepage url})
         (if description description)])]
     (configurations project)
     (publications project)
     (dependencies project)]))

(defn ivy-element
  [project]
  (xml/sexp-as-element (ivy-sexp project)))

(defn make-ivy-xml
  [project]
  (xml/indent-str (ivy-element project)))

(defn ivy-xml
  "Write a ivy.xml file to disk for Ivy interoperability."
  ([project ivy-location silently?]
     (when-let [ivy (make-ivy-xml project)]
       (let [ivy-file (io/file "." ivy-location)]
         (.mkdirs (.getParentFile ivy-file))
         (with-open [ivy-writer (io/writer ivy-file)]
           (.write ivy-writer ivy))
         (when-not silently? (println "Wrote" (str ivy-file)))
         ivy-file)))
  ([project ivy-location] (ivy-xml project ivy-location true))
  ([project] (ivy-xml project ".ivy.xml")))
