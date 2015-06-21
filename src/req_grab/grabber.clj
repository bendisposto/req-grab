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
