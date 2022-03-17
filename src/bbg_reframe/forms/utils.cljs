(ns bbg-reframe.forms.utils
  (:require [clojure.string :as s]))

(defn is-substring?
  [substring str]
  (s/includes? (s/upper-case str) (s/upper-case substring)))


(defn if-nil?->value
  [v default]
  (if (nil? v) default v))
(if-nil?->value true false)



(defn find-key-value-in-map-list
  "Search a list of maps and return the first map in which the key value pair exists."
  [list keyword value]
  (->> list
       (filter (fn [m] (= value (keyword m))))
       first))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn random-char
  []
  (nth (->> (range 10)
            (map #(+ 65 %))
            (map char)
            (into [])) (rand-int 10)))

(defn random-word
  [len]
  (->> (range len)
       (map (fn [_] (random-char)))
       (s/join)))

(defn random-name-map
  [len] (->> (range len)
             (map (fn [n] {:id (str n) :name (random-word 10)}))
             (into [])))



(def random-names-new (random-name-map 300))


(comment
  (->> random-names-new
       (filter
        (fn [{:keys [_ name]}]
          (or (nil? "AA") (is-substring? "AA" name))))))

