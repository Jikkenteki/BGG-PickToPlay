(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.model.db :refer [all-fields]]
   [bbg-reframe.model.sort-filter :refer [rating-for-number-of-players sorting-fun]]
   [goog.string :as gstring]
   [goog.string.format]))


(defn field-div
  [fields field]
  ^{:key (random-uuid)}
  [:label [:input {:type "checkbox"
                   :name "field"
                   :value field
                   :checked (some #(= field %) fields)
                   :on-change #(re-frame/dispatch
                                [::events/field field (-> % .-target .-value)])}]
   field])

(defn fields-div
  [fields]
  [:div
   [:form
    [:fieldset
     [:legend " Show fields"]
     (map #(field-div fields %) all-fields)]]])

(defn game-div
  [game fields players]
  (let [playability (gstring/format "%.2f" (rating-for-number-of-players
                                            game players))]
    ^{:key (random-uuid)}
    [:tr
     (map (fn [field]
            ^{:key (random-uuid)}
            [:td (if (= field "playability")
                   playability
                   (game (keyword field)))])
          fields)]))

(defn result-div
  [result fields players]
  (let [fields-sorted (filter (fn [f] (some #(= f %) fields)) all-fields)]
    [:div "Games "
     [:table
      [:tbody
       [:tr
        (map (fn [field]
               ^{:key (random-uuid)}
               [:th field])
             fields-sorted)]
       (map
        (fn [game]
          (game-div game fields-sorted players))
        result)]]]))

(defn select
  [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div
     [:label label]
     [:select {:value @value
               :on-change #(re-frame/dispatch [::events/update-form
                                               id
                                               (-> % .-target .-value)])}
      (map (fn [o] [:option {:key o :value o} o]) options)]]))

(defn slider
  [id label min max step]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div
     [:label label " " @value]
     [:br]
     [:input {:type "range" :min min :max max :step step :value @value :id id
              :onChange #(re-frame/dispatch [::events/update-form
                                             id
                                             (-> % .-target .-value)])}]]))

(defn main-panel []
  (let [result (re-frame/subscribe [::subs/result])
        fields (re-frame/subscribe [::subs/fields])
        players (re-frame/subscribe [::subs/form :players])]
    [:div
     [:h1
      "BGG "]
     (fields-div @fields)
     (select :sort-id "Sort by " (map name (keys sorting-fun)))
     (slider :take "Take" 1 25 1)
     (slider :higher-than "Rating higher than" 0 10 0.1)
     (slider :players "For number of players" 1 10 1)
     (slider :threshold "Playability threshold" 0 0.95 0.05)
     (slider :time-limit "Time limit" 0 500 10)
     (result-div @result @fields @players)]))
