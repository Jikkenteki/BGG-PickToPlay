(ns bbg-reframe.routes
  (:require ["sax" :as sax]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.views.collections-view.collections-view :refer [collections-view-panel]]
            [bbg-reframe.views.game-view.game-view :refer [game-view-panel]]
            [bbg-reframe.views.home-view.home-view :refer [home-panel]]
            [bbg-reframe.views.login-view.login-view :refer [login-view-panel]]
            [re-frame.core :as re-frame]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(defmulti panels identity)
(defmethod panels :default [] [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200 "No panel found for this route."])
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :game-view-panel [] [game-view-panel])
(defmethod panels :login-view-panel [] [login-view-panel])
(defmethod panels :collections-view-panel [] [collections-view-panel])

;; main
(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (panels @active-panel)))
