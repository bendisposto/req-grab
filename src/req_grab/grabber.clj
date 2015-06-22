(ns req-grab.grabber
  (:require [opennlp.nlp :as nlp]
            [opennlp.treebank :as treebank]
            [clojure.java.io :as io])
  (:import [org.apache.pdfbox.pdmodel PDDocument]
           [org.apache.pdfbox.util PDFTextStripper]
           java.net.URL
           java.io.File)
  (:gen-class))

(defn- text-of-pdf
  [filename]
  (with-open [pd (PDDocument/load (io/as-url (File. filename)))]
    (let [stripper (PDFTextStripper.)]
      (.getText stripper pd))))

(def get-sentences (nlp/make-sentence-detector "models/en-sent.bin"))
(def tokenize (nlp/make-tokenizer "models/en-token.bin"))
(def chunker (treebank/make-treebank-chunker "models/en-chunker.bin"))
(def pos-tag (nlp/make-pos-tagger "models/en-pos-maxent.bin"))

(defn- fix-linebreaks [sentence]
  (let [complex-breaks (.replaceAll sentence "-\n" "")]
    (.replaceAll complex-breaks "\n" " ")))

(defn read-sentences [filename]
  (let [text (text-of-pdf filename)
        sentences (map fix-linebreaks (get-sentences text))]
    sentences))

(def text (read-sentences "HD-CaseStudy-Description.pdf"))

(defn read-stopwords []
  (with-open [rdr (clojure.java.io/reader "SmartStoplist.txt")]
    (into #{} (line-seq rdr))))

(defn stopword? [stopwords word]
  (get stopwords (.toLowerCase word)))

(defn candidates [words stopwords]
  (remove (partial stopword? stopwords) words))

(defn phrases [sentences]
  (->> sentences
       (mapcat tokenize)
       pos-tag
       chunker))

(defn noun-phrases [stopwords phrases]
  (->> phrases
       (filter #(= (:tag %) "NP"))
       (map :phrase)
       (map #(remove (partial stopword? stopwords) %))
       (remove empty?)))

(defn sorted-phrases [sentences]
  (->> sentences
      phrases
      (noun-phrases #{"." "," ";" ":" "!" "?"})
      frequencies
      (sort-by second)
      reverse))
