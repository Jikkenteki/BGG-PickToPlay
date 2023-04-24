(ns bbg-reframe.routes
  (:require ["sax" :as sax]
            [bbg-reframe.panels.collections.collection-panel :refer [collection-view-panel]]
            [bbg-reframe.panels.collections.collections-panel :refer [collections-view-panel]]
            [bbg-reframe.panels.game.game-panel :refer [game-view-panel]]
            [bbg-reframe.panels.home.components.bottom-buttons-bar-comp :refer [bottom-buttons-bar-comp]]
            [bbg-reframe.panels.home.home-panel :refer [home-panel]]
            [bbg-reframe.panels.login.login-panel :refer [login-view-panel]]
            [bbg-reframe.subs :as subs]
            [re-frame.core :as re-frame]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(defmulti panels identity)
(defmethod panels :default [] [:p "No panel found for this route."])
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :game-view-panel [] [game-view-panel])
(defmethod panels :login-view-panel [] [login-view-panel])
(defmethod panels :collections-view-panel [] [collections-view-panel])
(defmethod panels :collection-view-panel [] [collection-view-panel])

;; main
(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div.max-w-xl.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [:div.flex-1.min-h-0 (panels @active-panel)]
     [bottom-buttons-bar-comp]]))
