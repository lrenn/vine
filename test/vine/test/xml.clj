(ns vine.test.xml
  (:use [vine.xml]
        [clojure.test]
        [clojure.data.xml :as xml]))

(def extended-project {:name   "test"
                       :group   "vine"
                       :version "1.0.0-SPAPSHOT"
                       :description "A test project for vine."
                       :url "http://github.com/lrenn/vine"
                       :dependencies '[[org.clojure/clojure "1.3.0"]
                                       [ring/ring-core "1.0.0-RC1"
                                        :exclusions [commons-codec]]
                                       [foo/bar "1.0.0" :branch "boo"]]})

(def extended-project-sexp
  [:ivy-module {:xmlns:m "http://ant.apache.org/ivy/maven", :version "2.0"}
   [:info {:organisation "vine", :module "test", :revision "1.0.0-SPAPSHOT"}
    [:description {:homepage "http://github.com/lrenn/vine"} "A test project for vine."]]
   [:configurations {}
    [:conf {:name "master"}]
    [:conf {:name "default"}]
    [:conf {:visibility "private", :name "devel"}]]
   [:publications {} [:artifact {:name "test", :type "jar", :conf "master,default"}]]
   [:dependencies
    [:dependency {:org "org.clojure", :name "clojure", :rev "1.3.0", :conf "default->default"}]
    [:dependency {:org "ring", :name "ring-core", :rev "1.0.0-RC1", :conf "default->default"}
     [:exclude {:org "commons-codec", :name "commons-codec"}]]
    [:dependency
     {:org "foo", :name "bar", :rev "1.0.0", :conf "default->default", :branch "boo"}]]])

(def simple-project {:name   "test"
                     :group   "vine"
                     :version "1.0.0-SPAPSHOT"
                     :description "A test project for vine."
                     :url "http://github.com/lrenn/vine"
                     :dependencies '[[org.clojure/clojure "1.3.0"]
                                     [ring/ring-core "1.0.0-RC1"]
                                     [foo/bar "1.0.0"]]})


(def simple-project-sexp
  [:ivy-module {:xmlns:m "http://ant.apache.org/ivy/maven", :version "2.0"}
   [:info {:organisation "vine", :module "test", :revision "1.0.0-SPAPSHOT"}
    [:description {:homepage "http://github.com/lrenn/vine"} "A test project for vine."]]
   [:configurations {} [:conf {:name "master"}]
    [:conf {:name "default"}]
    [:conf {:visibility "private", :name "devel"}]]
   [:publications {} [:artifact {:name "test", :type "jar", :conf "master,default"}]]
   [:dependencies
    [:dependency {:org "org.clojure", :name "clojure", :rev "1.3.0", :conf "default->default"}]
    [:dependency {:org "ring", :name "ring-core", :rev "1.0.0-RC1", :conf "default->default"}]
    [:dependency {:org "foo", :name "bar", :rev "1.0.0", :conf "default->default"}]]])

(deftest test-sexps
  (are [x y] (= x y)
       extended-project-sexp (ivy-sexp extended-project)
       simple-project-sexp   (ivy-sexp simple-project)))

(deftest test-extended
   (let [e (ivy-element extended-project)
         s (xml/indent-str e)]
     (comment (println s))
     (is (= :ivy-module (:tag e)))))

(deftest test-simple
   (let [e (ivy-element simple-project)
         s (xml/indent-str e)]
     (comment (println s))
     (is (= :ivy-module (:tag e)))))
