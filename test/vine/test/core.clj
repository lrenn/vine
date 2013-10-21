(ns vine.test.core
  (:use [vine.core])
  (:use [clojure.test]))

(def project {:name   "simple"
              :group   "vine.test"
              :version "1.0.0-SPAPSHOT"
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
