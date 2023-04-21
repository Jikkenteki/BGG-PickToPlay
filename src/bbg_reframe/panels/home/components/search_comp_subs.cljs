(ns bbg-reframe.panels.home.components.search-comp-subs 
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-sub
 ::game-substring-query
 (fn [db]
   (get-in db [:search :substring])))

(re-frame/reg-sub
 ::games-search-results
 (fn [db]
   (get-in db [:search :search-results])))