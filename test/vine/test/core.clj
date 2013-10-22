(ns vine.test.core
  (:require [vine.core :refer :all]
            [clojure.test :refer :all]
            [me.raynes.fs :as fs]))

(def project {:name   "simple"
              :group   "vine.test"
              :version "0.0.1-SNAPSHOT"
              :description "Simple test project."
              :url "https://github.com/lrenn/vine"
              :dependencies '[[org.clojure/clojure "1.5.1"]
                              [ring/ring-core "1.1.8"]]})

(deftest test-resolved-files
  (let [ivy (ivy-instance project)
        report (ivy-resolve ivy project)
        files (report-files report :dependencies)]
    (is (= 8 (count files)))
    (is (every? #(.exists %1) files))))

(def destination
  "~/.ivy2/local/vine.test/simple")

(def published-path
  (str destination "/0.0.1-SNAPSHOT/ivys/ivy.xml"))

(def delivered-path
  ".ivy-0.0.1-SNAPSHOT.xml")

(deftest test-publish
  (let [ivy (ivy-instance project)
        report (ivy-resolve ivy project)
        missing (ivy-publish ivy report :overwrite true)]
    ;; There's no jar in this test, so 1 file should be missing
    ;; The ivy file itself should get published though.
    (is (= (count missing) 1))
    (is (fs/exists? (fs/expand-home published-path)))
    (is (fs/exists? delivered-path))))
