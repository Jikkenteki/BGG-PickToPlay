(ns bbg-reframe.components.search-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]))

(defn search-comp
  []
  (let [substring (re-frame/subscribe [::subs/substring-query])]
    [:input.input-box.min-w-0;.grow.h-full
     {:type "text"
      :id "name"
      :value @substring
      :placeholder "Search games (type at least two characters)"
      :on-change #(re-frame/dispatch [::events/search-name-with-substring (-> % .-target .-value)])}]))