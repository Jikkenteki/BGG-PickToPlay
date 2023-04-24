(ns bbg-reframe.panels.collections.collection-panel
  (:require [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.events :as events]
            [bbg-reframe.panels.collections.collections-events :as collections-events]
            [bbg-reframe.panels.collections.collections-subs :as collection-subs]
            [bbg-reframe.panels.home.components.search-comp :refer [search-games-query-comp search-games-results-comp]]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.example.forms.bind :refer [bind-form-to-value!]]
            [re-frame.core :as re-frame]))

(defn- game-in-collection
  [id game]
  ^{:key (:id game)}
  [:div
   [:div
    [:button {:on-click
              #(re-frame/dispatch
                [::collections-events/remove-game-from-collection [(keyword id) (:id game)]])
              :class "fa fa-minus-square"}]
    [:button {:on-click #(re-frame/dispatch [::events/navigate [:game-view :id (:id game)]])}

     (:name game)]]])

(defn collection-view-panel
  []
  (let [_ (re-frame/dispatch [::search-comp-events/reset-search])
        route-params @(re-frame/subscribe [::subs/route-params 1])
        id (:id route-params)
        collection @(re-frame/subscribe [::collection-subs/collection id])
        collection-games @(re-frame/subscribe [::collection-subs/collection-games (name id)])
        form-path (bind-form-to-value!
                   {:new-name (:name collection)}
                   [:collection-form :edit-collection])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [:h1 "Collection with id: " id]
     [:div "Name: " (:name collection)]
     [:div
      [:button {:on-click
                #(re-frame/dispatch
                  [::collections-events/delete-collection (keyword id)])}
       "Delete"]]


     [:div
      [:label "Rename a collection"]
      [input-element {:class "input-box min-w-0"
                      :type :text
                      :placeholder "Collection name"
                      :path (into form-path [:new-name])}]
      [:button {:on-click #(re-frame/dispatch
                            [::collections-events/edit-collection-name [(keyword id) (into form-path [:new-name])]])} "Update"]]

     [:div.border-2.p-2
      [:h4 "Games in collection"]
      (map #(game-in-collection id %) collection-games)]

     [:div
      [:label "Add Game to collection"]
      [search-games-query-comp]
      [search-games-results-comp (fn [game-id] (re-frame/dispatch [::collections-events/add-game-to-collection [(keyword id) game-id]]))]]]))