(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.model.db :refer [all-fields]]))


(defn field-div
  [fields field]
  ^{:key (random-uuid)} [:div
   [:input {:type "checkbox" 
            :name field 
            :value field
            :checked (some #(= field %) fields)
            :on-change (fn [e]
            (re-frame/dispatch [::events/field field]))             
            }]
   [:label field]])

(defn fields-div
  [fields]
  [:div "Fields"
   [:form
    (map #(field-div fields %) all-fields)]])

(defn game-div
  [game fields]
  ^{:key (random-uuid)} [:tr
   (map (fn [field]
          ^{:key (random-uuid)} 
          [:td (game (keyword field))])
        fields)])

(defn result-div
  [collection fields]
  (let [fields-sorted (filter (fn [f] (some #(= f %) fields)) all-fields)]
  [:div "Games"
   [:table
     [:tr
     (map (fn [field]
            ^{:key (random-uuid)} 
            [:th field])
          fields-sorted)]
     (map 
      (fn [game] 
        (game-div game fields-sorted)) 
      collection)]])  )
  
(defn sorting-div
  []
  [:div "Sort by "
   [:select
    [:option "rating"]
    [:option "time"]]]
  )

(defn main-panel []
  (let [result (re-frame/subscribe [::subs/result])
        fields (re-frame/subscribe [::subs/fields])]
    [:div
     [:h1
      "BGG "]
     (fields-div @fields)
     (sorting-div)
     (result-div @result @fields)]))
