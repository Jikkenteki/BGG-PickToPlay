(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.model.sort-filter :refer [rating-for-number-of-players sorting-fun]]
   [goog.string :as gstring]
   [goog.string.format]
   ["sax" :as sax]
   [bbg-reframe.model.db :refer [all-fields]]))


; required for tubax to work
(js/goog.exportSymbol "sax" sax)



(defn game-div
  [game]
  (let [;_ (println game)
        ]
    ^{:key (random-uuid)}
    [:p (:name game)]))

(defn result-div
  [result]
  [:div.pl-6.flex-initial.overflow-auto
   (map
    (fn [game]
      (game-div game))
    result)])

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
    [:div.flex.justify-between
     [:label label " " @value]
     [:input {:type "range" :min min :max max :step step :value @value :id id
              :onChange #(re-frame/dispatch [::events/update-form
                                             id
                                             (-> % .-target .-value)])}]]))



(defn main-panel []
  (let [result (re-frame/subscribe [::subs/result])]
    [:div.container.p-3.flex.flex-col.max-h-screen
     [:h1.text-3xl.font-bold.mb-2
      "HMPWTP "
      [:span.text-sm.font-normal "aka 'Help me pick what to play'"]]
     (slider :take "Take" 1 25 1)
     (slider :higher-than "Rating higher than" 0 10 0.1)
     (slider :players "For number of players" 1 10 1)
     (slider :threshold "Playability threshold" 0 0.95 0.05)
     (slider :time-limit "Time limit" 0 500 10)
     [:h3 "Games"]
     (result-div @result)
     (select :sort-id "Sort by " (map name (keys sorting-fun)))]))
