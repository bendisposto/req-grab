(defproject req-grab "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.apache.pdfbox/pdfbox "1.8.9"]
                 [clojure-opennlp "0.3.3"]
                 [instaparse "1.4.0" :scope "provided"]]
  :main req-grab.grabber
  :aot :all
  :source-paths ["src"]
)
