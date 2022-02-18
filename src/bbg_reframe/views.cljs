(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]))


(defn field-div
  [field]
  [:div
   [:input {:type "checkbox" :name field :value field}]
   [:label field]])

(defn fields-div
  [fields]
  [:div "Fields"
   [:form
    (map field-div fields)]])

(defn game-div
  [game fields]
  [:tr
   (map (fn [field]
          [:td (game (keyword field))])
        fields)])

(defn result-div
  [collection fields]

  [:div "Result"
   [:table
    [:tr
     (map (fn [field]
            [:th field])
          fields)]
    (map #(game-div % fields) collection)]])


(defn main-panel []
  (let [result (re-frame/subscribe [::subs/result])
        fields (re-frame/subscribe [::subs/fields])]
    [:div
     [:h1
      "BGG "]
     [:div (fields-div @fields)]
     [:div (result-div @result @fields)]]))
