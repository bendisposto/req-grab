(ns req-grab.grabber
  (:require [opennlp.nlp :as nlp]
            [opennlp.treebank :as treebank]
            [clojure.java.io :as io])
  (:import [org.apache.pdfbox.pdmodel PDDocument]
           [org.apache.pdfbox.util PDFTextStripper]
           java.net.URL
           java.io.File)
  (:gen-class))

(defn text-of-pdf
  [filename]
  (with-open [pd (PDDocument/load (io/as-url (File. filename)))]
    (let [stripper (PDFTextStripper.)]
      (.getText stripper pd))))

(def text (text-of-pdf "HD-CaseStudy-Description.pdf"))

(def get-sentences (nlp/make-sentence-detector "models/en-sent.bin"))
(def tokenize (nlp/make-tokenizer "models/en-token.bin"))
(def chunker (treebank/make-treebank-chunker "models/en-chunker.bin"))
(def pos-tag (nlp/make-pos-tagger "models/en-pos-maxent.bin"))
