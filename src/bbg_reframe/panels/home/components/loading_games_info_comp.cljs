(ns bbg-reframe.panels.home.components.loading-games-info-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]))

(defn loading-games-info-comp []
  (let [loading @(re-frame/subscribe [::subs/loading])]
    (when loading
      [:div.flex.ml-3
       [:div.sk-folding-cube
        [:div.sk-cube1.sk-cube]
        [:div.sk-cube2.sk-cube]
        [:div.sk-cube4.sk-cube]
        [:div.sk-cube3.sk-cube]]
       [:p.ml-2.inline.italic "loading games..."]])))