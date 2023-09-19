(ns bbg-reframe.panels.collections.collection-panel
  (:require [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.panels.collections.collections-events :as collections-events]
            [bbg-reframe.panels.collections.collections-subs :as collection-subs]
            [bbg-reframe.panels.collections.components.game-in-collection :refer [game-in-collection]]
            [bbg-reframe.panels.home.components.search-comp :refer [search-games-query-comp search-games-results-comp]]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.example.forms.bind :refer [bind-form-to-value!]]
            [re-frame.core :as re-frame]
            [reagent.core :as r]))

(defn collection-view-panel
  []
  (let [_ (re-frame/dispatch [::search-comp-events/reset-search])
        route-params @(re-frame/subscribe [::subs/route-params 1])
        id (:id route-params)
        collection (re-frame/subscribe [::collection-subs/collection id])
        collection-games (re-frame/subscribe [::collection-subs/collection-games (name id)])
        form-path (bind-form-to-value!
                   {:new-name (:name @collection)}
                   [:collection-form :edit-collection])
        state (r/atom {:collection-renaming? false
                       :games-searching? false})]
    (println id)
    (fn []
      [:div.flex.flex-col.gap-2.p-3.h-full
       (if (:collection-renaming? @state)
         [:div.flex.gap-2.min-w-0
          [input-element {:class "input-box text-2xl font-semibold"
                          :type :text
                          :placeholder "Collection name"
                          :path (into form-path [:new-name])
                          :auto-focus? true}]
          [:button.button.h-full {:on-click #(do (re-frame/dispatch
                                                  [::collections-events/edit-collection-name [(keyword id) (into form-path [:new-name])]])
                                                 (reset! state {:collection-renaming? false}))}
           [:i.fa-solid.fa-check]]]
         [:h1.cursor-default.py-2
          [:i.mr-1.font-normal.fa.fa-sm.fa-object-group] (:name @collection)])

       [:div.flex.justify-between.items-center.gap-2.pb-2
        [:div.flex.gap-2
         [:button.button.bg-slate-500
          {:class "hover:!bg-slate-400"
           :on-click
           #(re-frame/dispatch
             [::collections-events/delete-collection (keyword id)])}
          "Delete"]
         [:button.button {:class (when
                                  (:collection-renaming? @state) "bg-primary-dark  hover:!bg-primary-dark")
                          :on-click
                          #(swap! state assoc-in
                                  [:collection-renaming?]
                                  (not (:collection-renaming? @state)))}
          (if (:collection-renaming? @state) "Cancel rename" "Rename")]]]

       [:div.flex.justify-between.items-center
        [:h2 "Games in collection"]
        [:button.button {:class (when (:games-searching? @state) "active")
                         :on-click #(swap! state update-in [:games-searching?] not)}
         "Add Game to collection"]]

       [:div.flex-1.overflow-auto.relative
        (when (:games-searching? @state)
          [:div.absolute.flex.flex-col.w-full.bg-stone-750.rounded-xl.py-1.shadow-2xl
           {:class "h-1/2 top-1/4"}
           [search-games-query-comp]
           [search-games-results-comp
            true
            true
            (fn [game-id] (re-frame/dispatch [::collections-events/add-game-to-collection [(keyword id) game-id]]))]])
        (map #(game-in-collection id %) @collection-games)]])))



(comment
  (def state (r/atom {:collection-renaming? false
                      :games-searching? false}))
  (swap! state update-in [:games-searching?] not)
  @state
  ;;
  )
