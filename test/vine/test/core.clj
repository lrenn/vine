(ns vine.test.core
  (:use [vine.core])
  (:use [clojure.test]))

(def test-project {:name   "test"
                   :group   "vine"
                   :version "1.0.0-SPAPSHOT"
                   :description "A fast library for rendering HTML in Clojure"
                   :url "http://github.com/weavejester/hiccup"
                   :dependencies '[[org.clojure/clojure "1.3.0"]
                                   [ring/ring-core "1.0.0-RC1"
                                    :exclusions [commons-codec]]]})

(deftest test-resolve-dependencies-xml
  (let [files  (resolve-dependencies-xml test-project :dependencies)]
    (is (= 5 (count files)))))
