(ns bbg-reframe.panels.collections.collections-subs
  (:require [bbg-reframe.model.collection :refer [get-collections]]
            [re-frame.core :as re-frame]))

;; ;; auth-collections
;; (re-frame/reg-sub
;;  ::collections-auth
;;  :<- [::auth-subs/on-auth-value ["collections"]]
;;  (fn [coll [_]]
;;    coll))

;; read from db since fb might be offline
;; auth-collections
(re-frame/reg-sub
 ::collections
 (fn [db]
   (get-collections (:collections db))))